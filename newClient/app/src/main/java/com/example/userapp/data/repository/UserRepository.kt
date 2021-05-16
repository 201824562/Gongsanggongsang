package com.example.userapp.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import android.content.ContentValues
import android.util.Log
import com.example.userapp.data.model.SignUpInfo

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

    fun insert(it: SignUpInfo) {
        firestore.collection("USER_INFO_WAITING").document(it.id)
            .set(it)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "Signup Successed!!")
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }
}