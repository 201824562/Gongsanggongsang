package com.parasol.userapp.ui.base

import android.app.Activity
import android.view.View
import androidx.annotation.StringRes

interface BaseView {


    fun showSnackbar(message : String)

    fun showToast(message: String)

    fun showToast(@StringRes stringRes: Int)

}