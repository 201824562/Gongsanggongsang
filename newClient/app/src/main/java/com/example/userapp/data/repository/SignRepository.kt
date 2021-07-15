package com.example.userapp.data.repository

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.userapp.data.dto.UserModel
import com.example.userapp.data.model.Agency
import com.example.userapp.data.model.ReceiverSignIn
import com.example.userapp.data.model.SignUpInfo
import com.example.userapp.utils.SingleLiveEvent
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable
import io.reactivex.Observable
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



    fun getSearchAgencyResult(keyWord : String) : Single<List<Agency>> {
        return Single.create { emitter ->
            firestore.collection("SIGN_UP_AGENCY").whereArrayContainsAny("location", listOf(keyWord)).get()
                .addOnSuccessListener { documents ->
                    Log.e("checking","CHECKING")
                    Log.e("checking","$documents")
                    val agencyList : MutableList<Agency> = mutableListOf()
                    for (document in documents){
                        Log.e("checking","${document.data["name"]}")
                        agencyList.add(Agency(key = document["key"].toString(), name = document["name"].toString().replace(",", " "),
                            location = document["location"].toString().replace(",", " ")))
                    }
                    emitter.onSuccess(agencyList)
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents: ", exception)
                    emitter.onError(Throwable("Error getting SEARCHED AGENGYINFO at SIGN_UP_AGENCY"))
                }
        }
    }

    fun checkIdFromWaitingUser(userId: String) : Single<Boolean> {
        return Single.create { emitter ->
            firestore.collection("USER_INFO_WAITING").whereEqualTo("id", userId)
                .get()
                .addOnSuccessListener {
                    if (it.isEmpty) emitter.onSuccess(false)
                    else emitter.onSuccess(true)
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting Id duplicate info", exception)
                    emitter.onError(Throwable("Error getting CHECK_ID at USER_INFO_WAITING"))
                }
        }
    }

    fun checkIdFromAllowedUser(userId: String) : Single<Boolean> {
        return Single.create { emitter ->
            firestore.collection("USER_INFO").whereEqualTo("id", userId)
                .get()
                .addOnSuccessListener {
                    if (it.isEmpty) emitter.onSuccess(false)
                    else emitter.onSuccess(true)
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting Id duplicate info", exception)
                    emitter.onError(Throwable("Error getting CHECK_ID at USER_INFO"))
                }
        }
    }

    fun checkNicknameFromWaitingUser(nickname: String) : Single<Boolean> {
        return Single.create { emitter ->
            firestore.collection("USER_INFO_WAITING").whereEqualTo("nickname", nickname)
                .get()
                .addOnSuccessListener {
                    if (it.isEmpty) emitter.onSuccess(false)
                    else emitter.onSuccess(true)
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting Nickname duplicate info", exception)
                    emitter.onError(Throwable("Error getting CHECK_NICKNAME at USER_INFO_WAITING"))
                }
        }
    }

    fun checkNicknameFromAllowedUser(nickname: String) : Single<Boolean> {
        return Single.create { emitter ->
            firestore.collection("USER_INFO").whereEqualTo("nickname", nickname)
                .get()
                .addOnSuccessListener {
                    if (it.isEmpty) emitter.onSuccess(false)
                    else emitter.onSuccess(true)
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting Nickname duplicate info", exception)
                    emitter.onError(Throwable("Error getting CHECK_NICKNAME at USER_INFO"))
                }
        }
    }


    fun checkUserStatusWaiting(token: String): Single<Boolean> {
        return Single.create { emitter ->
            firestore.collection("USER_INFO_WAITING").document()
                .get()
                .addOnSuccessListener {
                    if (it.data != null && it.data!!["id"] == token){ emitter.onSuccess(true) }
                    else emitter.onSuccess(false)
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents: ", exception)
                    emitter.onError(Throwable("Error getting USERINFO at USER_INFO_WAITING"))
                }
        }
    }

    fun checkUserStatusAllowed(token: String): Single<Boolean> {
        return Single.create { emitter ->
            firestore.collection("USER_INFO").document(token)
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


    fun signUp(it: SignUpInfo) : Completable {
        return Completable.create { emitter ->
            firestore.collection("USER_INFO_WAITING").document(it.id)
                .set(it)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "Signup Successed!!")
                    _onSuccessSignupEvent.value = true
                    emitter.onComplete()
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents: ", exception)
                    _onSuccessSignupEvent.value = false
                    emitter.onError(Throwable("Error doing SignUp"))
                }
        }
    }

    fun checkingAllowedsignIn(userId : String, userPwd : String) : Single<ReceiverSignIn> {
        return Single.create{ emitter ->
            firestore.collection("USER_INFO").document(userId)
                .get()
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "Found SignIn ID!!")
                    if (it.data != null && it.data!!["id"] == userId && it.data!!["pwd"]==userPwd){
                        Log.d(ContentValues.TAG, "SignIn Successed!!")
                        emitter.onSuccess(
                            ReceiverSignIn(true, UserModel(it.data!!["id"].toString(), it.data!!["name"].toString(), it.data!!["nickname"].toString(),
                                it.data!!["birthday"].toString(), it.data!!["SmsInfo"].toString())))
                    }
                    else emitter.onSuccess(ReceiverSignIn(false, null))
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents: ", exception)
                    emitter.onError(Throwable("Error getting SignIn-Info"))
                }
        }
    }

    fun checkingWaitingsignIn(userId : String, userPwd : String): Single<Boolean>{
        return Single.create{ emitter ->
            firestore.collection("USER_INFO_WAITING").whereEqualTo("id", userId).whereEqualTo("pwd", userPwd)
                .get()
                .addOnSuccessListener {
                    if (it.isEmpty) emitter.onSuccess(false)
                    else emitter.onSuccess(true)
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents: ", exception)
                    emitter.onError(Throwable("Error getting SignIn-Info"))
                }
        }
    }



}