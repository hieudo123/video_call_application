package com.example.stackexchange.diamondvideocall.utils

import com.quickblox.videochat.webrtc.QBRTCSession
import com.quickblox.videochat.webrtc.callbacks.QBRTCSessionEventsCallback

object SessionEventCallBack: QBRTCSessionEventsCallback {
    override fun onUserNotAnswer(p0: QBRTCSession?, p1: Int?) {

    }

    override fun onReceiveHangUpFromUser(p0: QBRTCSession?, p1: Int?, p2: MutableMap<String, String>?) {

    }

    override fun onCallAcceptByUser(p0: QBRTCSession?, p1: Int?, p2: MutableMap<String, String>?) {

    }

    override fun onSessionClosed(p0: QBRTCSession?) {

    }

    override fun onCallRejectByUser(p0: QBRTCSession?, p1: Int?, p2: MutableMap<String, String>?) {

    }

}