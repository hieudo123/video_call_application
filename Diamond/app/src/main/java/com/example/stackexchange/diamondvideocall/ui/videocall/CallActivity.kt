package com.example.stackexchange.diamondvideocall.ui.videocall

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.example.stackexchange.diamondvideocall.R
import com.example.stackexchange.diamondvideocall.interfaces.ConversationFragmentCallback
import com.example.stackexchange.diamondvideocall.interfaces.IncomeCallFragmentCallbackListener
import com.example.stackexchange.diamondvideocall.others.Constant.EXTRA_IS_INCOMING_CALL
import com.example.stackexchange.diamondvideocall.services.CallService
import com.example.stackexchange.diamondvideocall.ui.base.BaseActivity
import com.example.stackexchange.diamondvideocall.ui.recivecall.IncomeFragment
import com.quickblox.chat.QBChatService
import com.quickblox.videochat.webrtc.QBRTCSession
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientSessionCallbacks

class CallActivity : BaseActivity(),IncomeCallFragmentCallbackListener,QBRTCClientSessionCallbacks,ConversationFragmentCallback {
    override fun onHangUpCurrentSession() {
        hangUpCurrentSession()
    }
    private lateinit var callServiceConnection: ServiceConnection
    private lateinit var callService: CallService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call)
    }
    companion object {
        fun start(context: Context, isIncomingCall: Boolean) {
            val intent = Intent(context, CallActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra(EXTRA_IS_INCOMING_CALL, isIncomingCall)
            context.startActivity(intent)
            CallService.start(context)
        }
    }
    fun initScreen(){
        startIncomeFragment()
    }
    fun startIncomeFragment(){
        addFragment(R.id.frmContainer,IncomeFragment(),false)
    }
    private fun startVideoCallFragment() {
        addFragment(R.id.frmContainer,VideoCallFragment(),false)
    }
    fun bindCallService(){
        callServiceConnection = object  : ServiceConnection{
            override fun onServiceDisconnected(name: ComponentName?) {

            }

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val binder = service as CallService.CallServiceBinder
                callService = binder.getService()
                if(callService.currentSessionExist()){
                    if (QBChatService.getInstance().isLoggedIn) {
                        initScreen()
                    }
                }
            }
        }
        Intent(this, CallService::class.java).also { intent ->
            bindService(intent, callServiceConnection, Context.BIND_AUTO_CREATE)
        }
    }
    override fun onResume() {
        super.onResume()
        bindCallService()
    }

    override fun onPause() {
        unbindService(callServiceConnection)
        super.onPause()
    }

    override fun finish() {
        CallService.stop(this)
        super.finish()
    }
    override fun onAcceptCurrentSession() {
        Log.e("ACCEPT","Accepted Call")
        callService.acceptCall(HashMap())
        startVideoCallFragment()
    }
    override fun onRejectCurrentSession() {
        Log.e("REJECT","Rejected Call")
        callService.rejectCurrentSession(HashMap())
    }
    private fun hangUpCurrentSession() {
        callService.hangUpCurrentSession(java.util.HashMap())
        finish()
    }
    ////////////////////////////// QBRTCClientSessionCallbacks //////////////////////////////
    override fun onUserNotAnswer(p0: QBRTCSession?, p1: Int?) {

    }

    override fun onSessionStartClose(p0: QBRTCSession?) {
        callService.removeCurrentSession()
    }

    override fun onReceiveHangUpFromUser(p0: QBRTCSession?, p1: Int?, p2: MutableMap<String, String>?) {
        Log.e("HANGUP","HangUp Call")
        hangUpCurrentSession()
    }

    override fun onCallAcceptByUser(p0: QBRTCSession?, p1: Int?, p2: MutableMap<String, String>?) {

    }

    override fun onReceiveNewSession(p0: QBRTCSession?) {

    }

    override fun onUserNoActions(p0: QBRTCSession?, p1: Int?) {

    }

    override fun onSessionClosed(p0: QBRTCSession?) {
        callService.stopForeground(true)
        finish()
    }

    override fun onCallRejectByUser(p0: QBRTCSession?, p1: Int?, p2: MutableMap<String, String>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}