package com.parasol.adminapp.ui.main

import androidx.lifecycle.LiveData
import com.parasol.adminapp.ui.base.BaseViewModel
import com.parasol.adminapp.utils.SingleLiveEvent
import java.util.concurrent.TimeUnit

class MainViewModel : BaseViewModel(){

    private val _onBackPressedEventLiveData = SingleLiveEvent<Any>()
    val onBackPressedEventLiveData: LiveData<Any> get() = _onBackPressedEventLiveData
    private var mBackPressedAt = 0L

    fun onBackPressed() {
        if (mBackPressedAt + TimeUnit.SECONDS.toMillis(2) > System.currentTimeMillis()) {
            _onBackPressedEventLiveData.call()
        } else {
            showSnackbar("\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.")
            mBackPressedAt = System.currentTimeMillis()
        }
    }

    /*   fun getUserStatus() {
       apiCall(userRepository.requestSignUpStatus(authToken), { status ->
           when (status.data.state) {
               UserStatus.NOT_USER -> callSessionInvalidEvent()
               else -> {
                   // DO Nothing
               }
           }
       })
   }*/
}