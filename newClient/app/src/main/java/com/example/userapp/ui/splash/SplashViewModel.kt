package com.example.userapp.ui.splash

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.userapp.base.BaseSessionViewModel
import com.example.userapp.data.repository.SignRepository
import com.example.userapp.utils.SingleLiveEvent

class SplashViewModel(application: Application) : BaseSessionViewModel(application) {

    private val signRepository: SignRepository = SignRepository.getInstance()

    private val _onSuccessGettingToken = SingleLiveEvent<Boolean>()
    val onSuccessGettingToken: LiveData<Boolean> get() = _onSuccessGettingToken

    fun getUserStatus() {
        apiCall(signRepository.checkUserStatusAllowed(authToken), {
            if (it) _onSuccessGettingToken.postValue(true)
            else _onSuccessGettingToken.postValue(false)
        })
    }

}