package com.example.stackexchange.diamondvideocall.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.stackexchange.diamondvideocall.R
import com.example.stackexchange.diamondvideocall.others.Constant
import com.example.stackexchange.diamondvideocall.services.CallService
import com.example.stackexchange.diamondvideocall.services.LoginService
import com.example.stackexchange.diamondvideocall.ui.base.BaseActivity
import com.example.stackexchange.diamondvideocall.ui.videocall.MakeVideoCallFragment
import com.example.stackexchange.diamondvideocall.utils.SharedPrefUtils
import com.quickblox.users.model.QBUser

class MainActivity : BaseActivity() {
    var qbUser : QBUser? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(R.id.frmContainer,MakeVideoCallFragment(),false)
    }
}
