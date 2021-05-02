package com.example.gongsanggongsangAdmin.Fifth.UserInfo


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.gongsanggongsangAdmin.data.AppDatabase
import com.example.gongsanggongsangAdmin.data.UserDataClass

class AdministerViewmodel(application: Application) : AndroidViewModel(application)  {
    val repository: AdministerRepository = AdministerRepository(AppDatabase.getDatabase(application, viewModelScope))

    fun updateAllWaitingUsers() {
        repository.updateAllWaitingUsers()
    }

    fun getAllWaitingusers() : LiveData<List<UserDataClass>> {
        updateAllWaitingUsers()
        return repository.Final_AllWaitinguserlist
    }

    fun acceptUser(userdata : UserDataClass){
        repository.acceptUser(userdata)
    }

    fun deleteWaitingUser(userdata: UserDataClass){
        repository.deleteWaitingUser(userdata)
    }





}