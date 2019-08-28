package com.example.stackexchange.diamondvideocall.utils

import com.quickblox.videochat.webrtc.BaseSession
import com.quickblox.videochat.webrtc.QBRTCSession
import com.quickblox.videochat.webrtc.callbacks.QBRTCSessionConnectionCallbacks

object SessionConnectCallBack: QBRTCSessionConnectionCallbacks {
    override fun onDisconnectedFromUser(p0: QBRTCSession?, p1: Int?) {

    }

    override fun onConnectedToUser(p0: QBRTCSession?, p1: Int?) {

    }

    override fun onConnectionClosedForUser(p0: QBRTCSession?, p1: Int?) {

    }

    override fun onStartConnectToUser(p0: QBRTCSession?, p1: Int?) {

    }

    override fun onDisconnectedTimeoutFromUser(p0: QBRTCSession?, p1: Int?) {

    }

    override fun onStateChanged(p0: QBRTCSession?, p1: BaseSession.QBRTCSessionState?) {

    }

    override fun onConnectionFailedWithUser(p0: QBRTCSession?, p1: Int?) {

    }
}