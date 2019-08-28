package com.example.stackexchange.diamondvideocall.ui.base

import android.app.Application
import com.example.stackexchange.diamondvideocall.others.Constant
import com.example.stackexchange.diamondvideocall.utils.WebRtcSessionManager
import com.quickblox.auth.session.QBSettings
import com.quickblox.chat.QBChatService
import com.quickblox.chat.QBSignaling
import com.quickblox.chat.QBVideoChatWebRTCSignalingManager
import com.quickblox.chat.connections.tcp.QBTcpChatConnectionFabric
import com.quickblox.chat.connections.tcp.QBTcpConfigurationBuilder
import com.quickblox.chat.listeners.QBVideoChatSignalingManagerListener
import com.quickblox.videochat.webrtc.QBRTCClient
import com.quickblox.videochat.webrtc.QBRTCConfig

class MyApplication: Application() {
    private lateinit var rtcClient: QBRTCClient
    private lateinit var chatService: QBChatService
    companion object {
        private lateinit var instance: MyApplication

        @Synchronized
        fun getInstance(): MyApplication = instance
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        init()
    }
    fun init() {
        QBSettings.getInstance()
            .init(this, Constant.APPLICATION_ID, Constant.AUTH_KEY, Constant.AUTH_SECRET)
        QBSettings.getInstance().accountKey = Constant.ACCOUNT_KEY
    }
}