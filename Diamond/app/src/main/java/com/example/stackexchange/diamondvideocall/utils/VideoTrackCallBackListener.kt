package com.example.stackexchange.diamondvideocall.utils

import com.quickblox.videochat.webrtc.QBRTCSession
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientVideoTracksCallbacks
import com.quickblox.videochat.webrtc.view.QBRTCVideoTrack

object VideoTrackCallBackListener: QBRTCClientVideoTracksCallbacks<QBRTCSession> {
    override fun onLocalVideoTrackReceive(p0: QBRTCSession?, p1: QBRTCVideoTrack?) {

    }

    override fun onRemoteVideoTrackReceive(p0: QBRTCSession?, p1: QBRTCVideoTrack?, p2: Int?) {

    }

}