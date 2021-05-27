package com.example.userapp.ui.signin

import androidx.lifecycle.LiveData
import com.example.userapp.base.BaseViewModel
import com.example.userapp.data.model.SignUpInfo
import com.example.userapp.data.repository.UserRepository
import com.example.userapp.ui.signup.PersonalSignUpInfo

class SignInViewModel : BaseViewModel() {
    val repository: UserRepository = UserRepository.getInstance()

    fun checkForSignInInfo(userId : String, userPwd : String) : Boolean {
        return if (userId.isBlank()||userId.isEmpty()){
            showSnackbar("아이디를 입력해주세요.")
            false
        } else if (userPwd.isBlank() || userPwd.isEmpty()){
            showSnackbar("비밀번호를 입력해주세요.")
            false
        } else true
    }

    fun sendSignInInfo(userId : String, userPwd : String): LiveData<Boolean> {
        repository.signIn(userId, userPwd) //성공시 라이브 데이터에 값 보내서 bind로 이동하게 해야함.(수정필요)
        return repository.onSuccessSignInEvent
    }

}