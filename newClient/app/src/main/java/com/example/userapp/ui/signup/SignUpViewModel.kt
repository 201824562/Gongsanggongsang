package com.example.userapp.ui.signup

import androidx.lifecycle.LiveData
import com.example.userapp.base.BaseViewModel
import com.example.userapp.data.model.SignUpInfo
import com.example.userapp.data.repository.SignRepository
import com.example.userapp.utils.RegularExpressionUtil
import com.example.userapp.utils.SingleLiveEvent

class SignUpViewModel() : BaseViewModel() {

    private val signRepository: SignRepository = SignRepository.getInstance()


    //[SignUpPermissionFragment]--------------------------------------------------------------------------------------------

    //SignUpPermissionFragment 변수들
    private val _onClickedAllBtn = SingleLiveEvent<Boolean>()
    val onClickedAllBtn : LiveData<Boolean> get() = _onClickedAllBtn
    private val _onClickedFirstBtn = SingleLiveEvent<Boolean>()
    val onClickedFirstBtn : LiveData<Boolean> get() = _onClickedFirstBtn
    private val _onClickedSecondBtn = SingleLiveEvent<Boolean>()
    val onClickedSecondBtn : LiveData<Boolean> get() = _onClickedSecondBtn
    private val _checkClickedState = SingleLiveEvent<Any>()
    val checkClickedState : LiveData<Any> get() = _checkClickedState
    var clickedAllBtn : Boolean = false
    var clickedFirstBtn : Boolean = false
    var clickedSecondBtn : Boolean = false


    fun changeAllBtnValue() {
        clickedAllBtn = !clickedAllBtn
        clickedFirstBtn = clickedAllBtn
        clickedSecondBtn = clickedAllBtn
        _onClickedAllBtn.value = clickedAllBtn
        _onClickedFirstBtn.value = clickedAllBtn
        _onClickedSecondBtn.value = clickedAllBtn
        observeBtnState()
    }

