package com.example.stackexchange.diamondvideocall.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.stackexchange.diamondvideocall.R
import com.example.stackexchange.diamondvideocall.others.Constant
import com.example.stackexchange.diamondvideocall.services.CallService
import com.example.stackexchange.diamondvideocall.services.LoginService
import com.example.stackexchange.diamondvideocall.ui.base.BaseActivity
import com.example.stackexchange.diamondvideocall.utils.SharedPrefUtils
import com.quickblox.users.model.QBUser

class MainActivity : BaseActivity() {
    var qbUser : QBUser? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun startLoginService(){
        val tempIntent = Intent(this, LoginService::class.java)
        val pendingIntent = this.createPendingResult(Constant.EXTRA_LOGIN_RESULT_CODE, tempIntent, 0)
        qbUser.let {
            Log.e("HHH","${it!!.login}")
            LoginService.start(this, it!!,pendingIntent)

        }

    }
    fun getUser(){
        if(SharedPrefUtils.getString(this, Constant.USER_NAME).isNotEmpty() && SharedPrefUtils.getString(this, Constant.USER_PASS).isNotEmpty()){
            val userName = SharedPrefUtils.getString(this, Constant.USER_NAME)
            val password : String = SharedPrefUtils.getString(this, Constant.USER_PASS)
            qbUser = QBUser(userName,password)
        }
    }
}
