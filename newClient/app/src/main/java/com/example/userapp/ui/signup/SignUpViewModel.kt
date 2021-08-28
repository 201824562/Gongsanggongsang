package com.example.userapp.ui.signup

import android.app.Application
import android.widget.CheckBox
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.userapp.base.BaseSessionViewModel
import com.example.userapp.data.model.Agency
import com.example.userapp.data.model.SignUpInfo
import com.example.userapp.utils.RegularExpressionUtils
import com.example.userapp.utils.SingleLiveEvent
import io.reactivex.Single
import io.reactivex.functions.BiFunction

class SignUpViewModel(application: Application) : BaseSessionViewModel(application) {

    private val _onClickedAllBtn = SingleLiveEvent<Boolean>()
    val onClickedAllBtn : LiveData<Boolean> get() = _onClickedAllBtn
    private val _onClickedFirstBtn = SingleLiveEvent<Boolean>()
    val onClickedFirstBtn : LiveData<Boolean> get() = _onClickedFirstBtn
    private val _onClickedSecondBtn = SingleLiveEvent<Boolean>()
    val onClickedSecondBtn : LiveData<Boolean> get() = _onClickedSecondBtn
    private val _checkPermissionClickedState = SingleLiveEvent<Any>()
    val checkPermissionClickedState : LiveData<Any> get() = _checkPermissionClickedState
    private val _invalidSearchResultEventLiveData = SingleLiveEvent<String>()
    val invalidSearchResultEventLiveData : LiveData<String> get() = _invalidSearchResultEventLiveData
    private val _validSearchResultEventLiveData = SingleLiveEvent<Any>()
    val validSearchResultEventLiveData : LiveData<Any> get() = _validSearchResultEventLiveData
    private val _agencyDataList = MutableLiveData<List<Agency>>()
    val agencyDataList: LiveData<List<Agency>> get() = _agencyDataList
    private val _checkAgencyClickedState = SingleLiveEvent<Any>()
    val checkAgencyClickedState : LiveData<Any> get() = _checkAgencyClickedState
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
    private val _saveSignUpInfoEventLiveData = SingleLiveEvent<Any>()
    val saveSignUpInfoEventLiveData: LiveData<Any> get() = _saveSignUpInfoEventLiveData
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
    private val _checkIdEventLiveData = SingleLiveEvent<Boolean>()
    val checkIdEventLiveData: LiveData<Boolean> get() = _checkIdEventLiveData
    private val _checkNicknameEventLiveData = SingleLiveEvent<Boolean>()
    val checkNicknameEventLiveData: LiveData<Boolean> get() = _checkNicknameEventLiveData
    private val _sendSignUpInfoEventLiveData = SingleLiveEvent<Any>()
    val sendSignUpInfoEventLiveData: LiveData<Any> get() = _sendSignUpInfoEventLiveData
    private val _onSuccessSignUpEvent = SingleLiveEvent<Any>()
    val onSuccessSignUpEvent : LiveData<Any> get() = _onSuccessSignUpEvent

    var clickedAllBtn : Boolean = false
    var clickedFirstBtn : Boolean = false
    var clickedSecondBtn : Boolean = false
    var cameFromPermission : Boolean = true
    var clickedAgencyBtn : Boolean = false
    var selectedAgency : Agency?  = null
    var checkedId : String ?= null
    var checkedNickname : String ?= null
    private var personalSignUpInfo : PersonalSignUpInfo ?= null
    private var signUpInfo : SignUpInfo? =null

    fun clearSecondFragmentVar(){
        clearCheckedId()
        clearCheckedNickname()
        signUpInfo = null
    }

    fun clearCheckedId() {checkedId = null}
    fun clearCheckedNickname() {checkedNickname = null}

    fun changeAllBtnValue() {
        clickedAllBtn = !clickedAllBtn
        clickedFirstBtn = clickedAllBtn
        clickedSecondBtn = clickedAllBtn
        _onClickedAllBtn.value = clickedAllBtn
        _onClickedFirstBtn.value = clickedAllBtn
        _onClickedSecondBtn.value = clickedAllBtn
        observePermissionBtnState()
    }

