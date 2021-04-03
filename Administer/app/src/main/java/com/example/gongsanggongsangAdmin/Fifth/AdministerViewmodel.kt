package com.example.gongsanggongsangAdmin.Fifth


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.gongsanggongsangAdmin.data.AppDatabase
import com.example.gongsanggongsangAdmin.data.UserDataClass

class AdministerViewmodel(application: Application) : AndroidViewModel(application)  {
    val repository: AdministerRepository = AdministerRepository(AppDatabase.getDatabase(application, viewModelScope))

    fun updateAllusers() {
        repository.updateAllusers()
    }

    fun getAllusers() : LiveData<List<UserDataClass>> {
        updateAllusers()
        return repository.Final_Alluserlist
    }

    fun acceptUser(userdata : UserDataClass){
        repository.acceptUser(userdata)
    }

    fun denyUser(userdata: UserDataClass){
        repository.denyUser(userdata)
    }


}