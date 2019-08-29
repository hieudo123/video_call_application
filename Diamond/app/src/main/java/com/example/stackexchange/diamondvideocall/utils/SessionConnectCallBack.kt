package com.example.stackexchange.diamondvideocall.utils

import com.quickblox.videochat.webrtc.BaseSession
import com.quickblox.videochat.webrtc.QBRTCSession
import com.quickblox.videochat.webrtc.callbacks.QBRTCSessionConnectionCallbacks

object SessionConnectCallBack: QBRTCSessionConnectionCallbacks {
    private  var isCallState = false
    fun setCallMod(isCallState : Boolean){
        this.isCallState = isCallState
    }

    fun isCallMod():Boolean{return this.isCallState}
    override fun onDisconnectedFromUser(p0: QBRTCSession?, p1: Int?) {

    }

    override fun onConnectedToUser(p0: QBRTCSession?, p1: Int?) {
        setCallMod(true)
    }

    override fun onConnectionClosedForUser(p0: QBRTCSession?, p1: Int?) {
        setCallMod(false)
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