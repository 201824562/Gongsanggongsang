package com.example.gongsanggongsang.Splash

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gongsanggongsang.data.AppDatabase
import com.example.gongsanggongsang.data.UserDataRepository

class SplashViewmodel(application: Application) : AndroidViewModel(application) {
    val repository: UserDataRepository = UserDataRepository(AppDatabase.getDatabase(application, viewModelScope))


}