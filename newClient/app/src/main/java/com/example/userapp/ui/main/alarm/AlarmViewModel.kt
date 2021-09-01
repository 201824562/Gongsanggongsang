package com.example.userapp.ui.main.alarm

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.userapp.base.BaseSessionViewModel
import com.example.userapp.data.AppDatabase
import com.example.userapp.data.model.AlarmItem
import com.example.userapp.data.model.AlarmType
import com.example.userapp.data.model.AlarmType.Companion.makeStringToEnumData
import com.example.userapp.data.model.PostAlarmData
import com.example.userapp.data.model.ReservationAlarmData
import com.example.userapp.data.repository.AlarmRepository
import com.example.userapp.utils.SingleLiveEvent
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
    private val _onSuccessGetAlarmCommunityList = SingleLiveEvent<List<AlarmItem>>()
    private val onSuccessGetAlarmCommunityList: LiveData<List<AlarmItem>> get() = _onSuccessGetAlarmCommunityList
    private val _onSuccessGetAlarmReservationList = SingleLiveEvent<List<AlarmItem>>()
    private val onSuccessGetAlarmReservationList: LiveData<List<AlarmItem>> get() = _onSuccessGetAlarmReservationList


    fun makeReservationLogUsing(documentId: String){ alarmRepository.makeReservationLogUsing(agencyInfo, documentId) }
    fun makeReservationLogCancel(documentId: String) { alarmRepository.makeReservationLogCancel(agencyInfo, documentId) }
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
                    _onSuccessGetAlarmAllList.postValue(emptyList())
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
                                        null, postAlarmData, null))
                            }
                            AlarmType.RESERVATION ->{
                                if (document.get("reservationData") == null){
                                    alarmList.add(
                                        AlarmItem(document.get("documentId") as String,
                                            document.get("time") as String,
                                            document.get("otherUser") as String,
                                            document.get("message") as String,
                                            document.get("type") as String,
                                            null, null, null))
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
                                            reservationData, null, null))
                                }
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
                                        null, postAlarmData, null))
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
                    _onSuccessGetAlarmNoticeList.postValue(emptyList())
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

    fun getAlarmCommunityList(): LiveData<List<AlarmItem>> {
        getAlarmCommunityListFromFireBase()
        return onSuccessGetAlarmCommunityList
    }

    private fun getAlarmCommunityListFromFireBase(){
        firestore.collection(agencyInfo).document(FIRESTORE_ALARM)
            .collection(authToken).whereIn("type", listOf("함께", "건의", "장터")).orderBy("time", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(ContentValues.TAG, "Listen failed.", e)
                    _onSuccessGetAlarmCommunityList.postValue(emptyList())
                    return@addSnapshotListener }
                if (snapshot == null) _onSuccessGetAlarmCommunityList.postValue(emptyList())
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
                    _onSuccessGetAlarmCommunityList.postValue(alarmList)
                }
            }
    }

    fun getAlarmReservationList(): LiveData<List<AlarmItem>> {
        getAlarmReservationListFromFireBase()
        return onSuccessGetAlarmReservationList
    }

    private fun getAlarmReservationListFromFireBase(){
        firestore.collection(agencyInfo).document(FIRESTORE_ALARM)
            .collection(authToken).whereIn("type", listOf("공용")).orderBy("time", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(ContentValues.TAG, "Listen failed.", e)
                    _onSuccessGetAlarmReservationList.postValue(emptyList())
                    return@addSnapshotListener }
                if (snapshot == null) _onSuccessGetAlarmReservationList.postValue(emptyList())
                else {
                    val alarmList: MutableList<AlarmItem> = mutableListOf()
                    for (document in snapshot) {
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
                    _onSuccessGetAlarmReservationList.postValue(alarmList)
                }
            }
    }

}