package com.example.stackexchange.diamondvideocall.ui.base

interface MvpView {
    abstract fun showLoading()

    abstract fun hideLoading()

    abstract fun addFragment(containerViewId:Int,fragment: BaseFragment, isAddToBackStack: Boolean)

    abstract fun replaceFragment(containerViewId:Int,fragment: BaseFragment, isAddToBackStack: Boolean)
}