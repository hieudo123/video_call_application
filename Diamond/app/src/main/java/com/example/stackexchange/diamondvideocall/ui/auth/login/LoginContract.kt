package com.example.stackexchange.diamondvideocall.ui.auth.login

import com.example.stackexchange.diamondvideocall.ui.base.MvpView
import com.quickblox.users.model.QBUser

interface LoginContract {
    interface LoginView :MvpView{
        fun loginSuccess(qbUser: QBUser)
        fun loginFaile(errorMessage :String)
    }
    interface LoginPresenter{
        fun loginAuth(userName: String ,password: String)
        fun getUserById(id:String,password: String)
    }
}