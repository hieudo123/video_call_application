package com.example.stackexchange.diamondvideocall.services

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.example.stackexchange.diamondvideocall.others.Constant
import com.example.stackexchange.diamondvideocall.ui.auth.login.LoginContract
import com.example.stackexchange.diamondvideocall.ui.auth.login.LoginPresenterImp
import com.example.stackexchange.diamondvideocall.ui.base.BaseFragment
import com.example.stackexchange.diamondvideocall.utils.SharedPrefUtils
import com.example.stackexchange.diamondvideocall.utils.WebRtcSessionManager
import com.quickblox.auth.QBAuth
import com.quickblox.auth.session.QBSession

import com.quickblox.chat.QBChatService
import com.quickblox.chat.QBWebRTCSignaling
import com.quickblox.chat.connections.tcp.QBTcpChatConnectionFabric
import com.quickblox.chat.connections.tcp.QBTcpConfigurationBuilder
import com.quickblox.core.QBEntityCallback
import com.quickblox.core.exception.QBResponseException

import com.quickblox.users.model.QBUser
import com.quickblox.videochat.webrtc.QBRTCClient
import com.quickblox.videochat.webrtc.QBRTCConfig


class LoginService: Service(){
    private lateinit var chatService: QBChatService
    private lateinit var rtcClient: QBRTCClient
    private var currentUser : QBUser? = null
    private var pendingIntent: PendingIntent? = null
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    companion object {
        fun start(context: Context, qbUser: QBUser, pendingIntent: PendingIntent? = null) {
            val intent = Intent(context, LoginService::class.java)
            intent.putExtra(Constant.EXTRA_COMMAND_TO_SERVICE, Constant.COMMAND_LOGIN)
            intent.putExtra(Constant.EXTRA_QB_USER, qbUser)
            intent.putExtra(Constant.EXTRA_PENDING_INTENT, pendingIntent)
            context.startService(intent)
        }

        fun stop(context: Context) {
            val intent = Intent(context, LoginService::class.java)
            context.stopService(intent)
        }

        fun logout(context: Context) {
            val intent = Intent(context, LoginService::class.java)
            intent.putExtra(Constant.EXTRA_COMMAND_TO_SERVICE, Constant.COMMAND_LOGOUT)
            context.startService(intent)
        }
    }
    override fun onCreate() {
        super.onCreate()
        createChatService()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        parseIntentExtras(intent)
        startLoginToChat()
        return Service.START_REDELIVER_INTENT
    }
    private fun startLoginToChat() {
            currentUser?.let {
                Log.d("UUU", "${it.login}")
                loginToChat(currentUser!!)
            }
    }
    private fun parseIntentExtras(intent: Intent?) {
        intent?.extras?.let {
            pendingIntent = intent.getParcelableExtra(Constant.EXTRA_PENDING_INTENT)
            val currentUser: QBUser? = intent.getSerializableExtra(Constant.EXTRA_QB_USER) as QBUser?
            currentUser?.let {
                this.currentUser = currentUser
            }
        }
    }
    private fun loginToChat(qbUser: QBUser){
        QBAuth.createSession(qbUser).performAsync(object:QBEntityCallback<QBSession>{
            override fun onSuccess(p0: QBSession?, p1: Bundle?) {

                Log.e("HHH","isLogined ${p0?.token} ")
                qbUser.id = p0!!.userId
                chatService.login(qbUser,object :QBEntityCallback<QBUser>{
                    override fun onSuccess(p0: QBUser?, p1: Bundle?) {
                        Log.e("HHH","login onSuccess")
                        initQBRTCClient()
                    }
                    override fun onError(p0: QBResponseException?) {

                    }
                })
            }

            override fun onError(p0: QBResponseException?) {

            }

        })
    }
    private fun createChatService() {
        val configurationBuilder = QBTcpConfigurationBuilder()
        configurationBuilder.socketTimeout = 0
        QBChatService.setConnectionFabric(QBTcpChatConnectionFabric(configurationBuilder))
        QBChatService.setDebugEnabled(true)
        chatService = QBChatService.getInstance()
    }
    private fun initQBRTCClient() {
        rtcClient = QBRTCClient.getInstance(applicationContext)
        chatService.videoChatWebRTCSignalingManager.addSignalingManagerListener { qbSignaling, p1 ->
            rtcClient.addSignaling(qbSignaling as QBWebRTCSignaling) }
        QBRTCConfig.setDebugEnabled(true)
        rtcClient.addSessionCallbacksListener(WebRtcSessionManager)
        rtcClient.prepareToProcessCalls()
    }
}