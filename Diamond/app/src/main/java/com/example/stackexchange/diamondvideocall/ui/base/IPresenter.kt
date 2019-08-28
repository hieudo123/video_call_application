package com.example.stackexchange.diamondvideocall.ui.base

interface IPresenter<V : MvpView> {
    fun onAttach(mvpView: V)
    fun onDetach()
    fun isViewAttach(): Boolean
}