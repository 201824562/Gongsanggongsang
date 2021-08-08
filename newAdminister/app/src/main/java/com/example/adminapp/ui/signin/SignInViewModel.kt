package com.example.adminapp.ui.signin

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.adminapp.base.BaseSessionViewModel
import com.example.adminapp.data.model.Admin
import com.example.adminapp.data.model.AdminStatus
import com.example.adminapp.utils.RegularExpressionUtils
import com.example.adminapp.utils.SingleLiveEvent


class SignInViewModel (application: Application) : BaseSessionViewModel(application) {

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
    private val _onFailCheckingAdminData = SingleLiveEvent<String>()
    val onFailCheckingAdminData : LiveData<String> get() = _onFailCheckingAdminData
    private val _onSuccessSaveUserInfo = SingleLiveEvent<Any>()
    val onSuccessSaveUserInfo : LiveData<Any> get() = _onSuccessSaveUserInfo
    private val _userStatusEvent = SingleLiveEvent<AdminStatus>()
    val adminStatusEvent : LiveData<AdminStatus> get() = _userStatusEvent

    var findInfoType : FindInfoType = FindInfoType.ID
    var findInfoUserName : String = ""
    var findInfoResultData : String = ""
    private var adminData : Admin?= null

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
            FindInfoType.ID -> { apiCall(adminRepository.findId(userName, userBirthday, usersSmsInfo),{
                _onSuccessFindInfo.postValue(it) }) }
            FindInfoType.PWD ->  { apiCall(adminRepository.findPwd(userName2, userId),{
                _onSuccessFindInfo.postValue(it) }) }
        }
    }

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
        apiCall(adminRepository.checkingAdminSignIn(adminId, adminPwd), { receivedData ->
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