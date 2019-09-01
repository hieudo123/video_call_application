package com.example.stackexchange.diamondvideocall.ui.videocall

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.stackexchange.diamondvideocall.R
import com.example.stackexchange.diamondvideocall.ui.base.BaseFragment
import com.example.stackexchange.diamondvideocall.utils.SessionEventCallBack
import com.example.stackexchange.diamondvideocall.utils.VideoTrackCallBackListener
import com.example.stackexchange.diamondvideocall.utils.WebRtcSessionManager
import com.quickblox.videochat.webrtc.QBRTCClient
import com.quickblox.videochat.webrtc.QBRTCSession
import com.quickblox.videochat.webrtc.QBRTCTypes
import com.quickblox.videochat.webrtc.QBSignalingSpec
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientSessionCallbacks
import com.quickblox.videochat.webrtc.callbacks.QBRTCSignalingCallback
import com.quickblox.videochat.webrtc.exception.QBRTCSignalException
import kotlinx.android.synthetic.main.fragment_make_calll.*
import java.util.HashMap

class MakeVideoCallFragment:BaseFragment(),
    View.OnClickListener{



    lateinit var btnStartCall : Button
    lateinit var etUserId : EditText
    var userId =0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.fragment_make_calll,container,false)
        init(view)
        return view
    }
    fun init(view:View){
        view.findViewById<Button>(R.id.btn_startCall).setOnClickListener(this)
        etUserId = view.findViewById(R.id.et_userId)
    }
    override fun onClick(view: View?) {
        when (view!!.id){
            R.id.btn_startCall->{
                startCall(96926738)

            }
        }
    }
    fun startCall(userId: Int){
        val qbConferenceType: QBRTCTypes.QBConferenceType = QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO
        var opponents : ArrayList<Int> = ArrayList()
        opponents.add(userId)

        val userInfo : Map<String,String> = HashMap()
        val qbrtcClient = QBRTCClient.getInstance(this.context)
        val session  : QBRTCSession = qbrtcClient.createNewSessionWithOpponents(opponents,qbConferenceType)
        WebRtcSessionManager.setCurrentSession(session)

        CallActivity.start(this.context!!,false)
    }
}