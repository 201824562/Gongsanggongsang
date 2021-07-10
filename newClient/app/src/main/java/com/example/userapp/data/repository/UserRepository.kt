package com.example.userapp.data.repository

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.userapp.data.AppDatabase
import com.example.userapp.data.dto.UserModel
import com.example.userapp.data.model.SignUpInfo
import com.example.userapp.data.model.UserStatus
import com.example.userapp.utils.SingleLiveEvent
import io.reactivex.Completable
import io.reactivex.Single
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserRepository(appdatabase: AppDatabase) {

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

    private val userdataDao = appdatabase.userDao()

    fun saveUserInfo(userData : UserModel) : Completable{
        return Completable.create { emitter ->
            userdataDao.insertUserData(userData.getUserEntity())
            emitter.onComplete()
        }
    }

    fun deleteUserInfo(userId: String) : Completable {
        return Completable.create { emitter ->
            userdataDao.deleteUserData(userId)
            emitter.onComplete()
        }
    }

    //TODO : 이거 막힘. 미쳐부리겠따.
    var storedToken: String? = null
    fun getUserToken() : String? {
        if (storedToken != null) return storedToken
        return storedToken
/*        val token = userdataDao.getUserToken()
        return if (token != null) {
            storedToken = token
            storedToken
        } else null*/
    }


}