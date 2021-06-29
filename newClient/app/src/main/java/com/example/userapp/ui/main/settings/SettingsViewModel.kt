package com.example.userapp.ui.main.settings

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.userapp.base.BaseSessionViewModel
import com.example.userapp.utils.SingleLiveEvent

class SettingsViewModel(application: Application) : BaseSessionViewModel(application) {

    private val _onSuccessDeleteUserInfo = SingleLiveEvent<Any>()
    val onSuccessDeleteUserInfo : LiveData<Any> get() = _onSuccessDeleteUserInfo

    fun deleteUserInfo(userId : String) {
        apiCall(userRepository.deleteUserInfo(userId), {_onSuccessDeleteUserInfo.value = true}, {showSnackbar("로그아웃에 실패했습니다. 잠시후에 시도해주세요.")})
    }
}