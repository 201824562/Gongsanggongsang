package com.example.userapp.ui.signin

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.userapp.base.BaseSessionViewModel
import com.example.userapp.data.dto.UserModel
import com.example.userapp.data.model.ReceiverSignIn
import com.example.userapp.data.model.UserStatus
import com.example.userapp.utils.RegularExpressionUtils
import com.example.userapp.utils.SingleLiveEvent
import io.reactivex.Single
import io.reactivex.functions.BiFunction

class SignInViewModel(application: Application) : BaseSessionViewModel(application) {

    private val _invalidUserNameEventLiveData = SingleLiveEvent<String>()
    val invalidUserNameEventLiveData: LiveData<String> get() = _invalidUserNameEventLiveData
    private val _validUserNameEventLiveData = SingleLiveEvent<Any>()
    val validUserNameEventLiveData: LiveData<Any> get() = _validUserNameEventLiveData
    private val _invalidBirthInfoEventLiveData = SingleLiveEvent<String>()
    val invalidUserBirthEventLiveData: LiveData<String> get() = _invalidBirthInfoEventLiveData
    private val _validUserBirthEventLiveData = SingleLiveEvent<Any>()
    val validUserBirthEventLiveData: LiveData<Any> get() = _validUserBirthEventLiveData
    private val _invalidUserSmsEventLiveData = SingleLiveEvent<String>()
    val invalidUserSmsEventLiveData: LiveData<String> get() = _invalidUserSmsEventLiveData
    private val _validUserSmsEventLiveData = SingleLiveEvent<Any>()
    val validUserSmsEventLiveData: LiveData<Any> get() = _validUserSmsEventLiveData
    private val _invalidUserIdEventLiveData = SingleLiveEvent<String>()
    val invalidUserIdEventLiveData: LiveData<String> get() = _invalidUserIdEventLiveData
    private val _validUserIdEventLiveData = SingleLiveEvent<Any>()
    val validUserIdEventLiveData: LiveData<Any> get() = _validUserIdEventLiveData
    private val _findInfoEventLiveData = SingleLiveEvent<Any>()
    val findInfoEventLiveData: LiveData<Any> get() = _findInfoEventLiveData
    private val _onSuccessFindInfo = SingleLiveEvent<String>()
    val onSuccessFindInfo : LiveData<String> get() = _onSuccessFindInfo
    private val _onSuccessSaveUserInfo = SingleLiveEvent<Any>()
    val onSuccessSaveUserInfo : LiveData<Any> get() = _onSuccessSaveUserInfo
    private val _userStatusEvent = SingleLiveEvent<UserStatus>()
    val userStatusEvent : LiveData<UserStatus> get() = _userStatusEvent

    var findInfoType : FindInfoType = FindInfoType.ID
    var findInfoUserName : String = ""
    var findInfoResultData : String = ""
    private var userData : UserModel ?= null

    fun clearTypeData() {
        findInfoType = FindInfoType.ID
        findInfoUserName = ""
        findInfoResultData = ""
    }

    private fun checkForUserName(userName: String) : Boolean{
        return if (userName.isBlank() || userName.isEmpty()) {
            _invalidUserNameEventLiveData.postValue("이름을 입력해주세요.")
            false
        } else if (userName.length < 2) {
            _invalidUserNameEventLiveData.postValue("이름은 2글자 이상이어야 합니다.")
            false
        } else if (!RegularExpressionUtils.validCheck(RegularExpressionUtils.Regex.NAME, userName)) {
            _invalidUserNameEventLiveData.postValue("제대로 된 한글형식으로 입력해주세요.")
            false
        } else {
            _validUserNameEventLiveData.call()
            true}
    }

    private fun checkForUserBirthday(userBirthday: String) : Boolean{
        return if (userBirthday.isBlank() || userBirthday.isEmpty()) {
            _invalidBirthInfoEventLiveData.postValue("생년월일을 입력해주세요.")
            false
        } else if (!RegularExpressionUtils.validCheck(RegularExpressionUtils.Regex.BIRTH, userBirthday)){
            _invalidBirthInfoEventLiveData.postValue("생년월일은 8자리 숫자이어야 합니다.")
            false
        } else {
            _validUserBirthEventLiveData.call()
            true
        }
    }

    private fun checkForUserSmsInfo(usersSmsInfo: String) : Boolean {
        return if (usersSmsInfo.isBlank() || usersSmsInfo.isEmpty()){
            _invalidUserSmsEventLiveData.postValue("전화번호를 입력해주세요.")
            false
        } else if (!RegularExpressionUtils.validCheck(RegularExpressionUtils.Regex.PHONE, usersSmsInfo)){
            _invalidUserSmsEventLiveData.postValue("전화번호를 확인해주세요.")
            false
        } else{
            _validUserSmsEventLiveData.call()
            true
        }
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

    fun checkForFindInfo(type : FindInfoType, userName : String, userBirthday : String, usersSmsInfo : String,
                         userName2 : String, userId : String) {
        when(type) {
            FindInfoType.ID -> {
                val checkName = checkForUserName(userName)
                val checkBirthInfo = checkForUserBirthday(userBirthday)
                val checkSmsInfo = checkForUserSmsInfo(usersSmsInfo)
                if (checkName && checkBirthInfo && checkSmsInfo) _findInfoEventLiveData.call() }
            FindInfoType.PWD -> {
                val checkName = checkForUserName(userName2)
                val checkId = checkForUserId(userId)
                if (checkName && checkId) _findInfoEventLiveData.call() }
        }
    }

    fun sendFindInfo(type : FindInfoType, userName : String, userBirthday : String, usersSmsInfo : String,
                     userName2 : String, userId : String) {
        when(type) {
            FindInfoType.ID -> { apiCall(userRepository.findId(userName, userBirthday, usersSmsInfo),{
                _onSuccessFindInfo.postValue(it) }) }
            FindInfoType.PWD ->  { apiCall(userRepository.findPwd(userName2, userId),{
                _onSuccessFindInfo.postValue(it) }) }
        }
    }

    fun sendSignInInfo(userId : String, userPwd : String) {
        apiCall(
            Single.zip(userRepository.checkingAllowedSignIn(userId, userPwd), userRepository.checkingWaitingSignIn(userId, userPwd),
            BiFunction<ReceiverSignIn, Boolean, UserStatus> { receivedAllowedData, waitingBoolean ->
                var status = UserStatus.NOT_USER
                if (receivedAllowedData.boolean) {
                    userData = receivedAllowedData.userdata
                    status = UserStatus.USER
                }
                if (waitingBoolean) status = UserStatus.WAIT_APPROVE
                return@BiFunction status
            }), {  _userStatusEvent.postValue(it) })
    }

    fun saveUserInfo() {
        val context = getApplication<Application>().applicationContext
        userData?.let {
            apiCall(userRepository.saveUserInfo(it),{
                Log.e("체킹!!유저인포 저장됐는가", "$it")
                userRepository.saveAgencyInfo(it.agency, context)
                userRepository.saveUserToken(it.id, context)
                _onSuccessSaveUserInfo.value = true } )
        }
    }




}