package com.example.userapp.ui.intro

import androidx.lifecycle.LiveData
import com.example.userapp.base.BaseViewModel
import com.example.userapp.utils.SingleLiveEvent
import java.util.concurrent.TimeUnit

class IntroViewModel : BaseViewModel() {

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
}