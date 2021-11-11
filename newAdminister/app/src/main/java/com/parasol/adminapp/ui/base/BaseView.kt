package com.parasol.adminapp.ui.base

import androidx.annotation.StringRes

interface BaseView {
    //fun snackbarObserving() -> 액티비티용. (프래그에서는 바로표출만 가능.)

    fun showSnackbar(message : String)

    fun showToast(message: String)

    fun showToast(@StringRes stringRes: Int)    //Resource Type Anotations == 전달되는 id가 이 type이 아닐경우 error를 표시한다.

    //fun setupKeyboardHide(view: View, activity: Activity?)

    //fun setToolbarTitle(title : String?)      //MainActivity에서 네비게이션 컴포넌트로 처리한다.(아마 필요없을듯)

    //fun loadingIndicatorObserving()
}