    fun changeFirstBtnValue() {
        clickedFirstBtn = !clickedFirstBtn
        _onClickedFirstBtn.value = clickedFirstBtn
        observeBtnState()
    }
    fun changeFirstBtnValueTrue() {
        clickedFirstBtn = true
        _onClickedFirstBtn.postValue(clickedFirstBtn)
    }
    fun changeSecondBtnValue() {
        clickedSecondBtn = !clickedSecondBtn
        _onClickedSecondBtn.value = clickedSecondBtn
        observeBtnState()
    }
    fun changeSecondBtnValueTrue() {
        clickedSecondBtn = true
        _onClickedSecondBtn.postValue(clickedSecondBtn)
    }
    fun observeBtnState() {
        _checkClickedState.call()
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

    //[SignUpFirstFragment]--------------------------------------------------------------------------------------------

    //SignUpFirstFragment 변수들
    private val _invalidUserNameEventLiveData = SingleLiveEvent<String>()
    val invalidUserNameEventLiveData: LiveData<String> get() = _invalidUserNameEventLiveData
    private val _validUserNameEventLiveData = SingleLiveEvent<Any>()
    val validUserNameEventLiveData: LiveData<Any> get() = _validUserNameEventLiveData
    private val _invalidBirthInfoEventLiveData = SingleLiveEvent<String>()
    val invalidBirthInfoEventLiveData: LiveData<String> get() = _invalidBirthInfoEventLiveData
    private val _validBirthInfoEventLiveData = SingleLiveEvent<Any>()
    val validBirthInfoEventLiveData: LiveData<Any> get() = _validBirthInfoEventLiveData
    private val _invalidSmsInfoEventLiveData = SingleLiveEvent<String>()
    val invalidSmsInfoEventLiveData: LiveData<String> get() = _invalidSmsInfoEventLiveData
    private val _validSmsInfoEventLiveData = SingleLiveEvent<Any>()
    val validSmsInfoEventLiveData: LiveData<Any> get() = _validSmsInfoEventLiveData
    private val _saveSignUpInfoEventLiveData = SingleLiveEvent<Any>()
    val saveSignUpInfoEventLiveData: LiveData<Any> get() = _saveSignUpInfoEventLiveData

    private var personalSignUpInfo : PersonalSignUpInfo ?= null


    private fun checkForUserName(userName: String) : Boolean{
        return if (userName.isBlank() || userName.isEmpty()) {
            _invalidUserIdEventLiveData.postValue("이름을 입력해주세요.")
            false
        } else if (userName.length < 2) {
            _invalidUserIdEventLiveData.postValue("이름은 2글자 이상이어야 합니다.")
            false
        } else if (!RegularExpressionUtil.validCheck(RegularExpressionUtil.Regex.NAME, userName)) {
            _invalidUserIdEventLiveData.postValue("제대로 된 한글형식으로 입력해주세요.")
            false
        } else {
            _validUserPwdEventLiveData.call()
            true}
    }


    private fun checkForUserBirthday(userBirthday: String) : Boolean{
        return if (userBirthday.isBlank() || userBirthday.isEmpty()) {
            _invalidNicknameEventLiveData.postValue("생년월일을 입력해주세요.")
            false
        } else if (!RegularExpressionUtil.validCheck(RegularExpressionUtil.Regex.BIRTH, userBirthday)){
            _invalidNicknameEventLiveData.postValue("생년월일은 8자리 숫자이어야 합니다.")
            false
        } else {
            _validBirthInfoEventLiveData.call()
            true
        }
    }

    private fun checkForUserSmsInfo(usersSmsInfo: String) : Boolean {
        return if (usersSmsInfo.isBlank() || usersSmsInfo.isEmpty()){
            _invalidSmsInfoEventLiveData.postValue("전화번호를 입력해주세요.")
            false
        } else if (!RegularExpressionUtil.validCheck(RegularExpressionUtil.Regex.PHONE, usersSmsInfo)){
            _invalidSmsInfoEventLiveData.postValue("전화번호를 확인해주세요.")
            false
        } else{
            _validSmsInfoEventLiveData.call()
            true
        }
    }

    fun checkForSaveSignUpInfo(userName : String, userBirthday : String, usersSmsInfo : String) {
        val checkName = checkForUserName(userName)
        val checkBirthInfo = checkForUserBirthday(userBirthday)
        val checkSmsInfo = checkForUserSmsInfo(usersSmsInfo)
        if (checkName && checkBirthInfo && checkSmsInfo) _sendSignUpInfoEventLiveData.call()
    }

    fun checkForSendSignUpInfo(userId : String, userPwd : String, userPwd2 : String,userNickname : String) : Boolean {
        return if (!checkForUserId(userId)) false
        else if (!checkForUserPwd(userPwd, userPwd2)) false
        else checkForUserNickname(userNickname)
    }

    fun saveSignUpInfo(userName : String, userBirthday : String, usersSmsInfo : String){
        personalSignUpInfo = PersonalSignUpInfo(userName, userBirthday, usersSmsInfo)
    }


    //[SignUpFirstFragment]--------------------------------------------------------------------------------------------

    //SignUpFirstFragment 변수들
    private val _invalidUserIdEventLiveData = SingleLiveEvent<String>()
    val invalidUserIdEventLiveData: LiveData<String> get() = _invalidUserIdEventLiveData
    private val _validUserPwdEventLiveData = SingleLiveEvent<Any>()
    val validUserPwdEventLiveData: LiveData<Any> get() = _validUserPwdEventLiveData
    private val _invalidNicknameEventLiveData = SingleLiveEvent<String>()
    val invalidNicknameEventLiveData: LiveData<String> get() = _invalidNicknameEventLiveData
    private val _sendSignUpInfoEventLiveData = SingleLiveEvent<Any>()
    val sendSignUpInfoEventLiveData: LiveData<Any> get() = _sendSignUpInfoEventLiveData
    
    private var signUpInfo : SignUpInfo? =null

    fun sendSignUpInfo(userId : String, userPwd : String, userNickname : String): LiveData<Boolean> {
       signUpInfo = SignUpInfo(personalSignUpInfo!!.username, personalSignUpInfo!!.userBirthday, personalSignUpInfo!!.usersSmsInfo,
           userId, userPwd, userNickname, false)
        signRepository.signUp(signUpInfo!!) //성공시 라이브 데이터에 값 보내서 bind로 이동하게 해야함.(수정필요)
        return signRepository.onSuccessSignupEvent
    }


    private fun checkForUserId(userId: String) : Boolean {
        return if (userId.isBlank() || userId.isEmpty()) {
            showSnackbar("아이디를 입력해주세요.")
            false
        } else if (!RegularExpressionUtil.validCheck(RegularExpressionUtil.Regex.ID, userId)) {
            showSnackbar("아이디는 4~12자의 영문, 숫자만 가능합니다.")
            false
        } else true
    }

    private fun checkForUserPwd(userPwd: String, userPwd2: String) : Boolean {
        return if (userPwd.isBlank() || userPwd.isEmpty() || userPwd2.isBlank() || userPwd2.isEmpty()) {
            showSnackbar("비밀번호를 입력해주세요.")
            false
        }else if (userPwd != userPwd2){
            showSnackbar("비밀번호 확인이 일치하지 않습니다.")
            false
        }
        else if (userPwd.length < 8 || userPwd.length > 22) {
            showSnackbar("비밀번호는 8~22자로 입력해야 합니다.")
            false
        } else true
    }

    private fun checkForUserNickname(userNickname: String) : Boolean {
        return if (userNickname.isBlank() || userNickname.isEmpty()) {
            showSnackbar("닉네임을 입력해주세요.")
            false
        }else if (userNickname.length < 5) {
            showSnackbar("닉네임은 다섯글자 이상이어야 합니다.")
            false
        } else if (!RegularExpressionUtil.validCheck(RegularExpressionUtil.Regex.NICKNAME, userNickname)) {
            showSnackbar("닉네임은 10글자 이내의 한글만 입력가능합니다.")
            false
        } else true
    }

}
//------------------------------------------------------------------------------------------------------------------------

data class PersonalSignUpInfo(val username : String, val userBirthday: String, val usersSmsInfo: String)