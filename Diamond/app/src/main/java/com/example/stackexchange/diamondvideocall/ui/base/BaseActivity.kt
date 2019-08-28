package com.example.stackexchange.diamondvideocall.ui.base

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.stackexchange.diamondvideocall.R
import com.example.stackexchange.diamondvideocall.others.dialog.ProgressDialog
import com.example.stackexchange.diamondvideocall.ui.main.MainActivity
import com.example.stackexchange.diamondvideocall.utils.WebRtcSessionManager
import com.quickblox.chat.QBChatService
import com.quickblox.videochat.webrtc.QBRTCClient
import com.quickblox.videochat.webrtc.QBRTCConfig

open class BaseActivity :AppCompatActivity(),MvpView {
    var progressDialog : ProgressDialog? = null

    override fun showLoading() {
        if(progressDialog != null)
            progressDialog!!.show()
        else{
            progressDialog = ProgressDialog(this)
            progressDialog!!.show()
        }
    }

    override fun hideLoading() {
        progressDialog?.dismiss()
    }

    override fun addFragment(containerViewId:Int,fragment: BaseFragment, isAddToBackStack: Boolean) {
        addReplaceFragment(containerViewId,fragment,false,isAddToBackStack)
    }

    override fun replaceFragment(containerViewId:Int,fragment: BaseFragment, isAddToBackStack: Boolean) {
        addReplaceFragment(containerViewId,fragment,true,isAddToBackStack)
    }
    private fun addReplaceFragment(containerViewId:Int,fragment: BaseFragment,isReplace:Boolean, isAddToBackStack: Boolean){
        val fm :FragmentManager = supportFragmentManager
        if(fm != null && fragment != null){
            val fragmentTransaction : FragmentTransaction = fm.beginTransaction()
            if(isReplace)
                fragmentTransaction.replace(
                    if (containerViewId !== 0) containerViewId else if (this is MainActivity) R.id.frmContainer else R.id.frmContainer,
                    fragment
                )
            else{
                val currentFragment =
                    supportFragmentManager.findFragmentById(if (containerViewId !== 0) containerViewId else if (this is MainActivity) R.id.frmContainer else R.id.frmContainer)
                if (currentFragment != null) {
                    fragmentTransaction.hide(currentFragment)
                }
                fragmentTransaction.add(
                    if (containerViewId !== 0) containerViewId else if (this is MainActivity) R.id.frmContainer else R.id.frmContainer,
                    fragment,
                    fragment.javaClass.simpleName
                )
            }
            if(isAddToBackStack){
                fragmentTransaction.addToBackStack(fragment.javaClass.simpleName)
            }
            fragmentTransaction.commit()
        }
    }
    fun clearAllBackStack() {
        val fm = supportFragmentManager
        val count = fm.backStackEntryCount
        for (i in 0 until count) {
            fm.popBackStack()
        }
    }


}