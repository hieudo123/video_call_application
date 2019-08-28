package com.example.stackexchange.diamondvideocall.ui.auth.login

import android.os.Bundle
import android.util.Log
import com.example.stackexchange.diamondvideocall.ui.base.BasePresenter
import com.quickblox.auth.session.QBSession

import com.quickblox.chat.QBChatService
import com.quickblox.core.QBEntityCallback
import com.quickblox.core.exception.QBResponseException
import com.quickblox.users.QBUsers
import com.quickblox.users.model.QBUser

class LoginPresenterImp: BasePresenter<LoginContract.LoginView>(),LoginContract.LoginPresenter {
    override fun loginAuth(userName: String, password: String) {
        getView()!!.showLoading()
        val qbUser : QBUser = QBUser(userName,password)
        QBUsers.signIn(qbUser).performAsync(object:  QBEntityCallback<QBUser> {
            override fun onSuccess(user: QBUser?, bundle: Bundle?) {
                Log.e("LOGE","${user!!.id}")
                getView()!!.loginSuccess(user!!)
                getView()!!.hideLoading()

            }

            override fun onError(e: QBResponseException?) {
                getView()!!.loginFaile(e!!.message!!)
                getView()!!.hideLoading()
            }
        })

    }

    override fun getUserById(id: String, password: String) {

    }
}