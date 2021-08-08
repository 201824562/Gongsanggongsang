package com.example.adminapp

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.adminapp.base.BaseActivityViewModel
import com.example.adminapp.data.AppDatabase
import com.example.adminapp.data.model.Admin
import com.example.adminapp.data.repository.AdminRepository
import com.google.firebase.firestore.auth.User

open class MainActivityViewModel(application: Application) : BaseActivityViewModel(application) {

    private val adminRepository : AdminRepository  = AdminRepository.getInstance(AppDatabase.getDatabase(application, viewModelScope))

    fun getAdminInfo() : LiveData<Admin?> {
        return adminRepository.getAdminInfo()
    }
}