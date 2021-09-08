package com.example.userapp.ui.main.settings

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.userapp.ui.base.BaseSessionViewModel
import com.example.userapp.data.dto.UserModel
import com.example.userapp.utils.RegularExpressionUtils
import com.example.userapp.utils.SingleLiveEvent
import io.reactivex.Single
import io.reactivex.functions.BiFunction

class SettingsViewModel(application: Application) : BaseSessionViewModel(application) {

    private val _onSuccessDeleteUserInfoFromServer = SingleLiveEvent<Any>()
    val onSuccessDeleteUserInfoFromServer : LiveData<Any> get() = _onSuccessDeleteUserInfoFromServer
    private val _onSuccessDeleteFcmTokenFromServer = SingleLiveEvent<Any>()
    val onSuccessDeleteFcmTokenFromServer : LiveData<Any> get() = _onSuccessDeleteFcmTokenFromServer
    private val _onSuccessDeleteUserInfoFromApp = SingleLiveEvent<Any>()
    val onSuccessDeleteUserInfoFromApp : LiveData<Any> get() = _onSuccessDeleteUserInfoFromApp
    private val _invalidUserIdEventLiveData = SingleLiveEvent<String>()
    val invalidUserIdEventLiveData: LiveData<String> get() = _invalidUserIdEventLiveData
    private val _validUserIdEventLiveData = SingleLiveEvent<Any>()
    val validUserIdEventLiveData: LiveData<Any> get() = _validUserIdEventLiveData
    private val _invalidUserPwdEventLiveData = SingleLiveEvent<String>()
    val invalidUserPwdEventLiveData: LiveData<String> get() = _invalidUserPwdEventLiveData
    private val _validUserPwdEventLiveData = SingleLiveEvent<Any>()
    val validUserPwdEventLiveData: LiveData<Any> get() = _validUserPwdEventLiveData
    private val _invalidUserPwd2EventLiveData = SingleLiveEvent<String>()
    val invalidUserPwd2EventLiveData: LiveData<String> get() = _invalidUserPwd2EventLiveData
    private val _validUserPwd2EventLiveData = SingleLiveEvent<Any>()
    val validUserPwd2EventLiveData: LiveData<Any> get() = _validUserPwd2EventLiveData
    private val _invalidUserNicknameEventLiveData = SingleLiveEvent<String>()
    val invalidUserNicknameEventLiveData: LiveData<String> get() = _invalidUserNicknameEventLiveData
    private val _validUserNicknameEventLiveData = SingleLiveEvent<Any>()
    val validUserNicknameEventLiveData: LiveData<Any> get() = _validUserNicknameEventLiveData
    private val _checkNicknameEventLiveData = SingleLiveEvent<Boolean>()
    val checkNicknameEventLiveData: LiveData<Boolean> get() = _checkNicknameEventLiveData
    private val _checkIdEventLiveData = SingleLiveEvent<Boolean>()
    val checkIdEventLiveData: LiveData<Boolean> get() = _checkIdEventLiveData
    private val _sendChangeInfoEventLiveData = SingleLiveEvent<Any>()
    val sendChangeInfoEventLiveData: LiveData<Any> get() = _sendChangeInfoEventLiveData
    private val _onSuccessChangeInfoEvent = SingleLiveEvent<Any>()
    val onSuccessChangeInfoEvent : LiveData<Any> get() = _onSuccessChangeInfoEvent

    var checkedId : String ?= null
    var checkedNickname : String ?= null
    fun clearCheckedId() {checkedId = null}
    fun clearCheckedNickname() {checkedNickname = null}

    fun deleteUserInfoFromServerDatabase() {
        apiCall(userRepository.withdrawalUserInfo(authToken),
            { _onSuccessDeleteUserInfoFromServer.value = true },
            { showSnackbar("회원탈퇴에 실패했습니다. 네트워크 상태를 체크해주세요.")})
    }
    fun deleteDeviceTokenFromServerDatabase(){
        apiCall(userRepository.deleteUsersDeviceToken(authToken, fcmToken),
            { _onSuccessDeleteFcmTokenFromServer.value = true },
            { showSnackbar("디바이스 로그아웃에 실패했습니다. 네트워크 상태를 체크해주세요.")})
    }
    fun deleteUserInfoFromAppDatabase() {
        val context = getApplication<Application>().applicationContext
        apiCall(userRepository.deleteUserInfo(authToken),
            {   userRepository.removeAgencyInfo(context)
                userRepository.removeFCMToken(context)
                userRepository.removeUserToken(context)
                _onSuccessDeleteUserInfoFromApp.value = true },
            {showSnackbar("로그아웃에 실패했습니다. 잠시후에 시도해주세요.")})
    }
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

