package com.example.userapp.data.repository

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import com.example.userapp.ui.base.BaseSessionViewModel
import com.example.userapp.data.AppDatabase
import com.example.userapp.data.dto.UserModel
import com.example.userapp.data.model.Agency
import com.example.userapp.data.model.PhotoCardInfo
import com.example.userapp.data.model.ReceiverSignIn
import com.example.userapp.data.model.SignUpInfo
import com.example.userapp.utils.SingleLiveEvent
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable
import io.reactivex.Single

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
        private val FIRESTORE_ADMINISTER = "ADMINISTER"
        private val FIRESTORE_AGENCY = "SIGN_UP_AGENCY"
        private val FIRESTORE_WAITING_USER_INFO = "USER_INFO_WAITING"
        private val FIRESTORE_ALLOWED_USER_INFO = "USER_INFO"
    }

    private val userdataDao = appDatabase.userDao()
    private val SHARED_PREFERENCES_AGENCY = "client_agency_info"
    private val SHARED_PREFERENCES_FCMTOKEN = "client_fcm_token"
    private val SHARED_PREFERENCES_TOKEN = "client_login_token"
    private var storedAgency: String? = null
    private var storeFcmToken : String? = null
    private var storedToken: String? = null


    fun getAdminTokens(agencyInfo: String) : Single<List<String>> {
        return Single.create{ emitter ->
            fireStore.collection(FIRESTORE_ADMINISTER).whereEqualTo("agency", agencyInfo)
                .get()
                .addOnSuccessListener {
                    when {
                        it.isEmpty -> emitter.onError(Throwable("Error getting SEARCHED AGENGYINFO at SIGN_UP_AGENCY"))
                        it.documents.isEmpty() -> emitter.onError(Throwable("Error getting SEARCHED AGENGYINFO at SIGN_UP_AGENCY"))
                        else ->
                        {   val tokenList : MutableList<String> = mutableListOf()
                            for (document in it){
                                if ((document["fcmToken"] != null)){
                                        val list : List<String> = document["fcmToken"] as ArrayList<String>
                                        tokenList.addAll(list)
                                    }
                            }
                            emitter.onSuccess(tokenList)
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents: ", exception)
                    emitter.onError(Throwable("Error getting SEARCHED AGENGYINFO at SIGN_UP_AGENCY"))
                }
        }
    }

    fun getAdminNotifyInfo(agencyInfo: String) : Single<List<BaseSessionViewModel.AdminNotifyInfo>> {
        return Single.create{ emitter ->
            fireStore.collection(FIRESTORE_ADMINISTER).whereEqualTo("agency", agencyInfo)
                .get()
                .addOnSuccessListener {
                    when {
                        it.isEmpty -> emitter.onError(Throwable("Error getting SEARCHED AGENGYINFO at SIGN_UP_AGENCY"))
                        it.documents.isEmpty() -> emitter.onError(Throwable("Error getting SEARCHED AGENGYINFO at SIGN_UP_AGENCY"))
                        else ->
                        {   val tokenList : MutableList<BaseSessionViewModel.AdminNotifyInfo> = mutableListOf()
                            for (document in it){
                                if ((document["fcmToken"] != null)){
                                    val adminNotifyInfo = BaseSessionViewModel.AdminNotifyInfo(document["id"] as String, document["fcmToken"] as ArrayList<String>)
                                    tokenList.add(adminNotifyInfo)
                                }
                            }
                            emitter.onSuccess(tokenList)
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents: ", exception)
                    emitter.onError(Throwable("Error getting SEARCHED AGENGYINFO at SIGN_UP_AGENCY"))
                }
        }
    }

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

    fun saveFCMToken(fcmToken: String, context: Context){
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FCMTOKEN, Context.MODE_PRIVATE)
        storeFcmToken = fcmToken
        sharedPreferences.edit {
            putString(SHARED_PREFERENCES_FCMTOKEN , fcmToken)
            apply()
        }
    }
    fun getFCMToken(application : Application)  : String? {
        val sharedPreferences = application.getSharedPreferences(SHARED_PREFERENCES_FCMTOKEN, Context.MODE_PRIVATE)
        return if (storeFcmToken != null) storeFcmToken
        else {
            if (sharedPreferences.getString(SHARED_PREFERENCES_FCMTOKEN , "") != "") {
                storeFcmToken = sharedPreferences.getString(SHARED_PREFERENCES_FCMTOKEN , "")
                storeFcmToken
            }else storeFcmToken
        }
    }
    fun removeFCMToken(context: Context) {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FCMTOKEN, Context.MODE_PRIVATE)
        storeFcmToken = null
        sharedPreferences.edit { remove(SHARED_PREFERENCES_FCMTOKEN ) }
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

    fun getSearchAgencyResult(keyWord : String?, getAll : Boolean) : Single<List<Agency>> {
        return Single.create { emitter ->
            if (getAll){
                fireStore.collection(FIRESTORE_AGENCY).get()
                    .addOnSuccessListener { documents ->
                        val agencyList : MutableList<Agency> = mutableListOf()
                        for (document in documents){
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
            }else{
                fireStore.collection(FIRESTORE_AGENCY).get()
                    .addOnSuccessListener { documents ->
                        val agencyList : MutableList<Agency> = mutableListOf()
                        for (document in documents){
                            if ((document["location"].toString()).contains(keyWord!!) || (document["name"].toString()).contains(keyWord)){
                                agencyList.add(
                                    Agency(key = document["key"].toString(), name = document["name"].toString().replace(",", " "),
                                        location = document["location"].toString().replace(",", " ")))
                            }
                        }
                        emitter.onSuccess(agencyList)
                    }
                    .addOnFailureListener { exception ->
                        Log.w(ContentValues.TAG, "Error getting documents: ", exception)
                        emitter.onError(Throwable("Error getting SEARCHED AGENGYINFO at SIGN_UP_AGENCY"))
                    }
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

    fun checkingAllowedSignIn(userId : String, userPwd : String, fcmToken: String) : Single<ReceiverSignIn> {
        return Single.create{ emitter ->
            fireStore.collection(FIRESTORE_ALLOWED_USER_INFO).document(userId)
                .get()
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "Found SignIn ID!!")
                    if (it.data != null && it.data!!["id"] == userId && it.data!!["pwd"]==userPwd){
                        val fcmTokenList = if (it.data!!["fcmToken"] == null) mutableListOf()
                        else it.data!!["fcmToken"] as ArrayList<String>
                        fcmTokenList.add(fcmToken)
                        fireStore.collection(FIRESTORE_ALLOWED_USER_INFO).document(userId).update("fcmToken", fcmTokenList)
                        emitter.onSuccess(
                            ReceiverSignIn(true, UserModel(it.data!!["id"].toString(), it.data!!["name"].toString(), it.data!!["nickname"].toString(),
                                it.data!!["birthday"].toString(), it.data!!["smsInfo"].toString(), it.data!!["agency"].toString()))
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
    fun deleteUsersDeviceToken(userId : String, deviceToken : String) : Completable {
        return Completable.create{ emitter ->
            fireStore.collection(FIRESTORE_ALLOWED_USER_INFO).document(userId)
                .get()
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "getUserInfo Successed!!")
                    val fcmTokenList : MutableList<String> = it["fcmToken"] as ArrayList<String>
                    fcmTokenList.removeIf { fcmToken -> fcmToken == deviceToken }
                    fireStore.collection(FIRESTORE_ALLOWED_USER_INFO).document(userId).update("fcmToken", fcmTokenList)
                        .addOnSuccessListener {
                            Log.d(ContentValues.TAG, "delete UserDeviceToken Succeed!!")
                            emitter.onComplete()
                        }.addOnFailureListener { exception ->
                            Log.w(ContentValues.TAG, "Error todo deleteDeviceToken: ", exception)
                            emitter.onError(Throwable("Error doing deleteDeviceToken"))
                        }
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error todo deleteDeviceToken: ", exception)
                    emitter.onError(Throwable("Error doing deleteDeviceToken"))
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

    private val FIRESTORE_PHOTO_CARD = "PHOTO_CARD"

    fun saveUserPhotoCard(token : String, data : PhotoCardInfo) : Completable {
        return Completable.create { emitter ->
            fireStore.collection(FIRESTORE_PHOTO_CARD)
                .document(token).set(data)
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { emitter.onError(Throwable("Error saving PHOTO_CARD")) }
        }
    }
    fun deleteUserPhotoCard(token : String) : Completable {
        return Completable.create { emitter ->
            fireStore.collection(FIRESTORE_PHOTO_CARD)
                .document(token).delete()
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { emitter.onError(Throwable("Error delete PHOTO_CARD")) }
        }
    }


}