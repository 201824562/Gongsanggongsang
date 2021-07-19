package com.example.adminapp.data.repository

import android.content.ContentValues
import android.util.Log
import com.example.adminapp.data.dto.AdminModel
import com.example.adminapp.data.model.ReceiverSignIn
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Single

//TODO : 와이파이(서버연결관련) 에러처리
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


    fun checkAdminStatus(token: String): Single<Boolean> {
        return Single.create { emitter ->
            firestore.collection("ADMINISTER").document(token)
                .get()
                .addOnSuccessListener {
                    if (it.data != null && it.data!!["id"] == token){ emitter.onSuccess(true) }
                    else emitter.onSuccess(false)
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents: ", exception)
                    emitter.onError(Throwable("Error getting USERINFO at USER_INFO"))
                }
        }
    }


    fun checkingAdminSignIn(adminId : String, adminPwd : String) : Single<ReceiverSignIn> {
        return Single.create{ emitter ->
            firestore.collection("ADMINISTER").document(adminId)
                .get()
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "Found SignIn ID!!")
                    if (it.data != null && it.data!!["id"] == adminId && it.data!!["pwd"]==adminPwd){
                        Log.d(ContentValues.TAG, "SignIn Successed!!")
                        emitter.onSuccess(
                            ReceiverSignIn(true, AdminModel(it.data!!["agency"].toString(), it.data!!["id"].toString(), it.data!!["name"].toString(),
                                 it.data!!["smsInfo"].toString())
                            ))
                    }
                    else emitter.onSuccess(ReceiverSignIn(false, null))
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents: ", exception)
                    emitter.onError(Throwable("Error getting SignIn-Info"))
                }
        }
    }




}