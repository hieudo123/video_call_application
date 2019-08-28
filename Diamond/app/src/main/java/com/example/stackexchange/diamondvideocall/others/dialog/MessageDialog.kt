package com.example.stackexchange.diamondvideocall.others.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.example.stackexchange.diamondvideocall.R
import com.example.stackexchange.diamondvideocall.interfaces.ConfirmListener
import com.example.stackexchange.diamondvideocall.interfaces.YesNoListener
import kotlinx.android.synthetic.main.dialog_confirm.*

class MessageDialog: Dialog,View.OnClickListener {
    var title =""
    var content =""
    var isVisibility :Boolean? = null
    var yesNoListener: YesNoListener? = null
    var confirmListener: ConfirmListener? = null
    constructor(context: Context) : super(context) {}
    constructor(context: Context,title: String,content: String,isVisibility: Boolean,yesNoListener: YesNoListener?):super(context){
        this.title =title
        this.content = content
        this.isVisibility = isVisibility
        this.yesNoListener = yesNoListener
    }
    constructor(context: Context,title: String,content: String,confirmListener: ConfirmListener?):super(context){
        this.title = title
        this.content = content
        this.confirmListener = confirmListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (window != null) {
            window!!.setDimAmount(0.3f)
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        setContentView(R.layout.dialog_confirm)
        init()
    }
    fun init(){
        dialogConfirm_tvOK.setOnClickListener(this)
        dialogConfirm_tvContent.text = content
    }
    override fun onClick(view: View?) {
        when(view!!.id){
            R.id.dialogConfirm_tvOK->{
                yesNoListener?.onYesListener()
            }
            R.id.dialogConfirm_flCancel->{
                if(confirmListener != null)
                    confirmListener!!.onConfirmClicked()
                else if(yesNoListener != null){
                    yesNoListener!!.onNoListener()
                }
            }
        }
        dismiss()
    }

}