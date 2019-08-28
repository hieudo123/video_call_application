package com.example.stackexchange.diamondvideocall.ui.base

open class BasePresenter<V : MvpView>:IPresenter<V> {
    private var view :V? = null
    override fun onAttach(mvpView: V) {
        this.view = mvpView
    }

    override fun onDetach() {
        this.view = null
    }

    override fun isViewAttach(): Boolean {
        return  view != null
    }

    fun getView():V?{
        return  view
    }
}