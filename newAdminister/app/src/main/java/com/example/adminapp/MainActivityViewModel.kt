package com.example.adminapp

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.adminapp.base.BaseActivityViewModel
import com.example.adminapp.data.AppDatabase
import com.example.adminapp.data.model.AdminModel
import com.example.adminapp.data.repository.AdminRepository
import com.example.adminapp.utils.SingleLiveEvent

open class MainActivityViewModel(application: Application) : BaseActivityViewModel(application) {

    private val adminRepository : AdminRepository  = AdminRepository.getInstance(AppDatabase.getDatabase(application, viewModelScope))

    private val _onSuccessGettingAdminInfo = SingleLiveEvent<AdminModel>()
    val onSuccessGettingAdminInfo : LiveData<AdminModel> get() = _onSuccessGettingAdminInfo
    private val _onSuccessGettingNullAdminInfo  = SingleLiveEvent<AdminModel>()
    val onSuccessGettingNullAdminInfo : LiveData<AdminModel> get() = _onSuccessGettingNullAdminInfo

    fun getAdminInfo() {
        apiCall(adminRepository.getAdminInfo(), {
            _onSuccessGettingAdminInfo.postValue(it) },
            { _onSuccessGettingNullAdminInfo.call() })
    }
}