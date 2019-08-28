package com.example.stackexchange.diamondvideocall.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.stackexchange.diamondvideocall.R
import com.example.stackexchange.diamondvideocall.interfaces.ConfirmListener
import com.example.stackexchange.diamondvideocall.others.Constant
import com.example.stackexchange.diamondvideocall.services.LoginService
import com.example.stackexchange.diamondvideocall.ui.base.BaseFragment
import com.example.stackexchange.diamondvideocall.ui.main.MainActivity
import com.example.stackexchange.diamondvideocall.utils.AppUtils
import com.example.stackexchange.diamondvideocall.utils.SharedPrefUtils
import com.example.stackexchange.diamondvideocall.utils.WebRtcSessionManager
import com.quickblox.chat.QBChatService
import com.quickblox.users.model.QBUser
import com.quickblox.videochat.webrtc.QBRTCClient
import com.quickblox.videochat.webrtc.QBRTCConfig

class LoginFragment: BaseFragment(),LoginContract.LoginView,View.OnClickListener {
    lateinit var presenterImp :LoginPresenterImp
    lateinit var etUserName: EditText
    lateinit var etPassword :EditText
    private lateinit var chatService: QBChatService
    private lateinit var rtcClient: QBRTCClient
    var qbUser : QBUser? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.fragment_login_screen,container,false)
        presenterImp = LoginPresenterImp()
        presenterImp.onAttach(this)

        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
    }
    fun init(view : View) {
        etPassword = view.findViewById(R.id.et_password)
        etUserName = view.findViewById(R.id.et_userName)
        view.findViewById<Button>(R.id.btn_login).setOnClickListener(this)
        autoLogin()
    }
    override fun loginSuccess(qbUser: QBUser) {
        SharedPrefUtils.setString(this.context,Constant.USER_NAME,qbUser.login)
        SharedPrefUtils.setString(this.context,Constant.USER_PASS,etPassword.text.toString())
        qbUser.password = etPassword.text.toString()
        starLoginService(qbUser)
        changeActivity()
    }
    override fun loginFaile(errorMessage: String) {
        AppUtils.showDialogMessage(this!!.context!!, getString(R.string.unauthorize), errorMessage, null)
    }
    fun changeActivity(){
        val intent : Intent = Intent(activity,MainActivity::class.java)
        startActivity(intent)
        activity!!.finish()
    }
    fun autoLogin(){
        if(SharedPrefUtils.getString(this.context,Constant.USER_NAME).isNotEmpty() && SharedPrefUtils.getString(this.context,Constant.USER_PASS).isNotEmpty()){
            val userName = SharedPrefUtils.getString(this.context,Constant.USER_NAME)
            val password : String = SharedPrefUtils.getString(this.context,Constant.USER_PASS)
            qbUser = QBUser(userName,password)
            qbUser.let {
                starLoginService(it!!)
            }
            changeActivity()
        }
    }
    fun starLoginService(qbUser: QBUser){
        val tempIntent = Intent(this.activity, LoginService::class.java)
        val pendingIntent = this.activity!!.createPendingResult(Constant.EXTRA_LOGIN_RESULT_CODE, tempIntent, 0)
        LoginService.start(this.context!!, qbUser!!,pendingIntent)
    }
    override fun onClick(view: View?) {
        when(view!!.id){
            R.id.btn_login->{
                presenterImp.loginAuth(etUserName.text.toString(),etPassword.text.toString())
            }
        }
    }

}