    fun checkNicknameFromServer(nickname: String) {
        apiCall(Single.zip(userRepository.checkNicknameFromWaitingUser(nickname),userRepository.checkNicknameFromAllowedUser(nickname),
            BiFunction<Boolean, Boolean, Boolean> { waitingNicknameExist, allowedNicknameExist ->
                if (waitingNicknameExist) return@BiFunction false
                else if (allowedNicknameExist) return@BiFunction false
                return@BiFunction true
            }),
            {   if (it) checkedNickname = nickname
                _checkNicknameEventLiveData.postValue(it)
            })
    }

    fun checkForUserNickname(userNickname: String) : Boolean {
        return if (userNickname.isBlank() || userNickname.isEmpty()) {
            _invalidUserNicknameEventLiveData.postValue("닉네임을 입력해주세요.")
            false
        }else if (userNickname.length < 5) {
            _invalidUserNicknameEventLiveData.postValue("닉네임은 다섯글자 이상이어야 합니다.")
            false
        } else if (!RegularExpressionUtils.validCheck(RegularExpressionUtils.Regex.NICKNAME, userNickname)) {
            _invalidUserNicknameEventLiveData.postValue("닉네임은 10글자 이내의 한글만 입력가능합니다.")
            false
        } else {
            _validUserNicknameEventLiveData.call()
            true
        }
    }

    private fun checkForUserNicknameDuplicated(userNickname: String): Boolean {
        return if (userNickname != checkedNickname) {
            _invalidUserNicknameEventLiveData.postValue("닉네임 중복확인을 해주세요.")
            false
        }else true
    }


    fun checkIdFromServer(userId: String) {
        apiCall(
            Single.zip(userRepository.checkIdFromWaitingUser(userId), userRepository.checkIdFromAllowedUser(userId),
            BiFunction<Boolean, Boolean, Boolean> { waitingIdExist, allowedIdExist ->
                if (waitingIdExist) return@BiFunction false
                else if (allowedIdExist) return@BiFunction false
                return@BiFunction true
            }),
            {   if (it) checkedId = userId
                _checkIdEventLiveData.postValue(it)
            })
    }

    fun checkForUserId(userId: String) : Boolean {
        return if (userId.isBlank() || userId.isEmpty()) {
            _invalidUserIdEventLiveData.postValue("아이디를 입력해주세요.")
            false
        } else if (!RegularExpressionUtils.validCheck(RegularExpressionUtils.Regex.ID, userId)) {
            _invalidUserIdEventLiveData.postValue("아이디는 4~12자의 영문, 숫자만 가능합니다.")
            false
        } else {
            _validUserIdEventLiveData.call()
            true
        }
    }

    private fun checkForUserIdDuplicated(userId: String): Boolean {
        return if (userId!=checkedId) {
            _invalidUserIdEventLiveData.postValue("아이디 중복확인을 해주세요.")
            false
        }else true
    }

    fun checkForSendChangeInfo(type : ChangeInfoType, userId : String, userPwd : String, userPwd2 : String,userNickname : String) {
        when(type){
            ChangeInfoType.NICKNAME -> if(checkForUserNicknameDuplicated(userNickname)) _sendChangeInfoEventLiveData.call()
            ChangeInfoType.ID -> if(checkForUserIdDuplicated(userId)) _sendChangeInfoEventLiveData.call()
            ChangeInfoType.PWD -> if(checkForUserPwd(userPwd, userPwd2)) _sendChangeInfoEventLiveData.call()
        }
    }

    fun sendChangeInfoToServer(type : ChangeInfoType, userId : String, userPwd : String, userNickname : String){
        when(type){
            ChangeInfoType.NICKNAME -> apiCall(userRepository.changeUserInfoNickname(authToken, userNickname), { _onSuccessChangeInfoEvent.call() })
            ChangeInfoType.ID -> apiCall(userRepository.changeUserInfoId(authToken, userId), { _onSuccessChangeInfoEvent.call() })
            ChangeInfoType.PWD -> apiCall(userRepository.changeUserInfoPwd(authToken, userPwd), { _onSuccessChangeInfoEvent.call() })
        }
    }

    fun checkForSendChangePwd(userPwd : String, userPwd2 : String) {
        if(checkForUserPwd(userPwd, userPwd2)) _sendChangeInfoEventLiveData.call()
    }

    fun sendChangePwdToServer(userPwd : String){
        apiCall(userRepository.changeUserInfoPwd(authToken, userPwd), { _onSuccessChangeInfoEvent.call() })
    }


    //----------------------------------------------------------------------------------------------------------------


}