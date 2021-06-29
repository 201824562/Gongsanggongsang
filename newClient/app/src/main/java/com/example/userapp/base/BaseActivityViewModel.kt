package com.example.userapp.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.userapp.utils.SingleLiveEvent


open class BaseActivityViewModel: ViewModel() {
    private val  _onPermissionResult = SingleLiveEvent<Any>()
    val onPermissionResult: LiveData<Any> = _onPermissionResult

}