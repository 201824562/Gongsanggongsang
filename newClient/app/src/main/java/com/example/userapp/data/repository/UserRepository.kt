package com.example.userapp.data.repository

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import com.example.userapp.data.AppDatabase
import com.example.userapp.data.dto.UserModel
import com.example.userapp.data.entity.User
import io.reactivex.Completable

class UserRepository(appDatabase: AppDatabase) {

    companion object {
        private var sInstance: UserRepository? = null
        fun getInstance(database: AppDatabase): UserRepository {
            return sInstance
                ?: synchronized(this) {
                    val instance = UserRepository(database)
                    sInstance = instance
                    instance
                }
        }
    }

    private val userdataDao = appDatabase.userDao()
    private val SHARED_PREFERENCES_TOKEN = "client_login_token"
    private val SHARED_PREFERENCES_AGENCY = "client_agency_info"
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

    fun getUserToken(application : Application)  : String? {
        val sharedPreferences = application.getSharedPreferences(SHARED_PREFERENCES_TOKEN, Context.MODE_PRIVATE)
        return if (storedToken != null) storedToken
        else {
            if (sharedPreferences.getString(SHARED_PREFERENCES_TOKEN , "") != "") {
                storedToken = sharedPreferences.getString(SHARED_PREFERENCES_TOKEN , "")
                storedToken
            }else storedToken
        }
    }

    fun saveUserToken(token: String, context: Context){
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_TOKEN, Context.MODE_PRIVATE)
        storedToken = token
        sharedPreferences.edit {
            putString(SHARED_PREFERENCES_TOKEN , token)
            apply()
        }
    }

    fun removeUserToken(context: Context) {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_TOKEN, Context.MODE_PRIVATE)
        storedToken = null
        sharedPreferences.edit { remove(SHARED_PREFERENCES_TOKEN ) }
    }

    fun getUserInfo() : LiveData<User> {
        return userdataDao.getUserData()
    }

    fun saveUserInfo(userData : UserModel) : Completable{
        return userdataDao.insertUserData(userData.getUserEntity())
    }

    fun deleteUserInfo(userId: String) : Completable {
        return Completable.create { emitter ->
            userdataDao.deleteUserData(userId)
            emitter.onComplete()
        }
    }


}