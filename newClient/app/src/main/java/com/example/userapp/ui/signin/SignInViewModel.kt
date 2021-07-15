package com.example.userapp.ui.signin

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.userapp.base.BaseSessionViewModel
import com.example.userapp.data.dto.UserModel
import com.example.userapp.data.model.ReceiverSignIn
import com.example.userapp.data.model.UserStatus
import com.example.userapp.data.repository.SignRepository
import com.example.userapp.utils.SingleLiveEvent
import io.reactivex.Single
import io.reactivex.functions.BiFunction

class SignInViewModel(application: Application) : BaseSessionViewModel(application) {

    private val signRepository: SignRepository = SignRepository.getInstance()

    private val _onSuccessSaveUserInfo = SingleLiveEvent<Any>()
    val onSuccessSaveUserInfo : LiveData<Any> get() = _onSuccessSaveUserInfo

    private val _userStatusEvent = SingleLiveEvent<UserStatus>()
    val userStatusEvent : LiveData<UserStatus> get() = _userStatusEvent
    private var userData : UserModel ?= null


/*    fun checkForSignInInfo(userId : String, userPwd : String) : Boolean {
        return if (userId.isBlank()||userId.isEmpty()){
            _onFailLoginUserData.postValue("아이디를 입력해주세요.")
            false
        } else if (userPwd.isBlank() || userPwd.isEmpty()){
            _onFailLoginUserData.postValue("비밀번호를 입력해주세요.")
            false
        } else true
    }*/

    fun sendSignInInfo(userId : String, userPwd : String) {
        apiCall(
            Single.zip(signRepository.checkingAllowedsignIn(userId, userPwd), signRepository.checkingWaitingsignIn(userId, userPwd),
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
                _onSuccessSaveUserInfo.value = true
                userRepository.saveUserToken(it.id, context) } )
        }
    }




}