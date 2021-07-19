package com.example.adminapp.data.repository

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import com.example.adminapp.data.AppDatabase
import com.example.adminapp.data.dto.AdminModel
import io.reactivex.Completable

class AdminRepository(appDatabase: AppDatabase) {

    companion object {
        private var sInstance: AdminRepository? = null
        fun getInstance(database: AppDatabase): AdminRepository {
            return sInstance
                ?: synchronized(this) {
                    val instance = AdminRepository(database)
                    sInstance = instance
                    instance
                }
        }
    }


    private val adminDao = appDatabase.adminDao()
    private val SHARED_PREFERENCES_TOKEN = "admin_login_token"
    private val SHARED_PREFERENCES_AGENCY = "admin_agency_info"
    private var storedToken: String? = null
    private var storedAgency: String? = null

    fun getAgencyInfo(application : Application)  : String? {
        val sharedPreferences = application.getSharedPreferences(SHARED_PREFERENCES_AGENCY, Context.MODE_PRIVATE)
        return if (storedAgency != null) storedAgency
        else {
            if (sharedPreferences.getString(SHARED_PREFERENCES_AGENCY , "") != "") {
                storedAgency = sharedPreferences.getString(SHARED_PREFERENCES_AGENCY , "")
                storedAgency
            }else storedAgency
        }
    }

    fun saveAgencyInfo(agencyInfo: String, context: Context){
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_AGENCY, Context.MODE_PRIVATE)
        storedAgency = agencyInfo
        sharedPreferences.edit {
            putString(SHARED_PREFERENCES_AGENCY , agencyInfo)
            apply()
        }
    }

    fun removeAgencyInfo(context: Context) {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_AGENCY, Context.MODE_PRIVATE)
        storedAgency = null
        sharedPreferences.edit { remove(SHARED_PREFERENCES_AGENCY ) }
    }

    fun getAdminToken(application : Application)  : String? {
        val sharedPreferences = application.getSharedPreferences(SHARED_PREFERENCES_TOKEN, Context.MODE_PRIVATE)
        return if (storedToken != null) storedToken
        else {
            if (sharedPreferences.getString(SHARED_PREFERENCES_TOKEN , "") != "") {
                storedToken = sharedPreferences.getString(SHARED_PREFERENCES_TOKEN , "")
                storedToken
            }else storedToken
        }
    }

    fun saveAdminToken(token: String, context: Context){
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_TOKEN, Context.MODE_PRIVATE)
        storedToken = token
        sharedPreferences.edit {
            putString(SHARED_PREFERENCES_TOKEN , token)
            apply()
        }
    }

    fun removeAdminToken(context: Context) {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_TOKEN, Context.MODE_PRIVATE)
        storedToken = null
        sharedPreferences.edit { remove(SHARED_PREFERENCES_TOKEN ) }
    }

    fun saveAdminInfo(adminData : AdminModel) : Completable {
        return adminDao.insertAdminData(adminData.getAdminEntity())
    }

    fun deleteAdminInfo(adminId: String) : Completable {
        return Completable.create { emitter ->
            adminDao.deleteAdminData(adminId)
            emitter.onComplete()
        }
    }


}