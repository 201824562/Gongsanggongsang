package com.example.gongsanggongsang.Data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope

class UserDataViewmodel(application: Application) : AndroidViewModel(application) {
    val repository: UserDataRepository = UserDataRepository(AppDatabase.getDatabase(application, viewModelScope))
    var allTeas: LiveData<List<UserDataEntity>> = repository.allUsers
}

