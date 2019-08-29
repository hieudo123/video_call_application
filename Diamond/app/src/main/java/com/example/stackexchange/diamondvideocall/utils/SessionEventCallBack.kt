package com.example.stackexchange.diamondvideocall.utils

import android.util.Log
import com.example.stackexchange.diamondvideocall.services.CallService
import com.quickblox.videochat.webrtc.QBRTCSession
import com.quickblox.videochat.webrtc.callbacks.QBRTCSessionEventsCallback

object SessionEventCallBack: QBRTCSessionEventsCallback {
    val callService : CallService = CallService()
    override fun onUserNotAnswer(p0: QBRTCSession?, p1: Int?) {

    }

    override fun onReceiveHangUpFromUser(p0: QBRTCSession?, p1: Int?, userInfo: MutableMap<String, String>?) {
        Log.e("HANGUP","HangUp Call")
        userInfo?.let { callService.hangUpCurrentSession(it) }
    }

    override fun onCallAcceptByUser(p0: QBRTCSession?, p1: Int?, p2: MutableMap<String, String>?) {

    }

    override fun onSessionClosed(p0: QBRTCSession?) {

    }

    override fun onCallRejectByUser(p0: QBRTCSession?, p1: Int?, p2: MutableMap<String, String>?) {

    }

}