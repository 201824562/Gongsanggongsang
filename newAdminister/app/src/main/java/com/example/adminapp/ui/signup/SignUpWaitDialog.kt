package com.example.adminapp.ui.signup

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.example.adminapp.R

class SignUpWaitDialog constructor(context: Context) : Dialog(context){
    init {
        setCanceledOnTouchOutside(false)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.fragment_signup_waitdialog)
    }
}