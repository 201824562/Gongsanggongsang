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
    private val userdataDao = appdatabase.userDao()

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

    private val firestore = FirebaseFirestore.getInstance()   //.getReference()

    private val _onSuccessSignupEvent = SingleLiveEvent<Boolean>()
    val onSuccessSignupEvent: LiveData<Boolean> get() = _onSuccessSignupEvent


    fun signIn(userId : String, userPwd : String) : Single<UserModel> {
        return Single.create{ emitter ->
            firestore.collection("USER_INFO").document(userId)
                .get()
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "Found SignIn ID!!")
                    if (it.data != null && it.data!!["id"] == userId && it.data!!["pwd"]==userPwd){
                        //_onSuccessSignInEvent.value = true
                        Log.d(ContentValues.TAG, "SignIn Successed!!")
                        emitter.onSuccess(UserModel(it.data!!["id"].toString(), it.data!!["name"].toString(), it.data!!["nickname"].toString(),
                            it.data!!["birthday"].toString(), it.data!!["smsinfo"].toString()))
                    }
                    else emitter.onError(Throwable("Not Existing SignIn-Info!"))
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents: ", exception)
                    emitter.onError(Throwable("Error getting SignIn-Info"))
                }
        }
    }

    fun signUp(it: SignUpInfo) {
        firestore.collection("USER_INFO_WAITING").document(it.id)
            .set(it)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "Signup Successed!!")
                _onSuccessSignupEvent.value = true
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
                _onSuccessSignupEvent.value = false
            }
    }


    fun checkUserStatusAllowed(token: String): Single<Boolean>{
        return Single.create { emitter ->
            firestore.collection("USER_INFO").document(token)
                .get()
                .addOnSuccessListener {
                    if (it.data != null && it.data!!["id"] == token){ emitter.onSuccess(true) }
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents: ", exception)
                    emitter.onError(Throwable("Error getting USERINFO at USER_INFO"))
                }
        }
    }

    fun checkUserStatusWaiting(token: String): Single<Boolean>{
        return Single.create { emitter ->
            firestore.collection("USER_INFO_WAITING").document(token)
                .get()
                .addOnSuccessListener {
                    if (it.data != null && it.data!!["id"] == token){ emitter.onSuccess(true) }
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents: ", exception)
                    emitter.onError(Throwable("Error getting USERINFO at USER_INFO_WAITING"))
                }
        }
    }


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