package com.example.adminapp.ui.main.settings

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.adminapp.base.BaseSessionViewModel
import com.example.adminapp.utils.SingleLiveEvent

class SettingsViewModel(application: Application) : BaseSessionViewModel(application) {

    private val _onSuccessDeleteUserInfo = SingleLiveEvent<Any>()
    val onSuccessDeleteUserInfo : LiveData<Any> get() = _onSuccessDeleteUserInfo

    fun deleteUserInfo() {
        val context = getApplication<Application>().applicationContext
        apiCall(adminRepository.deleteAdminInfo(authToken),
            {   adminRepository.removeAgencyInfo(context)
                adminRepository.removeAdminToken(context)
                _onSuccessDeleteUserInfo.value = true },
            {showSnackbar("로그아웃에 실패했습니다. 잠시후에 시도해주세요.")})
    }
}