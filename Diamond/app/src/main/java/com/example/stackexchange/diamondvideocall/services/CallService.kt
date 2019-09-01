package com.example.stackexchange.diamondvideocall.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.stackexchange.diamondvideocall.R
import com.example.stackexchange.diamondvideocall.others.Constant.*
import com.example.stackexchange.diamondvideocall.ui.videocall.CallActivity
import com.example.stackexchange.diamondvideocall.utils.*
import com.quickblox.chat.QBChatService
import com.quickblox.videochat.webrtc.*
import com.quickblox.videochat.webrtc.callbacks.*
import com.quickblox.videochat.webrtc.exception.QBRTCSignalException
import org.jivesoftware.smack.ConnectionListener

class CallService :Service(), QBRTCSignalingCallback {
    private lateinit var appRTCAudioManager: AppRTCAudioManager
    lateinit var notificationManager : NotificationManager
    private var ringtonePlayer: RingtonePlayer? = null
    private val callServiceBinder: CallServiceBinder = CallServiceBinder()
    private lateinit var rtcClient: QBRTCClient
    private var currentSession: QBRTCSession? = null
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, CallService::class.java)
            context.startService(intent)
        }

        fun stop(context: Context) {
            val intent = Intent(context, CallService::class.java)
            context.stopService(intent)
        }
    }
    override fun onBind(intent: Intent?): IBinder? {
        return callServiceBinder
    }

    override fun onCreate() {
        currentSession = WebRtcSessionManager.getCurrentSession()

        ringtonePlayer = RingtonePlayer(this,R.raw.beep)
        iniRTCClient()
        initListeners()
        initAudioManager()
        super.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        removeCurrentSession()
    }


    fun initListeners(){
        addSessionStateListener(SessionConnectCallBack)
        addVideoTrackListener(VideoTrackCallBackListener)
        addSessionEventsListener(SessionEventCallBack)
        addSignalingListener(this)
    }
    fun initAudioManager() {
        appRTCAudioManager = AppRTCAudioManager.create(this)

        appRTCAudioManager.setOnWiredHeadsetStateListener { plugged, hasMicrophone ->

        }

        appRTCAudioManager.setBluetoothAudioDeviceStateListener { connected ->

        }

        appRTCAudioManager.start { selectedAudioDevice, availableAudioDevices ->
            Toast.makeText(this,"Switch Speaker enabled",Toast.LENGTH_SHORT).show()
        }
    }

    fun removeCurrentSession() {
        removeSessionStateListener(SessionConnectCallBack)
        removeSessionEventsListener(SessionEventCallBack)
        removeVideoTrackListener(VideoTrackCallBackListener)
        removeSignalingListener(this)
        currentSession = null

    }
    fun addConnectionListener(connectionListener: ConnectionListener?) {
        QBChatService.getInstance().addConnectionListener(connectionListener)
    }
    fun addSessionStateListener(callback: QBRTCSessionStateCallback<*>?) {
        currentSession?.addSessionCallbacksListener(callback)
    }

    fun addVideoTrackListener(callback: QBRTCClientVideoTracksCallbacks<QBRTCSession>?) {
        currentSession?.addVideoTrackCallbacksListener(callback)
    }

    fun addSessionEventsListener(callback: QBRTCSessionEventsCallback?) {
        rtcClient.addSessionCallbacksListener(callback)
    }

    fun addSignalingListener(callback: QBRTCSignalingCallback?) {
        currentSession?.addSignalingCallback(callback)
    }

    fun removeSessionStateListener(callback: QBRTCSessionStateCallback<*>?) {
        currentSession?.removeSessionCallbacksListener(callback)
    }
    fun removeVideoTrackListener(callback: QBRTCClientVideoTracksCallbacks<QBRTCSession>?) {
        currentSession?.removeVideoTrackCallbacksListener(callback)
    }
    fun removeSignalingListener(callback: QBRTCSignalingCallback?) {
        currentSession?.removeSignalingCallback(callback)
    }
    fun removeConnectionListener(connectionListener: ConnectionListener?) {
        QBChatService.getInstance().removeConnectionListener(connectionListener)
    }


    fun removeSessionEventsListener(callback: QBRTCSessionEventsCallback?) {
        rtcClient.removeSessionsCallbacksListener(callback)
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = initNotification()
        startForeground(SERVICE_ID,notification)
        return super.onStartCommand(intent, flags, startId)
    }
    inner class CallServiceBinder : Binder() {
        fun getService(): CallService = this@CallService
    }
    fun iniRTCClient(){
        rtcClient = QBRTCClient.getInstance(this)
        QBRTCConfig.setMaxOpponentsCount(MAX_OPPONENTS_COUNT)
        QBRTCConfig.setDebugEnabled(true)
        rtcClient.prepareToProcessCalls()
    }
    private fun initNotification(): Notification {
        val notifyIntent = Intent(this, CallActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val notifyPendingIntent = PendingIntent.getActivity(this, 0, notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationTitle = getString(R.string.notification_title)
        var notificationText = getString(R.string.notification_text, "")


        val bigTextStyle = NotificationCompat.BigTextStyle()
        bigTextStyle.setBigContentTitle(notificationTitle)
        bigTextStyle.bigText(notificationText)

        val channelId: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(CHANNEL_ID, CHANNEL_NAME)
        } else {
            getString(R.string.app_name)
        }

        val builder = NotificationCompat.Builder(this, channelId)
        builder.setStyle(bigTextStyle)
        builder.setContentTitle(notificationTitle)
        builder.setContentText(notificationText)
        builder.setWhen(System.currentTimeMillis())
        builder.setSmallIcon(R.mipmap.ic_launcher)
        val bitmapIcon = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
        builder.setLargeIcon(bitmapIcon)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            builder.priority = NotificationManager.IMPORTANCE_MAX
        } else {
            builder.priority = Notification.PRIORITY_MAX
        }
        builder.apply {
            setContentIntent(notifyPendingIntent)
        }
        return builder.build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE)
        channel.lightColor = getColor(R.color.green)
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(channel)
        return channelId
    }

    fun currentSessionExist(): Boolean {
        return currentSession != null
    }
    //Listener

    //Common methods
    fun acceptCall(userInfo: Map<String, String>) {
        currentSession?.acceptCall(userInfo)
    }
    fun startCall(userInfo: Map<String, String>) {
        currentSession?.startCall(userInfo)
    }
    fun rejectCurrentSession(userInfo: Map<String, String>) {
        currentSession?.rejectCall(userInfo)
    }
    fun hangUpCurrentSession(userInfo: Map<String, String>): Boolean {
        var result = false
        currentSession?.let {
            it.hangUp(userInfo)
            result = true
        }
        return result
    }
    fun setAudioEnabled(enabled: Boolean) {
        currentSession?.mediaStreamManager?.localAudioTrack?.setEnabled(enabled)
    }
    override fun onSuccessSendingPacket(p0: QBSignalingSpec.QBSignalCMD?, p1: Int?) {

    }

    override fun onErrorSendingPacket(p0: QBSignalingSpec.QBSignalCMD?, p1: Int?, p2: QBRTCSignalException?) {

    }

}