package com.example.userapp.ui.signin

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.userapp.base.BaseSessionViewModel
import com.example.userapp.data.dto.UserModel
import com.example.userapp.data.repository.SignRepository
import com.example.userapp.utils.SingleLiveEvent

class SignInViewModel(application: Application) : BaseSessionViewModel(application) {

    private val signRepository: SignRepository = SignRepository.getInstance()

    private val _onSuccessLoginUserData = SingleLiveEvent<UserModel>()
    val onSuccessLoginUserData : LiveData<UserModel> get() = _onSuccessLoginUserData

    private val _onSuccessSaveUserInfo = SingleLiveEvent<Any>()
    val onSuccessSaveUserInfo : LiveData<Any> get() = _onSuccessSaveUserInfo


    fun checkForSignInInfo(userId : String, userPwd : String) : Boolean {
        return if (userId.isBlank()||userId.isEmpty()){
            showSnackbar("아이디를 입력해주세요.")
            false
        } else if (userPwd.isBlank() || userPwd.isEmpty()){
            showSnackbar("비밀번호를 입력해주세요.")
            false
        } else true
    }

    fun sendSignInInfo(userId : String, userPwd : String){
        apiCall(signRepository.signIn(userId, userPwd), { userdata ->
            _onSuccessLoginUserData.postValue(userdata) }, {showSnackbar("로그인에 실패했습니다. 다시 시도해주세요.")})
    }

    fun saveUserInfo(userData : UserModel) {
        apiCall(userRepository.saveUserInfo(userData),{ _onSuccessSaveUserInfo.value = true} )
    }



}