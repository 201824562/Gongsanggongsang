package com.example.adminapp.data.repository

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import com.example.adminapp.data.AppDatabase
import com.example.adminapp.data.model.AdminModel
import com.example.adminapp.data.model.ReceiverAdmin
import com.example.adminapp.data.model.User
import com.example.adminapp.utils.SingleLiveEvent
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable
import io.reactivex.Single

class AdminRepository(appDatabase: AppDatabase) {

    companion object {
        private var sInstance: AdminRepository? = null
        fun getInstance(database: AppDatabase): AdminRepository {
            return sInstance
                ?: synchronized(this) {
                    val instance = AdminRepository(database)
                    sInstance = instance
                    instance
                }
        }
        private val FIRESTORE_ADMINISTER = "ADMINISTER"
        private val FIRESTORE_WAITING_USER_INFO = "USER_INFO_WAITING"
        private val FIRESTORE_ALLOWED_USER_INFO = "USER_INFO"
    }


    private val adminDao = appDatabase.adminDao()
    private val SHARED_PREFERENCES_TOKEN = "admin_login_token"
    private val SHARED_PREFERENCES_AGENCY = "admin_agency_info"
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

    fun getAdminToken(application : Application)  : String? {
        val sharedPreferences = application.getSharedPreferences(SHARED_PREFERENCES_TOKEN, Context.MODE_PRIVATE)
        return if (storedToken != null) storedToken
        else {
            if (sharedPreferences.getString(SHARED_PREFERENCES_TOKEN , "") != "") {
                storedToken = sharedPreferences.getString(SHARED_PREFERENCES_TOKEN , "")
                storedToken
            }else storedToken
        }
    }

