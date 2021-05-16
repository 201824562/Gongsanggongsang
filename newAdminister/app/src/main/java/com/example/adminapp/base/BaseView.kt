package com.example.adminapp.base

import androidx.annotation.StringRes

interface BaseView {
    fun snackbarObserving()

    fun showSnackbar(message : String)

    fun showToast(message: String)

    fun showToast(@StringRes stringRes: Int)    //Resource Type Anotations == 전달되는 id가 이 type이 아닐경우 error를 표시한다.

    //fun setupKeyboardHide(view: View, activity: Activity?)  //MainActivity에서 네비게이션 이동시 바로 처리.(아마 필요없을듯)

    //fun setToolbarTitle(title : String?)      //MainActivity에서 네비게이션 컴포넌트로 처리한다.(아마 필요없을듯)

    //fun loadingIndicatorObserving()
}