package com.example.adminapp.ui.splash

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.adminapp.ui.base.BaseSessionViewModel
import com.example.adminapp.utils.SingleLiveEvent


class SplashViewModel(application: Application) : BaseSessionViewModel(application) {

    private val _onSuccessGettingToken = SingleLiveEvent<Boolean>()
    val onSuccessGettingToken: LiveData<Boolean> get() = _onSuccessGettingToken

    fun getAdminStatus() {
        apiCall(adminRepository.checkAdminStatus(authToken), {
            if (it) _onSuccessGettingToken.postValue(true)
            else _onSuccessGettingToken.postValue(false)
        })
    }

}