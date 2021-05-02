package com.example.gongsanggongsang.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.gongsanggongsang.Login.LoginDataClass

class UserDataViewmodel(application: Application) : AndroidViewModel(application) {
    val repository: UserDataRepository = UserDataRepository(AppDatabase.getDatabase(application, viewModelScope))

    //여기서 서버에 정보를 저장시켜서 보내야 함.

    fun insert(it: UserDataClass) {
        repository.insert(it)
    }

    fun checkLoginValid(it : LoginDataClass) : Boolean{
        repository.checkLoginValid(it)
        return repository.isLoginDataValid.value!!
    }




}

