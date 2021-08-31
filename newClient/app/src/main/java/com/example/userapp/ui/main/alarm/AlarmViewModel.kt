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

class AlarmViewModel(application: Application) : BaseSessionViewModel(application)  {

    private val firestore = FirebaseFirestore.getInstance()
    private val FIRESTORE_ALARM ="ALARM"
    private val alarmRepository: AlarmRepository = AlarmRepository.getInstance(AppDatabase.getDatabase(application, viewModelScope))

    private val _onSuccessGetAlarmAllList = SingleLiveEvent<List<AlarmItem>>()
    private val onSuccessGetAlarmAllList: LiveData<List<AlarmItem>> get() = _onSuccessGetAlarmAllList
    private val _onSuccessGetAlarmNoticeList = SingleLiveEvent<List<AlarmItem>>()
    private val onSuccessGetAlarmNoticeList: LiveData<List<AlarmItem>> get() = _onSuccessGetAlarmNoticeList
    private val _onSuccessGetAlarmReservationList = SingleLiveEvent<List<AlarmItem>>()
    private val onSuccessGetAlarmReservationList: LiveData<List<AlarmItem>> get() = _onSuccessGetAlarmReservationList
    private val _onSuccessGetAlarmEmergencyList = SingleLiveEvent<List<AlarmItem>>()
    private val onSuccessGetAlarmEmergencyList: LiveData<List<AlarmItem>> get() = _onSuccessGetAlarmEmergencyList
    private val _onSuccessGetAlarmTogetherList = SingleLiveEvent<List<AlarmItem>>()
    private val onSuccessGetAlarmTogetherList: LiveData<List<AlarmItem>> get() = _onSuccessGetAlarmTogetherList
    private val _onSuccessGetAlarmSuggestList = SingleLiveEvent<List<AlarmItem>>()
    private val onSuccessGetAlarmSuggestList: LiveData<List<AlarmItem>> get() = _onSuccessGetAlarmSuggestList
    private val _onSuccessGetAlarmMarketList = SingleLiveEvent<List<AlarmItem>>()
    private val onSuccessGetAlarmMarketList: LiveData<List<AlarmItem>> get() = _onSuccessGetAlarmMarketList


    fun getAlarmAllList(): LiveData<List<AlarmItem>> {
        getAlarmAllListFromFireBase()
        return onSuccessGetAlarmAllList
    }

    private fun getAlarmAllListFromFireBase(){
        firestore.collection(agencyInfo).document(FIRESTORE_ALARM)
            .collection(authToken).addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(ContentValues.TAG, "Listen failed.", e)
                    _onSuccessGetAlarmAllList.postValue(emptyList())
                    return@addSnapshotListener }
                if (snapshot == null) _onSuccessGetAlarmAllList.postValue(emptyList())
                else {
                    val alarmList: MutableList<AlarmItem> = mutableListOf()
                    for (document in snapshot) {
                        val type = makeStringToEnumData((document.get("type") as String))
                        when (type){
                            AlarmType.RESERVATION ->{
                                val reservationData : ReservationAlarmData= (document.get("reservationData")  as HashMap<String, ReservationAlarmData>).let {
                                    ReservationAlarmData(it["documentId"] as String, it["name"] as String,
                                        it["startTime"] as String, it["endTime"] as String) }
                                alarmList.add(
                                    AlarmItem(document.get("documentId") as String,
                                        document.get("time") as String,
                                        document.get("userId") as String,
                                        document.get("message") as String,
                                        makeStringToEnumData((document.get("type") as String)),
                                        reservationData, null)) }
                            else -> {
                                val postAlarmData : PostAlarmData = (document.get("postData") as HashMap<String, PostAlarmData>).let {
                                    PostAlarmData(
                                        it["post_category"] as String, it["post_name"] as String, it["post_title"] as String,
                                        it["post_contents"] as String, it["post_date"] as String, it["post_time"] as String,
                                        it["post_comments"] as Long, it["post_id"] as String, it["post_photo_uri"] as ArrayList<String>,
                                        it["post_state"] as String, it["post_anonymous"] as Boolean) }
                                alarmList.add(
                                    AlarmItem(document.get("documentId") as String,
                                        document.get("time") as String,
                                        document.get("userId") as String,
                                        document.get("message") as String,
                                        makeStringToEnumData((document.get("type") as String)),
                                        null, postAlarmData))
                            }
                        }
                    }
                    _onSuccessGetAlarmAllList.postValue(alarmList)
                }
            }
    }

}