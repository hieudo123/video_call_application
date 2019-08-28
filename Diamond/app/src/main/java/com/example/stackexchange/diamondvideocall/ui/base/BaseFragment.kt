package com.example.stackexchange.diamondvideocall.ui.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

open class BaseFragment : Fragment(),MvpView {
    override fun showLoading() {
        if(activity is BaseActivity){
            (activity as BaseActivity).showLoading()
        }
    }

    override fun hideLoading() {
        if(activity is BaseActivity){
            (activity as BaseActivity).hideLoading()
        }
    }

    override fun addFragment(containerViewId: Int, fragment: BaseFragment, isAddToBackStack: Boolean) {
        if(activity is BaseActivity){
            (activity as BaseActivity).addFragment(containerViewId,fragment,isAddToBackStack)
        }
    }

    override fun replaceFragment(containerViewId: Int, fragment: BaseFragment, isAddToBackStack: Boolean) {
        if(activity is BaseActivity){
            (activity as BaseActivity).replaceFragment(containerViewId,fragment,isAddToBackStack)
        }
    }
    fun clearAllBackStack() {
        if (activity is BaseActivity) {
            (activity as BaseActivity).clearAllBackStack()
        }
    }
}