package com.example.stackexchange.diamondvideocall.interfaces

import com.quickblox.videochat.webrtc.QBRTCSession
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientVideoTracksCallbacks
import com.quickblox.videochat.webrtc.callbacks.QBRTCSessionEventsCallback
import com.quickblox.videochat.webrtc.callbacks.QBRTCSessionStateCallback
import com.quickblox.videochat.webrtc.view.QBRTCVideoTrack

interface ConversationFragmentCallback {
    //Linteners
    fun onHangUpCurrentSession()


    //Add
    fun addVideoTrackListener(callback: QBRTCClientVideoTracksCallbacks<QBRTCSession>?)
    fun addSessionCallbacksListener(callback: QBRTCSessionStateCallback<*>?)
    fun addSessionEventsListener(callback: QBRTCSessionEventsCallback)

    //Remove
    fun removeSessionEventsListener(eventsCallback: QBRTCSessionEventsCallback?)
    fun removeSessionStateListener(clientConnectionCallbacks: QBRTCSessionStateCallback<*>?)
    fun removeVideoTrackListener(callback: QBRTCClientVideoTracksCallbacks<QBRTCSession>?)


    //Get
    fun getLocalVideoTrack():QBRTCVideoTrack
    fun getRemoteVideoTrack():QBRTCVideoTrack
}