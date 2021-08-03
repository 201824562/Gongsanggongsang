package com.example.userapp

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.userapp.base.BaseActivityViewModel
import com.example.userapp.base.BaseViewModel
import com.example.userapp.data.AppDatabase
import com.example.userapp.data.entity.User
import com.example.userapp.data.repository.UserRepository
import com.example.userapp.utils.SingleLiveEvent
import java.util.concurrent.TimeUnit

open class MainActivityViewModel(application: Application) : BaseActivityViewModel(application) {


    private val userRepository : UserRepository  = UserRepository.getInstance(AppDatabase.getDatabase(application, viewModelScope))

    fun getUserInfo() : LiveData<User> {
        return userRepository.getUserInfo()
    }

}