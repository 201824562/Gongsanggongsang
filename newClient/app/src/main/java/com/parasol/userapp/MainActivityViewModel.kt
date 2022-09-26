package com.parasol.userapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.parasol.userapp.ui.base.BaseActivityViewModel
import com.parasol.userapp.data.AppDatabase
import com.parasol.userapp.data.dto.UserModel
import com.parasol.userapp.data.repository.UserRepository
import com.parasol.userapp.utils.SingleLiveEvent

open class MainActivityViewModel(application: Application) : BaseActivityViewModel(application) {

    private val userRepository : UserRepository  = UserRepository.getInstance(AppDatabase.getDatabase(application, viewModelScope))

    private val _onSuccessGettingUserInfo = SingleLiveEvent<UserModel>()
    val onSuccessGettingUserInfo : LiveData<UserModel> get() = _onSuccessGettingUserInfo
    private val _onSuccessGettingNullUserInfo = SingleLiveEvent<UserModel>()
    val onSuccessGettingNullUserInfo : LiveData<UserModel> get() = _onSuccessGettingNullUserInfo

    fun getUserInfo()  {
        apiCall(userRepository.getUserInfo(), {
            _onSuccessGettingUserInfo.postValue(it) },
            { _onSuccessGettingNullUserInfo.call() })
    }

}