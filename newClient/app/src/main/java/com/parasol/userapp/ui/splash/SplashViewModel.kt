package com.parasol.userapp.ui.splash

import android.app.Application
import androidx.lifecycle.LiveData
import com.parasol.userapp.ui.base.BaseSessionViewModel
import com.parasol.userapp.utils.SingleLiveEvent

class SplashViewModel(application: Application) : BaseSessionViewModel(application) {

    private val _onSuccessGettingToken = SingleLiveEvent<Boolean>()
    val onSuccessGettingToken: LiveData<Boolean> get() = _onSuccessGettingToken

    fun getUserStatus() {
        apiCall(userRepository.checkUserStatusAllowed(authToken), {
            if (it) _onSuccessGettingToken.postValue(true)
            else _onSuccessGettingToken.postValue(false)
        })
    }

}