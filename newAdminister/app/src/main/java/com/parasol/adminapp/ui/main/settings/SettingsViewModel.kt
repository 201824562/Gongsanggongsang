package com.parasol.adminapp.ui.main.settings

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.parasol.adminapp.data.model.*
import com.parasol.adminapp.ui.base.BaseSessionViewModel
import com.parasol.adminapp.utils.RegularExpressionUtils
import com.parasol.adminapp.utils.SingleLiveEvent
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime

class SettingsViewModel(application: Application) : BaseSessionViewModel(application) {

    val firestore = FirebaseFirestore.getInstance()
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
    private val _onSuccessDeleteFcmTokenFromServer = SingleLiveEvent<Any>()
    val onSuccessDeleteFcmTokenFromServer : LiveData<Any> get() = _onSuccessDeleteFcmTokenFromServer
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

    fun makeUserWithdrawal(userdata: User) { apiCall(adminRepository.withdrawalUser(agencyInfo, userdata)) }

    fun deleteAdminInfoFromServerDatabase() {
        apiCall(adminRepository.withdrawalAdminInfo(authToken),
            { _onSuccessDeleteUserInfoFromServer.value = true },
            { showSnackbar("회원탈퇴에 실패했습니다. 네트워크 상태를 체크해주세요.")})
    }
    fun deleteDeviceTokenFromServerDatabase(){
        apiCall(adminRepository.deleteAdminDeviceToken(authToken, fcmToken),
            { _onSuccessDeleteFcmTokenFromServer.value = true },
            { showSnackbar("디바이스 로그아웃에 실패했습니다. 네트워크 상태를 체크해주세요.")})
    }

    fun deleteAdminInfoFromAppDatabase() {
        val context = getApplication<Application>().applicationContext
        apiCall(adminRepository.deleteAdminInfo(authToken),
            {   adminRepository.removeAgencyInfo(context)
                adminRepository.removeAdminToken(context)
                adminRepository.removeFCMToken(context)
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun outInitialize(){
        var map1 = mutableMapOf<String, Any>()
        var settingOutInitData = arrayListOf<SettingItem>()
        for(i in 0..143){
            settingOutInitData.add(
                SettingItem(
                    false,
                    SettingData(i/6,i%6*10),
                    i,
                    "Nope"
                )
            )
        }
        Log.e("setting", settingOutInitData.toString())

        firestore.collection("한국장학재단_부산").document("community").collection("OUT_NOW")
            .document("Out").set(
                SettingWeekItem(
                settingOutInitData,
                settingOutInitData,
                settingOutInitData,
                settingOutInitData,
                settingOutInitData,
                settingOutInitData,
                settingOutInitData
            )
            )

        map1["initRefTime"] = LocalDateTime.now().toString()
        firestore.collection("한국장학재단_부산").document("community").collection("OUT_NOW")
            .document("Out").update(map1)
    }

    
}