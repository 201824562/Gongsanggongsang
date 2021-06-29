package com.example.userapp.ui.signup

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.userapp.base.BaseSessionViewModel
import com.example.userapp.data.model.SignUpInfo
import com.example.userapp.data.repository.UserRepository
import com.example.userapp.utils.RegularExpressionUtil

class SignUpViewModel(application: Application) : BaseSessionViewModel(application) {

    private var personalSignUpInfo : PersonalSignUpInfo ?= null
    private var signUpInfo : SignUpInfo? =null

    fun checkForSaveSignUpInfo(userName : String, userBirthday : String, usersSmsinfo : String) : Boolean{
        return if (!checkForUserName(userName)) false
        else if (!checkForUserBirthday(userBirthday)) false
        else checkForUserSmsinfo(usersSmsinfo)
    }

    fun checkForSendSignUpInfo(userId : String, userPwd : String, userPwd2 : String,userNickname : String) : Boolean {
        return if (!checkForUserId(userId)) false
        else if (!checkForUserPwd(userPwd, userPwd2)) false
        else checkForUserNickname(userNickname)
    }

    fun saveSignUpInfo(userName : String, userBirthday : String, usersSmsinfo : String){
        personalSignUpInfo = PersonalSignUpInfo(userName, userBirthday, usersSmsinfo)
    }

    fun sendSignUpInfo(userId : String, userPwd : String, userNickname : String): LiveData<Boolean> {
       signUpInfo = SignUpInfo(personalSignUpInfo!!.username, personalSignUpInfo!!.userBirthday, personalSignUpInfo!!.usersSmsinfo,
           userId, userPwd, userNickname, false)
        userRepository.signUp(signUpInfo!!) //성공시 라이브 데이터에 값 보내서 bind로 이동하게 해야함.(수정필요)
        return userRepository.onSuccessSignupEvent
    }


    private fun checkForUserName(userName: String) : Boolean{
        return if (userName.isBlank() || userName.isEmpty()) {
            showSnackbar("이름을 입력해주세요.")
            false
        } else if (userName.length < 2) {
            showSnackbar("이름은 두글자 이상이어야 합니다.")
            false
        } else if (!RegularExpressionUtil.validCheck(RegularExpressionUtil.Regex.NAME, userName)) {
            showSnackbar("이름은 한글만 입력가능합니다.")
            false
        } else true
    }

    private fun checkForUserBirthday(userBirthday: String) : Boolean{
        return if (userBirthday.isBlank() || userBirthday.isEmpty()) {
            showSnackbar("생년월일을 입력해주세요.")
            false
        } else if (!RegularExpressionUtil.validCheck(RegularExpressionUtil.Regex.BIRTH, userBirthday)){
            showSnackbar("생년월일은 8자리 숫자이어야 합니다.")
            false
        } else true
    }

    private fun checkForUserSmsinfo(usersSmsinfo: String) : Boolean {
        return if (usersSmsinfo.isBlank() || usersSmsinfo.isEmpty()){
            showSnackbar("전화번호를 입력해주세요.")
            false
        } else if (!RegularExpressionUtil.validCheck(RegularExpressionUtil.Regex.PHONE, usersSmsinfo)){
            showSnackbar("전화번호를 확인해주세요.")
            false
        } else true
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


data class PersonalSignUpInfo(val username : String, val userBirthday: String, val usersSmsinfo: String)