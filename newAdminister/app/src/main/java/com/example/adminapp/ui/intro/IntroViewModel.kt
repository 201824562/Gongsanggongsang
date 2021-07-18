package com.example.adminapp.ui.intro

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.adminapp.base.BaseSessionViewModel
import com.example.adminapp.data.dto.AdminModel
import com.example.adminapp.data.model.AdminStatus
import com.example.adminapp.data.repository.SignRepository
import com.example.adminapp.utils.SingleLiveEvent


class IntroViewModel (application: Application) : BaseSessionViewModel(application) {

    private val signRepository: SignRepository = SignRepository.getInstance()

    private val _onFailCheckingAdminData = SingleLiveEvent<String>()
    val onFailCheckingAdminData : LiveData<String> get() = _onFailCheckingAdminData
    private val _onSuccessSaveUserInfo = SingleLiveEvent<Any>()
    val onSuccessSaveUserInfo : LiveData<Any> get() = _onSuccessSaveUserInfo
    private val _userStatusEvent = SingleLiveEvent<AdminStatus>()
    val adminStatusEvent : LiveData<AdminStatus> get() = _userStatusEvent
    private var adminData : AdminModel?= null


   fun checkForSignInInfo(adminId : String, adminPwd : String) : Boolean {
        return if (adminId.isBlank()||adminId.isEmpty()){
            _onFailCheckingAdminData.postValue("아이디를 입력해주세요.")
            false
        } else if (adminPwd.isBlank() || adminPwd.isEmpty()){
            _onFailCheckingAdminData.postValue("비밀번호를 입력해주세요.")
            false
        } else true
    }

    fun sendSignInInfo(adminId : String, adminPwd : String) {
        apiCall(signRepository.checkingAdminSignIn(adminId, adminPwd), { receivedData ->
            Log.e("checking", "$receivedData")
            var status = AdminStatus.NOT_ADMIN
            if (receivedData.boolean) {
                adminData = receivedData.userdata
                status = AdminStatus.ADMIN
                _userStatusEvent.postValue(status)
            }
            else _userStatusEvent.postValue(status)
        })
    }

    fun saveUserInfo() {
        val context = getApplication<Application>().applicationContext
        adminData?.let {
            apiCall(adminRepository.saveAdminInfo(it),{
                _onSuccessSaveUserInfo.value = true
                adminRepository.saveAgencyInfo(it.agency, context)
                adminRepository.saveAdminToken(it.id, context) } )
        }
    }

}