    fun changeFirstBtnValue() {
        clickedFirstBtn = !clickedFirstBtn
        _onClickedFirstBtn.value = clickedFirstBtn
        observePermissionBtnState()
    }
    fun changeFirstBtnValueTrue() {
        clickedFirstBtn = true
        _onClickedFirstBtn.postValue(clickedFirstBtn)
        observePermissionBtnState()
    }
    fun changeSecondBtnValue() {
        clickedSecondBtn = !clickedSecondBtn
        _onClickedSecondBtn.value = clickedSecondBtn
        observePermissionBtnState()
    }
    fun changeSecondBtnValueTrue() {
        clickedSecondBtn = true
        _onClickedSecondBtn.postValue(clickedSecondBtn)
        observePermissionBtnState()
    }
    fun observePermissionBtnState() {
        _checkPermissionClickedState.call()
    }
    fun checkBtnState() : Boolean {
        return if (clickedFirstBtn && clickedSecondBtn) {
            clickedAllBtn = true
            _onClickedAllBtn.value = clickedAllBtn
            true
        } else {
            clickedAllBtn = false
            _onClickedAllBtn.value = clickedAllBtn
            false
        }
    }

    fun clearAgencyVars(){
        selectedAgency = null
    }

    fun clearAgencyResult(searchClicked : Boolean) {
        clickedAgencyBtn = false
        selectedAgency = null
        _agencyDataList.postValue(listOf())
        if (searchClicked) _invalidSearchResultEventLiveData.postValue("검색어를 입력해주세요.")
    }

    fun loadSearchAgencyResult(keyword : String?, getAll : Boolean) {
        apiCall(userRepository.getSearchAgencyResult(keyword, getAll), {
            _agencyDataList.postValue(it)
            _validSearchResultEventLiveData.call()
        })
    }

    fun observeAgencyBtnState() { _checkAgencyClickedState.call() }

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

    fun checkForSaveSignUpInfo(userName : String, userBirthday : String, usersSmsInfo : String) {
        val checkName = checkForUserName(userName)
        val checkBirthInfo = checkForUserBirthday(userBirthday)
        val checkSmsInfo = checkForUserSmsInfo(usersSmsInfo)
        if (checkName && checkBirthInfo && checkSmsInfo) _saveSignUpInfoEventLiveData.call()
    }


    fun saveSignUpInfo(userName : String, userBirthday : String, usersSmsInfo : String){
        personalSignUpInfo = PersonalSignUpInfo(userName, userBirthday, usersSmsInfo)
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


    fun checkIdFromServer(userId: String) {
        apiCall(Single.zip(userRepository.checkIdFromWaitingUser(userId), userRepository.checkIdFromAllowedUser(userId),
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

    fun checkNicknameFromServer(nickname: String) {
        apiCall(Single.zip(userRepository.checkNicknameFromWaitingUser(nickname), userRepository.checkNicknameFromAllowedUser(nickname),
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

    fun checkForSendSignUpInfo(userId : String, userPwd : String, userPwd2 : String,userNickname : String) {
        val checkId = checkForUserIdDuplicated(userId) // checkForUserId(userId)
        val checkPwd = checkForUserPwd(userPwd, userPwd2)
        val checkNickname = checkForUserNicknameDuplicated(userNickname) //checkForUserNickname(userNickname)
        if (checkId && checkPwd && checkNickname) _sendSignUpInfoEventLiveData.call()
    }


    fun sendSignUpInfoToServer(userId : String, userPwd : String, userNickname : String){
        signUpInfo = SignUpInfo(selectedAgency!!.key, personalSignUpInfo!!.username, personalSignUpInfo!!.userBirthday, personalSignUpInfo!!.usersSmsInfo,
            userId, userPwd, userNickname, false)
        apiCall(userRepository.signUp(signUpInfo!!), { _onSuccessSignUpEvent.call() })
    }

}

data class PersonalSignUpInfo(val username : String, val userBirthday: String, val usersSmsInfo: String)