package com.example.stackexchange.diamondvideocall.ui.recivecall

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.example.stackexchange.diamondvideocall.R
import com.example.stackexchange.diamondvideocall.interfaces.IncomeCallFragmentCallbackListener
import com.example.stackexchange.diamondvideocall.ui.base.BaseFragment
import java.lang.ClassCastException

class IncomeFragment:BaseFragment(),View.OnClickListener {
    private lateinit var incomeCallFragmentCallbackListener: IncomeCallFragmentCallbackListener
    lateinit var acceptCallButton : ImageButton
    lateinit var rejectCallButton: ImageButton
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.fragment_income_call,container,false)
        init(view)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    fun init(view : View){
        acceptCallButton = view.findViewById(R.id.image_button_accept_call)
        rejectCallButton = view.findViewById(R.id.image_button_reject_call)

        initOnclickListener()
    }
    fun initOnclickListener(){
        acceptCallButton.setOnClickListener(this)
        rejectCallButton.setOnClickListener(this)
    }
    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        try {
            incomeCallFragmentCallbackListener =activity as IncomeCallFragmentCallbackListener
        }catch (e:ClassCastException){
            throw ClassCastException(activity?.toString() + " must implement OnCallEventsController")
        }
    }
    override fun onClick(view: View?) {
        when (view!!.id){
            R.id.image_button_accept_call ->accept()
            R.id.image_button_reject_call->reject()
        }
    }
    private fun accept() {
        incomeCallFragmentCallbackListener.onAcceptCurrentSession()

    }

    private fun reject() {
        incomeCallFragmentCallbackListener.onRejectCurrentSession()
    }
}