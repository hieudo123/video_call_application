package com.example.stackexchange.diamondvideocall.ui.videocall

import android.app.Activity
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import com.example.stackexchange.diamondvideocall.R
import com.example.stackexchange.diamondvideocall.interfaces.ConversationFragmentCallback
import com.example.stackexchange.diamondvideocall.services.CallService
import com.example.stackexchange.diamondvideocall.ui.base.BaseFragment
import com.example.stackexchange.diamondvideocall.utils.VideoTrackCallBackListener
import com.example.stackexchange.diamondvideocall.utils.WebRtcSessionManager
import com.quickblox.videochat.webrtc.BaseSession
import com.quickblox.videochat.webrtc.QBRTCClient
import com.quickblox.videochat.webrtc.QBRTCSession
import com.quickblox.videochat.webrtc.QBSignalingSpec
import com.quickblox.videochat.webrtc.callbacks.*
import com.quickblox.videochat.webrtc.exception.QBRTCSignalException
import com.quickblox.videochat.webrtc.view.QBRTCSurfaceView
import com.quickblox.videochat.webrtc.view.QBRTCVideoTrack
import org.webrtc.*
import java.io.ByteArrayOutputStream
import java.lang.ref.WeakReference

class VideoCallFragment(var isInComming: Boolean):BaseFragment(), View.OnClickListener,
    QBRTCClientVideoTracksCallbacks<QBRTCSession>,
    QBRTCClientSessionCallbacks,
    QBRTCSessionStateCallback<QBRTCSession>,
    QBRTCSessionConnectionCallbacks,
    QBRTCSignalingCallback, VideoSink {

    private var conversationFragmentCallback: ConversationFragmentCallback?= null
    private var isLocalVideoFullScreen: Boolean = false
    private lateinit var mHandler : Handler
    lateinit var localVideoView: QBRTCSurfaceView
    var remoteFullScreenVideoView: QBRTCSurfaceView? = null
    lateinit var toggle_startChat: ToggleButton
    lateinit var toggle_hangUpCall: ToggleButton
    lateinit var localVideoTrack: QBRTCVideoTrack
    private var isCurrentCameraFront: Boolean = false
    init {
        mHandler =FragmentLifeCycleHandler(this)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.fragment_video_call,container,false)
        init(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        try {
            conversationFragmentCallback = context as ConversationFragmentCallback
        } catch (e: ClassCastException) {
            throw ClassCastException(activity?.toString() + " must implement ConversationFragmentCallback")
        }
    }
    fun init(view : View){
        conversationFragmentCallback!!.setAudioEnabled(true)
        localVideoView = view.findViewById(R.id.localView)
        toggle_startChat = view.findViewById(R.id.toggle_start_chat)
        remoteFullScreenVideoView = view.findViewById(R.id.opponentView)
        toggle_hangUpCall = view.findViewById(R.id.button_hangup_call)

        toggle_startChat.setOnClickListener(this)
        toggle_hangUpCall.setOnClickListener(this)
        localVideoView.setZOrderMediaOverlay(true)
        val eglContext: EglBase = QBRTCClient.getInstance(this.context).getEglContext()
        localVideoView.init(eglContext.eglBaseContext, null)
        localVideoView.release()
        localVideoView.requestLayout()
        initCorrectSizeForLocalView()
        mHandler.postDelayed({
            startToCall()
        }, 800)

    }
    internal class FragmentLifeCycleHandler(fragment: Fragment) : Handler() {

        private val fragmentRef: WeakReference<Fragment> = WeakReference(fragment)
        override fun dispatchMessage(msg: Message) {
            val fragment = fragmentRef.get() ?: return
            if (fragment.isAdded && fragment.activity != null) {
                super.dispatchMessage(msg)
            }
        }
    }
    override fun onFrame(frame: VideoFrame?) {
//            getFrameFormVideoFrame(frame!!)
    }
    fun getFrameFormVideoFrame(frame: VideoFrame){
        var data = frame!!.buffer.toI420().dataU
        var bytes : ByteArray = ByteArray(data.remaining())
        var out : ByteArrayOutputStream = ByteArrayOutputStream()
        var yuvImage : YuvImage = YuvImage(bytes, ImageFormat.NV21,frame.rotatedWidth,frame.rotatedHeight,null)
        yuvImage.compressToJpeg(
            Rect(0,0,frame.rotatedWidth,
                frame.rotatedHeight), 90, out)
        var imageBytes = out.toByteArray()
        var bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size);
        Log.e("AAA","$bitmap")
        out.flush()
        out.close()
    }
    fun initCorrectSizeForLocalView() {
        val params = localVideoView.layoutParams
        val displaymetrics = resources.displayMetrics
        val screenWidthPx = displaymetrics.widthPixels
        Log.d("LCVD", "screenWidthPx $screenWidthPx")
        val width = (screenWidthPx * 0.3).toInt()
        val height = width / 2 * 3
        params?.width = width
        params?.height = height
        localVideoView.layoutParams = params
    }
    fun startToCall(){
        if(isInComming){
            val localVideoTrack = conversationFragmentCallback!!.getLocalVideoTrack()
            onLocalVideoTrackReceive(null,localVideoTrack)
            val remoteVideoTrack : QBRTCVideoTrack = conversationFragmentCallback!!.getRemoteVideoTrack()
            onRemoteVideoTrackReceive(null,remoteVideoTrack,1)
        }
    }
    fun addListeners() {
        conversationFragmentCallback?.addSessionCallbacksListener(this)
        conversationFragmentCallback?.addSessionEventsListener(this)
        if(isInComming)
            conversationFragmentCallback?.addVideoTrackListener(VideoTrackCallBackListener)
        else
            conversationFragmentCallback?.addVideoTrackListener(this)

    }

    fun removeListeners() {
        conversationFragmentCallback?.removeSessionStateListener(this)
        conversationFragmentCallback?.removeSessionEventsListener(this)
        conversationFragmentCallback?.removeVideoTrackListener(this)

    }
    fun fillVideoView(videoView: QBRTCSurfaceView, videoTrack: QBRTCVideoTrack,
                      remoteRenderer: Boolean) {
        videoTrack.removeRenderer(videoTrack.renderer)
        videoTrack.addRenderer(videoView)
        videoTrack.addRenderer(this)
        if (!remoteRenderer) {
            updateVideoView(videoView, isCurrentCameraFront, RendererCommon.ScalingType.SCALE_ASPECT_FILL)
        }

    }

    fun updateVideoView(videoView: SurfaceViewRenderer, mirror: Boolean, scalingType: RendererCommon.ScalingType) {
        videoView.setScalingType(scalingType)
        videoView.setMirror(mirror)
        videoView.requestLayout()
    }
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.button_hangup_call -> {
                CallService.stop(activity as Activity)
                conversationFragmentCallback?.onHangUpCurrentSession()

            }
        }
    }
    override fun onLocalVideoTrackReceive(qbrtcSession: QBRTCSession?, viewTrack : QBRTCVideoTrack?) {
        if (viewTrack != null) {
            isLocalVideoFullScreen = true
            localVideoTrack = viewTrack!!
            localVideoTrack.let { fillVideoView(localVideoView, it, false) }
            isLocalVideoFullScreen = false
        }
    }
    override fun onRemoteVideoTrackReceive(qbrtcSession: QBRTCSession?, viewTrack: QBRTCVideoTrack?, p2: Int?) {
        if (viewTrack != null) {
            fillVideoView(remoteFullScreenVideoView!!, viewTrack!!, true)
            updateVideoView(remoteFullScreenVideoView!!, false, RendererCommon.ScalingType.SCALE_ASPECT_FILL)
        }
    }

    override fun onUserNotAnswer(p0: QBRTCSession?, p1: Int?) {

    }

    override fun onSessionStartClose(p0: QBRTCSession?) {

    }

    override fun onReceiveHangUpFromUser(p0: QBRTCSession?, p1: Int?, p2: MutableMap<String, String>?) {

    }

    override fun onCallAcceptByUser(p0: QBRTCSession?, p1: Int?, p2: MutableMap<String, String>?) {

    }

    override fun onReceiveNewSession(p0: QBRTCSession?) {

    }

    override fun onUserNoActions(p0: QBRTCSession?, p1: Int?) {

    }

    override fun onSessionClosed(p0: QBRTCSession?) {

    }

    override fun onCallRejectByUser(p0: QBRTCSession?, p1: Int?, p2: MutableMap<String, String>?) {

    }

    override fun onDisconnectedFromUser(p0: QBRTCSession?, p1: Int?) {

    }

    override fun onConnectedToUser(qbrtcSession: QBRTCSession?, p1: Int?) {

    }

    override fun onConnectionClosedForUser(p0: QBRTCSession?, p1: Int?) {

    }

    override fun onStateChanged(p0: QBRTCSession?, p1: BaseSession.QBRTCSessionState?) {

    }

    override fun onStartConnectToUser(p0: QBRTCSession?, p1: Int?) {

    }

    override fun onDisconnectedTimeoutFromUser(p0: QBRTCSession?, p1: Int?) {

    }

    override fun onConnectionFailedWithUser(p0: QBRTCSession?, p1: Int?) {

    }

    override fun onSuccessSendingPacket(p0: QBSignalingSpec.QBSignalCMD?, p1: Int?) {

    }

    override fun onErrorSendingPacket(p0: QBSignalingSpec.QBSignalCMD?, p1: Int?, p2: QBRTCSignalException?) {

    }

    override fun onDestroy() {
        super.onDestroy()
//        conversationFragmentCallback?.onHangUpCurrentSession()
    }
    override fun onStart() {
        super.onStart()
        conversationFragmentCallback?.startCall(HashMap())
        addListeners()
    }

    override fun onPause() {
        super.onPause()
        removeListeners()
    }

}