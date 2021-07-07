package com.example.userapp.data.repository

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.userapp.data.AppDatabase
import com.example.userapp.data.dto.UserModel
import com.example.userapp.data.model.SignUpInfo
import com.example.userapp.utils.SingleLiveEvent
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Single

class SignRepository() {

    companion object {
        private var sInstance: SignRepository? = null
        fun getInstance(): SignRepository {
            return sInstance
                ?: synchronized(this) {
                    val instance = SignRepository()
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
                        emitter.onSuccess(
                            UserModel(it.data!!["id"].toString(), it.data!!["name"].toString(), it.data!!["nickname"].toString(),
                            it.data!!["birthday"].toString(), it.data!!["SmsInfo"].toString())
                        )
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


    fun checkUserStatusAllowed(token: String): Single<Boolean> {
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

    fun checkUserStatusWaiting(token: String): Single<Boolean> {
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

}