package com.example.stackexchange.diamondvideocall.utils

import android.content.Context
import com.example.stackexchange.diamondvideocall.interfaces.ConfirmListener
import com.example.stackexchange.diamondvideocall.others.dialog.MessageDialog

class AppUtils {
    companion object{
        fun showDialogMessage(context: Context,title: String,content: String,listener: ConfirmListener?){
            val messageDialog = MessageDialog(context, title, content, listener)
            messageDialog.show()
        }
    }
}