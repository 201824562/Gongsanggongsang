package com.example.userapp.data.repository

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.userapp.data.AppDatabase
import com.example.userapp.data.dto.UserModel
import com.example.userapp.data.entity.User
import com.example.userapp.data.model.Agency
import com.example.userapp.data.model.ReceiverSignIn
import com.example.userapp.data.model.SignUpInfo
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable
import io.reactivex.Single

//TODO : 와이파이(서버연결관련) 에러처리
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
        private val FIRESTORE_AGENCY = "SIGN_UP_AGENCY"
        private val FIRESTORE_WAITING_USER_INFO = "USER_INFO_WAITING"
        private val FIRESTORE_ALLOWED_USER_INFO = "USER_INFO"
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

    fun getUserInfo() : Single<UserModel> {
        return Single.create<UserModel> { emitter ->
            val userEntityData = userdataDao.getUserData()
            try {
                if (userEntityData == null) emitter.onError(Throwable("Error of Getting UserInfo"))
                else emitter.onSuccess(userEntityData.getUserModel())
            }catch (e : Exception){
            }
        }
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

    private val fireStore = FirebaseFirestore.getInstance()   //.getReference()

    fun getSearchAgencyResult(keyWord : String) : Single<List<Agency>> {
        return Single.create { emitter ->
            fireStore.collection(FIRESTORE_AGENCY).whereArrayContainsAny("location", listOf(keyWord)).get()
                .addOnSuccessListener { documents ->
                    Log.e("checking","CHECKING")
                    Log.e("checking","$documents")
                    val agencyList : MutableList<Agency> = mutableListOf()
                    for (document in documents){
                        Log.e("checking","${document.data["name"]}")
                        agencyList.add(
                            Agency(key = document["key"].toString(), name = document["name"].toString().replace(",", " "),
                            location = document["location"].toString().replace(",", " "))
                        )
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
            fireStore.collection(FIRESTORE_WAITING_USER_INFO).whereEqualTo("id", userId)
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
            fireStore.collection(FIRESTORE_ALLOWED_USER_INFO).whereEqualTo("id", userId)
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
            fireStore.collection(FIRESTORE_WAITING_USER_INFO).whereEqualTo("nickname", nickname)
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
            fireStore.collection(FIRESTORE_ALLOWED_USER_INFO).whereEqualTo("nickname", nickname)
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

    fun checkUserStatusAllowed(token: String): Single<Boolean> {
        return Single.create { emitter ->
            fireStore.collection(FIRESTORE_ALLOWED_USER_INFO).document(token)
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
            fireStore.collection(FIRESTORE_WAITING_USER_INFO).document(it.id)
                .set(it)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "Signup Successed!!")
                    emitter.onComplete()
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents: ", exception)
                    emitter.onError(Throwable("Error doing SignUp"))
                }
        }
    }

    fun checkingAllowedsignIn(userId : String, userPwd : String) : Single<ReceiverSignIn> {
        return Single.create{ emitter ->
            fireStore.collection(FIRESTORE_ALLOWED_USER_INFO).document(userId)
                .get()
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "Found SignIn ID!!")
                    if (it.data != null && it.data!!["id"] == userId && it.data!!["pwd"]==userPwd){
                        Log.d(ContentValues.TAG, "SignIn Successed!!")
                        emitter.onSuccess(
                            ReceiverSignIn(true, UserModel(it.data!!["agency"].toString(), it.data!!["id"].toString(), it.data!!["name"].toString(), it.data!!["nickname"].toString(),
                                it.data!!["birthday"].toString(), it.data!!["smsInfo"].toString()))
                        )
                    }
                    else emitter.onSuccess(ReceiverSignIn(false, null))
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents: ", exception)
                    emitter.onError(Throwable("Error getting SignIn-Info"))
                }
        }
    }

    fun checkingWaitingSignIn(userId : String, userPwd : String): Single<Boolean> {
        return Single.create{ emitter ->
            fireStore.collection(FIRESTORE_WAITING_USER_INFO).whereEqualTo("id", userId).whereEqualTo("pwd", userPwd)
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

    fun findId(name: String, birthInfo : String, smsInfo : String) : Single<String> {
        return Single.create { emitter ->
            fireStore.collection(FIRESTORE_ALLOWED_USER_INFO)
                .whereEqualTo("name", name)
                .whereEqualTo("birthday", birthInfo)
                .whereEqualTo("smsInfo", smsInfo)
                .get()
                .addOnSuccessListener {
                    if (it.isEmpty) emitter.onSuccess("")
                    else emitter.onSuccess(it.documents[0]["id"] as String)
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting Nickname duplicate info", exception)
                    emitter.onError(Throwable("Error getting CHECK_NICKNAME at USER_INFO"))
                }
        }
    }

    fun findPwd(name: String, Id : String) : Single<String> {
        return Single.create { emitter ->
            fireStore.collection(FIRESTORE_ALLOWED_USER_INFO)
                .whereEqualTo("name", name)
                .whereEqualTo("id", Id)
                .get()
                .addOnSuccessListener {
                    if (it.isEmpty) emitter.onSuccess("")
                    else emitter.onSuccess(it.documents[0]["pwd"] as String)
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting Nickname duplicate info", exception)
                    emitter.onError(Throwable("Error getting CHECK_NICKNAME at USER_INFO"))
                }
        }
    }

    fun changeUserInfoNickname(token: String, userNickname : String) : Completable {
        return Completable.create{ emitter ->
            fireStore.collection(FIRESTORE_ALLOWED_USER_INFO).document(token)
                .update("nickname", userNickname)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "changeUserInfo Successed!!")
                    emitter.onComplete()
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error todo changeUserInfo: ", exception)
                    emitter.onError(Throwable("Error doing changeUserInfo"))
                }
        }
    }

    fun changeUserInfoId(token: String, userId: String) : Completable {
        return Completable.create{ emitter ->
            fireStore.collection(FIRESTORE_ALLOWED_USER_INFO).document(token)
                .update("id", userId)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "changeUserInfo Successed!!")
                    emitter.onComplete()
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error todo changeUserInfo: ", exception)
                    emitter.onError(Throwable("Error doing changeUserInfo"))
                }
        }
    }

    fun changeUserInfoPwd(token: String, userPwd : String) : Completable {
        return Completable.create{ emitter ->
            fireStore.collection(FIRESTORE_ALLOWED_USER_INFO).document(token)
                .update("pwd", userPwd)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "changeUserInfo Successed!!")
                    emitter.onComplete()
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error todo changeUserInfo: ", exception)
                    emitter.onError(Throwable("Error doing changeUserInfo"))
                }
        }
    }

    fun withdrawalUserInfo(userId : String) : Completable {
        return Completable.create{ emitter ->
            fireStore.collection(FIRESTORE_ALLOWED_USER_INFO).document(userId)
                .delete()
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "Withdrawal Successed!!")
                    emitter.onComplete()
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error todo Withdrawal: ", exception)
                    emitter.onError(Throwable("Error doing Withdrawal"))
                }
        }
    }

}