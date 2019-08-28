package com.example.stackexchange.diamondvideocall.utils

import android.util.Log
import com.example.stackexchange.diamondvideocall.ui.base.MyApplication
import com.example.stackexchange.diamondvideocall.ui.videocall.CallActivity
import com.quickblox.videochat.webrtc.QBRTCSession
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientSessionCallbacksImpl

object WebRtcSessionManager : QBRTCClientSessionCallbacksImpl() {
    private val TAG = WebRtcSessionManager::class.java.simpleName

    private var currentSession: QBRTCSession? = null

    fun getCurrentSession(): QBRTCSession? {
        return currentSession
    }

    fun setCurrentSession(qbCurrentSession: QBRTCSession?) {
        currentSession = qbCurrentSession
    }

    override fun onReceiveNewSession(session: QBRTCSession) {
        Log.d("AAA", "onReceiveNewSession to WebRtcSessionManager")
        if (currentSession == null) {
            setCurrentSession(session)
            CallActivity.start(MyApplication.getInstance(), true)
        }
    }

    override fun onSessionClosed(session: QBRTCSession?) {
        Log.d(TAG, "onSessionClosed WebRtcSessionManager")

        if (session == getCurrentSession()) {
            setCurrentSession(null)
        }
    }
}