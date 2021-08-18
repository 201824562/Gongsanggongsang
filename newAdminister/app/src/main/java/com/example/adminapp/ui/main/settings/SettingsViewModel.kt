package com.example.adminapp.ui.main.settings

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.adminapp.base.BaseSessionViewModel
import com.example.adminapp.data.model.AdminModel
import com.example.adminapp.data.model.User
import com.example.adminapp.utils.RegularExpressionUtils
import com.example.adminapp.utils.SingleLiveEvent

class SettingsViewModel(application: Application) : BaseSessionViewModel(application) {

    private val _invalidUserPwdEventLiveData = SingleLiveEvent<String>()
    val invalidUserPwdEventLiveData: LiveData<String> get() = _invalidUserPwdEventLiveData
    private val _validUserPwdEventLiveData = SingleLiveEvent<Any>()
    val validUserPwdEventLiveData: LiveData<Any> get() = _validUserPwdEventLiveData
    private val _invalidUserPwd2EventLiveData = SingleLiveEvent<String>()
    val invalidUserPwd2EventLiveData: LiveData<String> get() = _invalidUserPwd2EventLiveData
    private val _validUserPwd2EventLiveData = SingleLiveEvent<Any>()
    val validUserPwd2EventLiveData: LiveData<Any> get() = _validUserPwd2EventLiveData
    private val _sendChangeInfoEventLiveData = SingleLiveEvent<Any>()
    val sendChangeInfoEventLiveData: LiveData<Any> get() = _sendChangeInfoEventLiveData
    private val _onSuccessChangeInfoEvent = SingleLiveEvent<Any>()
    val onSuccessChangeInfoEvent : LiveData<Any> get() = _onSuccessChangeInfoEvent
    private val _onSuccessDeleteAdminInfo = SingleLiveEvent<Any>()
    val onSuccessDeleteUserInfo : LiveData<Any> get() = _onSuccessDeleteAdminInfo
    private val _onSuccessDeleteUserInfoFromServer = SingleLiveEvent<Any>()
    val onSuccessDeleteUserInfoFromServer : LiveData<Any> get() = _onSuccessDeleteUserInfoFromServer


    private fun checkForUserPwd(userPwd: String, userPwd2: String) : Boolean {
        return if (userPwd.isBlank() || userPwd.isEmpty() ) {
            _invalidUserPwdEventLiveData.postValue("비밀번호를 입력해주세요.")
            _validUserPwd2EventLiveData.call()
            false
        }else if (userPwd.length < 8 || userPwd.length > 22) {
            _invalidUserPwdEventLiveData.postValue("비밀번호는 8~22자로 입력해야 합니다.")
            _validUserPwd2EventLiveData.call()
            false
        }else if (!RegularExpressionUtils.validCheck(RegularExpressionUtils.Regex.PWD, userPwd)) {
            _invalidUserPwdEventLiveData.postValue("비밀번호는 영문 또는 숫자만 가능합니다.")
            _validUserPwd2EventLiveData.call()
            false
        }else if (userPwd2.isBlank() || userPwd2.isEmpty()){
            _invalidUserPwd2EventLiveData.postValue("비밀번호 확인을 입력해주세요.")
            _validUserPwdEventLiveData.call()
            false
        }else if (userPwd != userPwd2){
            _invalidUserPwd2EventLiveData.postValue("비밀번호 확인이 일치하지 않습니다.")
            _validUserPwdEventLiveData.call()
            false
        } else {
            _validUserPwdEventLiveData.call()
            _validUserPwd2EventLiveData.call()
            true
        }
    }

    fun checkForSendChangePwd(userPwd : String, userPwd2 : String) {
        if(checkForUserPwd(userPwd, userPwd2)) _sendChangeInfoEventLiveData.call()
    }

    fun sendChangePwdToServer(userPwd : String){
        apiCall(adminRepository.changeAdminInfoPwd(authToken, userPwd), { _onSuccessChangeInfoEvent.call() })
    }

    fun getSettingsWaitingUserList(): LiveData<List<User>> {
        return adminRepository.getSettingsWaitingUserList()
    }
    fun getSettingsAllowedUserList(): LiveData<List<User>> {
        return adminRepository.getSettingsAllowedUserList()
    }
    fun makeWaitingUserDelete(userdata: User){ apiCall(adminRepository.deleteWaitingUser(userdata)) }

    fun makeWaitingUserAllow(userdata : User){ apiCall(adminRepository.acceptWaitingUser(userdata)) }

    fun makeUserWithdrawal(userdata: User) { apiCall(adminRepository.withdrawalUser(userdata)) }

    fun deleteAdminInfoFromServerDatabase() {
        apiCall(adminRepository.withdrawalAdminInfo(authToken),
            { _onSuccessDeleteUserInfoFromServer.value = true },
            { showSnackbar("회원탈퇴에 실패했습니다. 네트워크 상태를 체크해주세요.")})
    }

    fun deleteAdminInfoFromAppDatabase() {
        val context = getApplication<Application>().applicationContext
        apiCall(adminRepository.deleteAdminInfo(authToken),
            {   adminRepository.removeAgencyInfo(context)
                adminRepository.removeAdminToken(context)
                _onSuccessDeleteAdminInfo.value = true },
            {showSnackbar("로그아웃에 실패했습니다. 잠시후에 시도해주세요.")})
    }

    //--------------------------------------------------------------------------------------------

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