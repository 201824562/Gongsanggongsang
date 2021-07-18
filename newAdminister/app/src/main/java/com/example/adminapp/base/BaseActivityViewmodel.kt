package com.example.adminapp.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.adminapp.utils.SingleLiveEvent


open class BaseActivityViewModel: ViewModel() {
    private val  _onPermissionResult = SingleLiveEvent<Any>()
    val onPermissionResult: LiveData<Any> = _onPermissionResult

}