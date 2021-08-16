package com.example.adminapp.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.adminapp.utils.SingleLiveEvent


open class BaseActivityViewModel(application: Application): AndroidViewModel(application) {
    private val  _onPermissionResult = SingleLiveEvent<Any>()
    val onPermissionResult: LiveData<Any> = _onPermissionResult

}