package com.example.adminapp.ui.main.alarm

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.adminapp.base.BaseSessionViewModel
import com.example.adminapp.base.BaseViewModel
import com.example.adminapp.data.AppDatabase
import com.example.adminapp.data.model.*
import com.example.adminapp.data.model.AlarmType.Companion.makeStringToEnumData
import com.example.adminapp.data.repository.AlarmRepository
import com.example.adminapp.utils.SingleLiveEvent
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class AlarmViewModel(application: Application) : BaseSessionViewModel(application)  {

    private val firestore = FirebaseFirestore.getInstance()
    private val FIRESTORE_ALARM ="ALARM"
    private val alarmRepository: AlarmRepository = AlarmRepository.getInstance(AppDatabase.getDatabase(application, viewModelScope))

    private val _onSuccessGetAlarmAllList = SingleLiveEvent<List<AlarmItem>>()
    private val onSuccessGetAlarmAllList: LiveData<List<AlarmItem>> get() = _onSuccessGetAlarmAllList
    private val _onSuccessGetAlarmNoticeList = SingleLiveEvent<List<AlarmItem>>()
    private val onSuccessGetAlarmNoticeList: LiveData<List<AlarmItem>> get() = _onSuccessGetAlarmNoticeList
    private val _onSuccessGetAlarmSuggestList = SingleLiveEvent<List<AlarmItem>>()
    private val onSuccessGetAlarmSuggestList: LiveData<List<AlarmItem>> get() = _onSuccessGetAlarmSuggestList
    private val _onSuccessGetAlarmAllowList = SingleLiveEvent<List<AlarmItem>>()
    private val onSuccessGetAlarmAllowList: LiveData<List<AlarmItem>> get() = _onSuccessGetAlarmAllowList
    private val _onSuccessGetAlarmOutList = SingleLiveEvent<List<AlarmItem>>()
    private val onSuccessGetAlarmOutList: LiveData<List<AlarmItem>> get() = _onSuccessGetAlarmOutList

    fun allowWaitingUser(userData : User){ apiCall(adminRepository.acceptWaitingUser(userData)) }
    fun deleteWaitingUser(userData: User) { apiCall(adminRepository.deleteWaitingUser(userData))}
    fun makeAlarmItemClickUnable(myToken : String, documentId: String) {
        alarmRepository.makeAlarmDataClickUnable(agencyInfo, myToken, documentId ) }

    fun getAlarmAllList(): LiveData<List<AlarmItem>> {
        getAlarmAllListFromFireBase()
        return onSuccessGetAlarmAllList
    }

    private fun getAlarmAllListFromFireBase(){
        firestore.collection(agencyInfo).document(FIRESTORE_ALARM)
            .collection(authToken).orderBy("time", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(ContentValues.TAG, "Listen failed.", e)
                    //_onSuccessGetAlarmAllList.postValue(emptyList())
                    return@addSnapshotListener }
                if (snapshot == null) _onSuccessGetAlarmAllList.postValue(emptyList())
                else {
                    val alarmList: MutableList<AlarmItem> = mutableListOf()
                    for (document in snapshot) {
                        when (makeStringToEnumData((document.get("type") as String))){
                            AlarmType.OUT -> {
                                val postAlarmData : PostAlarmData = (document.get("postData") as HashMap<String, PostAlarmData>).let {
                                    PostAlarmData(
                                        it["post_category"] as String, it["post_name"] as String, it["post_title"] as String,
                                        it["post_contents"] as String, it["post_date"] as String, it["post_time"] as String,
                                        it["post_comments"] as Long, it["post_id"] as String, it["post_photo_uri"] as ArrayList<String>,
                                        it["post_state"] as String, it["post_anonymous"] as Boolean) }
                                alarmList.add(
                                    AlarmItem(document.get("documentId") as String,
                                        document.get("time") as String,
                                        document.get("otherUser") as String,
                                        document.get("message") as String,
                                        document.get("type") as String,
                                        null, postAlarmData, null,
                                        document.get("clicked") as Boolean))
                            }
                            AlarmType.RESERVATION ->{
                                if (document.get("reservationData") == null){
                                    alarmList.add(
                                        AlarmItem(document.get("documentId") as String,
                                            document.get("time") as String,
                                            document.get("otherUser") as String,
                                            document.get("message") as String,
                                            document.get("type") as String,
                                            null, null, null,
                                            document.get("clicked") as Boolean))
                                }else {
                                    val reservationData : ReservationAlarmData= (document.get("reservationData")  as HashMap<String, ReservationAlarmData>).let {
                                        ReservationAlarmData(it["documentId"] as String, it["name"] as String,
                                            it["startTime"] as String, it["endTime"] as String) }
                                    alarmList.add(
                                        AlarmItem(document.get("documentId") as String,
                                            document.get("time") as String,
                                            document.get("otherUser") as String,
                                            document.get("message") as String,
                                            document.get("type") as String,
                                            reservationData, null, null,
                                            document.get("clicked") as Boolean))
                                }
                            }
                            AlarmType.SIGNUP -> {
                                val signUpData : SignUpAlarmData= (document.get("signData") as HashMap<String,SignUpAlarmData>).let {
                                    SignUpAlarmData(it["agency"] as String, it["id"] as String, it["pwd"] as String,
                                        it["name"] as String, it["nickname"] as String, it["birthday"] as String, it["smsInfo"] as String, it["allowed"] as Boolean) }
                                alarmList.add(
                                    AlarmItem(document.get("documentId") as String,
                                        document.get("time") as String,
                                        document.get("otherUser") as String,
                                        document.get("message") as String,
                                        document.get("type") as String,
                                        null, null, signUpData,
                                        document.get("clicked") as Boolean))
                            }else -> {
                                val postAlarmData : PostAlarmData = (document.get("postData") as HashMap<String, PostAlarmData>).let {
                                    PostAlarmData(
                                        it["post_category"] as String, it["post_name"] as String, it["post_title"] as String,
                                        it["post_contents"] as String, it["post_date"] as String, it["post_time"] as String,
                                        it["post_comments"] as Long, it["post_id"] as String, it["post_photo_uri"] as ArrayList<String>,
                                        it["post_state"] as String, it["post_anonymous"] as Boolean) }
                                alarmList.add(
                                    AlarmItem(document.get("documentId") as String,
                                        document.get("time") as String,
                                        document.get("otherUser") as String,
                                        document.get("message") as String,
                                        document.get("type") as String,
                                        null, postAlarmData, null,
                                        document.get("clicked") as Boolean))
                            }
                        }
                    }
                    _onSuccessGetAlarmAllList.postValue(alarmList)
                }
            }
    }

    fun getAlarmNoticeList(): LiveData<List<AlarmItem>> {
        getAlarmNoticeListFromFireBase()
        return onSuccessGetAlarmNoticeList
    }

    private fun getAlarmNoticeListFromFireBase(){
        firestore.collection(agencyInfo).document(FIRESTORE_ALARM)
            .collection(authToken).whereIn("type", listOf("공지", "긴급")).orderBy("time", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(ContentValues.TAG, "Listen failed.", e)
                    //_onSuccessGetAlarmNoticeList.postValue(emptyList())
                    return@addSnapshotListener }
                if (snapshot == null) _onSuccessGetAlarmNoticeList.postValue(emptyList())
                else {
                    val alarmList: MutableList<AlarmItem> = mutableListOf()
                    for (document in snapshot) {
                        val postAlarmData : PostAlarmData = (document.get("postData") as HashMap<String, PostAlarmData>).let {
                            PostAlarmData(
                                it["post_category"] as String, it["post_name"] as String, it["post_title"] as String,
                                it["post_contents"] as String, it["post_date"] as String, it["post_time"] as String,
                                it["post_comments"] as Long, it["post_id"] as String, it["post_photo_uri"] as ArrayList<String>,
                                it["post_state"] as String, it["post_anonymous"] as Boolean) }
                        alarmList.add(
                            AlarmItem(document.get("documentId") as String,
                                document.get("time") as String,
                                document.get("otherUser") as String,
                                document.get("message") as String,
                                document.get("type") as String,
                                null, postAlarmData, null,
                                document.get("clicked") as Boolean))
                    }
                    _onSuccessGetAlarmNoticeList.postValue(alarmList)
                }
            }
    }

    fun getAlarmSuggestList(): LiveData<List<AlarmItem>> {
        getAlarmSuggestListFromFireBase()
        return onSuccessGetAlarmSuggestList
    }

    private fun getAlarmSuggestListFromFireBase(){
        firestore.collection(agencyInfo).document(FIRESTORE_ALARM)
            .collection(authToken).whereEqualTo("type", "건의").orderBy("time", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(ContentValues.TAG, "Listen failed.", e)
                    //_onSuccessGetAlarmSuggestList.postValue(emptyList())
                    return@addSnapshotListener }
                if (snapshot == null) _onSuccessGetAlarmSuggestList.postValue(emptyList())
                else {
                    val alarmList: MutableList<AlarmItem> = mutableListOf()
                    for (document in snapshot) {
                        val postAlarmData : PostAlarmData = (document.get("postData") as HashMap<String, PostAlarmData>).let {
                            PostAlarmData(
                                it["post_category"] as String, it["post_name"] as String, it["post_title"] as String,
                                it["post_contents"] as String, it["post_date"] as String, it["post_time"] as String,
                                it["post_comments"] as Long, it["post_id"] as String, it["post_photo_uri"] as ArrayList<String>,
                                it["post_state"] as String, it["post_anonymous"] as Boolean) }
                        alarmList.add(
                            AlarmItem(document.get("documentId") as String,
                                document.get("time") as String,
                                document.get("otherUser") as String,
                                document.get("message") as String,
                                document.get("type") as String,
                                null, postAlarmData, null,
                                document.get("clicked") as Boolean))
                    }
                    _onSuccessGetAlarmSuggestList.postValue(alarmList)
                }
            }
    }

    fun getAlarmAllowList(): LiveData<List<AlarmItem>> {
        getAlarmAllowListFromFireBase()
        return onSuccessGetAlarmAllowList
    }

    private fun getAlarmAllowListFromFireBase(){
        firestore.collection(agencyInfo).document(FIRESTORE_ALARM)
            .collection(authToken).whereEqualTo("type", "가입승인").orderBy("time", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(ContentValues.TAG, "Listen failed.", e)
                    //_onSuccessGetAlarmAllowList.postValue(emptyList())
                    return@addSnapshotListener }
                if (snapshot == null) _onSuccessGetAlarmAllowList.postValue(emptyList())
                else {
                    val alarmList: MutableList<AlarmItem> = mutableListOf()
                    for (document in snapshot) {
                        val signUpData : SignUpAlarmData= (document.get("signData") as HashMap<String,SignUpAlarmData>).let {
                            SignUpAlarmData(it["agency"] as String, it["id"] as String, it["pwd"] as String,
                                it["name"] as String, it["nickname"] as String, it["birthday"] as String, it["smsInfo"] as String, it["allowed"] as Boolean) }
                        alarmList.add(
                            AlarmItem(document.get("documentId") as String,
                                document.get("time") as String,
                                document.get("otherUser") as String,
                                document.get("message") as String,
                                document.get("type") as String,
                                null, null, signUpData,
                                document.get("clicked") as Boolean))
                    }
                    _onSuccessGetAlarmAllowList.postValue(alarmList)
                }
            }
    }



    fun getAlarmOutList(): LiveData<List<AlarmItem>> {
        getAlarmOutListFromFireBase()
        return onSuccessGetAlarmOutList
    }

    private fun getAlarmOutListFromFireBase(){
        firestore.collection(agencyInfo).document(FIRESTORE_ALARM)
            .collection(authToken).whereEqualTo("type", "퇴실").orderBy("time", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(ContentValues.TAG, "Listen failed.", e)
                    //_onSuccessGetAlarmOutList.postValue(emptyList())
                    return@addSnapshotListener }
                if (snapshot == null) _onSuccessGetAlarmOutList.postValue(emptyList())
                else {
                    val alarmList: MutableList<AlarmItem> = mutableListOf()
                    for (document in snapshot) {
                        val postAlarmData : PostAlarmData = (document.get("postData") as HashMap<String, PostAlarmData>).let {
                            PostAlarmData(
                                it["post_category"] as String, it["post_name"] as String, it["post_title"] as String,
                                it["post_contents"] as String, it["post_date"] as String, it["post_time"] as String,
                                it["post_comments"] as Long, it["post_id"] as String, it["post_photo_uri"] as ArrayList<String>,
                                it["post_state"] as String, it["post_anonymous"] as Boolean) }
                        alarmList.add(
                            AlarmItem(document.get("documentId") as String,
                                document.get("time") as String,
                                document.get("otherUser") as String,
                                document.get("message") as String,
                                document.get("type") as String,
                                null, postAlarmData, null,
                                document.get("clicked") as Boolean))
                    }
                    _onSuccessGetAlarmOutList.postValue(alarmList)
                }
            }
    }


}