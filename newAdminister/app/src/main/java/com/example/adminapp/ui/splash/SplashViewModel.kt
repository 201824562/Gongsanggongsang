package com.example.adminapp.ui.splash

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.adminapp.base.BaseSessionViewModel
import com.example.adminapp.data.repository.SignRepository
import com.example.adminapp.utils.SingleLiveEvent


class SplashViewModel(application: Application) : BaseSessionViewModel(application) {

    private val signRepository: SignRepository = SignRepository.getInstance()

    private val _onSuccessGettingToken = SingleLiveEvent<Boolean>()
    val onSuccessGettingToken: LiveData<Boolean> get() = _onSuccessGettingToken

    fun getAdminStatus() {
        apiCall(signRepository.checkAdminStatus(authToken), {
            if (it) _onSuccessGettingToken.postValue(true)
            else _onSuccessGettingToken.postValue(false)
        })
    }

}