    fun saveAdminToken(token: String, context: Context){
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_TOKEN, Context.MODE_PRIVATE)
        storedToken = token
        sharedPreferences.edit {
            putString(SHARED_PREFERENCES_TOKEN , token)
            apply()
        }
    }

    fun removeAdminToken(context: Context) {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_TOKEN, Context.MODE_PRIVATE)
        storedToken = null
        sharedPreferences.edit { remove(SHARED_PREFERENCES_TOKEN ) }
    }

    fun getAdminInfo() : Single<AdminModel> {
        return Single.create<AdminModel> { emitter ->
            val userEntityData = adminDao.getAdminData()
            try {
                if (userEntityData == null) emitter.onError(Throwable("Error of Getting UserInfo"))
                else emitter.onSuccess(userEntityData.getAdminModel())
            }catch (e : Exception){
            }
        }
    }

    fun saveAdminInfo(adminModelData : AdminModel) : Completable {
        return adminDao.insertAdminData(adminModelData.getAdminEntity())
    }

    fun deleteAdminInfo(adminId: String) : Completable {
        return Completable.create { emitter ->
            adminDao.deleteAdminData(adminId)
            emitter.onComplete()
        }
    }

    private val fireStore = FirebaseFirestore.getInstance()   //.getReference()

    private val _onSuccessGetSettingWaitingUsersList = SingleLiveEvent<List<User>>()
    private val onSuccessGetSettingWaitingUsersList: LiveData<List<User>> get() = _onSuccessGetSettingWaitingUsersList
    private val _onSuccessGetSettingAllowedUsersList = SingleLiveEvent<List<User>>()
    private val onSuccessGetSettingAllowedUsersList: LiveData<List<User>> get() = _onSuccessGetSettingAllowedUsersList

    fun checkAdminStatus(token: String): Single<Boolean> {
        return Single.create { emitter ->
            fireStore.collection(FIRESTORE_ADMINISTER).document(token)
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


    fun checkingAdminSignIn(adminId : String, adminPwd : String) : Single<ReceiverAdmin> {
        return Single.create{ emitter ->
            fireStore.collection(FIRESTORE_ADMINISTER).document(adminId)
                .get()
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "Found SignIn ID!!")
                    if (it.data != null && it.data!!["id"] == adminId && it.data!!["pwd"]==adminPwd){
                        Log.d(ContentValues.TAG, "SignIn Successed!!")
                        emitter.onSuccess(
                            ReceiverAdmin(true, AdminModel(it.data!!["agency"].toString(), it.data!!["id"].toString(), it.data!!["name"].toString(),
                                it.data!!["birthday"].toString(),it.data!!["smsInfo"].toString())
                            )
                        )
                    }
                    else emitter.onSuccess(ReceiverAdmin(false, null))
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents: ", exception)
                    emitter.onError(Throwable("Error getting SignIn-Info"))
                }
        }
    }
    fun findId(name: String, birthInfo : String, smsInfo : String) : Single<String> {
        return Single.create { emitter ->
            fireStore.collection(FIRESTORE_ADMINISTER)
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
                    emitter.onError(Throwable("Error getting CHECK_NICKNAME at ADMINISTER"))
                }
        }
    }

    fun findPwd(name: String, Id : String) : Single<String> {
        return Single.create { emitter ->
            fireStore.collection(FIRESTORE_ADMINISTER)
                .whereEqualTo("name", name)
                .whereEqualTo("id", Id)
                .get()
                .addOnSuccessListener {
                    if (it.isEmpty) emitter.onSuccess("")
                    else emitter.onSuccess(it.documents[0]["pwd"] as String)
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting Nickname duplicate info", exception)
                    emitter.onError(Throwable("Error getting CHECK_NICKNAME at ADMINISTER"))
                }
        }
    }

    fun changeAdminInfoPwd(token: String, userPwd : String) : Completable {
        return Completable.create{ emitter ->
            fireStore.collection(FIRESTORE_ADMINISTER).document(token)
                .update("pwd", userPwd)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "changeAdminInfo Successed!!")
                    emitter.onComplete()
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error todo changeAdminInfo: ", exception)
                    emitter.onError(Throwable("Error doing changeAdminInfo"))
                }
        }
    }

    fun getSettingsWaitingUserList(): LiveData<List<User>> {
        getSettingsWaitingUserListFromFirebase()
        return onSuccessGetSettingWaitingUsersList
    }

    private fun getSettingsWaitingUserListFromFirebase() {
        fireStore.collection(FIRESTORE_WAITING_USER_INFO)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(ContentValues.TAG, "Listen failed.", e)
                    return@addSnapshotListener }
                if (snapshot == null) _onSuccessGetSettingWaitingUsersList.postValue(emptyList())
                else {
                    val waitingUserList: MutableList<User> = mutableListOf()
                    for (document in snapshot) {
                        waitingUserList.add(
                            User(document.get("agency") as String,
                                document.get("id") as String,
                                document.get("pwd") as String,
                                document.get("name") as String,
                                document.get("nickname") as String,
                                document.get("birthday") as String,
                                document.get("smsInfo") as String,
                                document.get("allowed") as Boolean ))
                    }
                    _onSuccessGetSettingWaitingUsersList.postValue(waitingUserList)
                }
            }
    }

    fun getSettingsAllowedUserList() :  LiveData<List<User>> {
        getSettingsAllowedUserListFromFirebase()
        return onSuccessGetSettingAllowedUsersList
    }

    private fun getSettingsAllowedUserListFromFirebase() {
        fireStore.collection(FIRESTORE_ALLOWED_USER_INFO)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(ContentValues.TAG, "Listen failed.", e)
                    return@addSnapshotListener }
                if (snapshot == null) _onSuccessGetSettingAllowedUsersList.postValue(emptyList())
                else {
                    val waitingUserList: MutableList<User> = mutableListOf()
                    for (document in snapshot) {
                        waitingUserList.add(
                            User(document.get("agency") as String,
                                document.get("id") as String,
                                document.get("pwd") as String,
                                document.get("name") as String,
                                document.get("nickname") as String,
                                document.get("birthday") as String,
                                document.get("smsInfo") as String,
                                document.get("allowed") as Boolean ))
                    }
                    _onSuccessGetSettingAllowedUsersList.postValue(waitingUserList)
                }
            }
    }

    fun acceptWaitingUser(userdata: User) : Completable {
        return Completable.create{ emitter ->
            userdata.allowed = true
            fireStore.collection(FIRESTORE_WAITING_USER_INFO).document(userdata.id)
                .delete()
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents: ", exception)
                    emitter.onError(Throwable("Error getting documents: ", exception)) }
            fireStore.collection(FIRESTORE_ALLOWED_USER_INFO).document(userdata.id)
                .set(userdata)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "acceptWaitingUser Succeed!!")
                    emitter.onComplete()
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents: ", exception)
                    emitter.onError(Throwable("Error getting documents: ", exception))
                }
        }
    }
    fun deleteWaitingUser(userdata: User) : Completable {
        return Completable.create { emitter ->
            fireStore.collection(FIRESTORE_WAITING_USER_INFO).document(userdata.id)
                .delete()
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "deleteWaitingUser Succeed!!")
                    emitter.onComplete()
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents: ", exception)
                    emitter.onError(Throwable("Error getting documents: ", exception))
                }
        }
    }

    fun withdrawalUser(userdata: User) : Completable {
        return Completable.create { emitter ->
            fireStore.collection(FIRESTORE_ALLOWED_USER_INFO).document(userdata.id)
                .delete()
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "deleteUser Succeed!!")
                    emitter.onComplete()
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents: ", exception)
                    emitter.onError(Throwable("Error getting documents: ", exception))
                }
        }
    }

    fun withdrawalAdminInfo(adminId : String) : Completable {
        return Completable.create{ emitter ->
            fireStore.collection(FIRESTORE_ADMINISTER).document(adminId)
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