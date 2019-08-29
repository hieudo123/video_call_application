package com.example.stackexchange.diamondvideocall.utils

import com.quickblox.videochat.webrtc.QBRTCSession
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientVideoTracksCallbacks
import com.quickblox.videochat.webrtc.view.QBRTCVideoTrack

object VideoTrackCallBackListener: QBRTCClientVideoTracksCallbacks<QBRTCSession> {
    private var localVideoTrack : QBRTCVideoTrack? =null
    private var remoteVideoTrack : QBRTCVideoTrack? = null

    fun addLocalVideoTrack(videoTrack: QBRTCVideoTrack){
        this.localVideoTrack = videoTrack
    }

    fun addRemoteVideoTrack(videoTrack: QBRTCVideoTrack){
        this.remoteVideoTrack = videoTrack
    }

    fun getLocalVideoTrack(): QBRTCVideoTrack { return this.localVideoTrack!! }

    fun getRemoteVideoTrack():QBRTCVideoTrack { return this.remoteVideoTrack!! }

    override fun onLocalVideoTrackReceive(qbrtcSession: QBRTCSession?, videoTrack: QBRTCVideoTrack?) {
       videoTrack?.let {
           addLocalVideoTrack(it)
       }
    }
    override fun onRemoteVideoTrackReceive(qbrtcSession: QBRTCSession?, videoTrack: QBRTCVideoTrack?, userId: Int?) {
        videoTrack?.let {
            addRemoteVideoTrack(it)
        }
    }

}