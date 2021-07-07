package com.example.userapp.ui.splash

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.userapp.base.BaseSessionViewModel
import com.example.userapp.data.model.UserStatus
import com.example.userapp.data.repository.SignRepository
import com.example.userapp.utils.SingleLiveEvent
import io.reactivex.Single
import io.reactivex.functions.BiFunction

class SplashViewModel(application: Application) : BaseSessionViewModel(application) {

    private val signRepository: SignRepository = SignRepository.getInstance()

    private val _userStatusEvent = SingleLiveEvent<UserStatus>()
    val userStatusEvent: LiveData<UserStatus> get() = _userStatusEvent

    fun getUserStatus() {
        apiCall(Single.zip(signRepository.checkUserStatusAllowed(authToken), signRepository.checkUserStatusWaiting(authToken),
            BiFunction<Boolean, Boolean, UserStatus> { allowed, waiting ->
                var status = UserStatus.NOT_USER
                if (allowed) status = UserStatus.USER
                else if (waiting) status = UserStatus.WAIT_APPROVE
                return@BiFunction status
            }), { _userStatusEvent.postValue(it) })
    }

}