package com.example.userapp.ui.signup

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.example.userapp.R

class SignUpWaitDialog constructor(context: Context) : Dialog(context){
    init {
        setCanceledOnTouchOutside(false)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.fragment_signup_waitdialog)
    }
}