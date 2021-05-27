package com.example.userapp.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.userapp.data.model.SignUpInfo
import com.example.userapp.utils.SingleLiveEvent

class UserRepository() {
    companion object {
        private var sInstance: UserRepository? = null
        fun getInstance(): UserRepository {
            return sInstance
                ?: synchronized(this) {
                    val instance = UserRepository()
                    sInstance = instance
                    instance
                }
        }
    }

    private val firestore = FirebaseFirestore.getInstance()   //.getReference()

    private val _onSuccessSignInEvent = SingleLiveEvent<Boolean>()
    val onSuccessSignInEvent : LiveData<Boolean> get() = _onSuccessSignInEvent

    private val _onSuccessSignupEvent = SingleLiveEvent<Boolean>()
    val onSuccessSignupEvent: LiveData<Boolean> get() = _onSuccessSignupEvent


    fun signIn(userId : String, userPwd : String){
        firestore.collection("USER_INFO").document(userId)
            .get()
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "Signup Successed!!")
                if (it.data != null){
                    _onSuccessSignInEvent.value = it.data!!["id"]?.toString() == userId && it.data!!["pwd"]==userPwd
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
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
}