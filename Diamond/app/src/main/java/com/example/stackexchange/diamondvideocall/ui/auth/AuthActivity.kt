package com.example.stackexchange.diamondvideocall.ui.auth

import android.os.Bundle
import com.example.stackexchange.diamondvideocall.R
import com.example.stackexchange.diamondvideocall.ui.auth.login.LoginFragment
import com.example.stackexchange.diamondvideocall.ui.base.BaseActivity

class AuthActivity: BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_auth)
        super.onCreate(savedInstanceState)
        replaceFragment(R.id.frmContainer, LoginFragment(),false)
    }
}