package com.example.gongsanggongsang.Data

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserDataViewmodel(application: Application) : AndroidViewModel(application) {
    val repository: UserDataRepository = UserDataRepository(AppDatabase.getDatabase(application, viewModelScope))
    var allUserd: LiveData<List<UserDataEntity>> = repository.allUsers

    //여기서 서버에 정보를 저장시켜서 보내야 함.

    fun insert(it: UserDataClass) {
        repository.insert(it)
    }



}

