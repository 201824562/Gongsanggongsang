package com.example.adminapp.ui.main.reservation.detail

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.adminapp.ui.base.BaseSessionViewModel
import com.example.adminapp.data.model.*
import com.example.adminapp.data.repository.ReservationRepository
import com.example.adminapp.utils.SingleLiveEvent
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ReservationDetailFacilityViewModel (application: Application) : BaseSessionViewModel(application) {

    private val reservationRepository: ReservationRepository = ReservationRepository.getInstance()

    fun initDetailFacilityLiveData() {
        reservationRepository.initDetailFacilityLiveData()
    }

    fun getReservationFacilitySettingData(itemName : String) : LiveData<ReceiverFacilitySettingData> {
        //return reservationRepository.getReservationFacilitySettingData(agencyInfo, itemName)
        return this.getReservationFacilitySettingData(agencyInfo, itemName)
    }

    fun getReservationFacilityLogData(itemName : String): LiveData<ReceiverFacilityLog> {
        //return reservationRepository. getReservationFacilityLogData(agencyInfo, itemName)
        return this. getReservationFacilityLogData(agencyInfo, itemName)
    }

    fun getReservationFacilityLogDataList (itemType : String, itemName : String): LiveData<List<ReservationFacilityLog>> {
        //return reservationRepository.getReservationFacilityLogDataList(agencyInfo, itemType, itemName)
        return this.getReservationFacilityLogDataList(agencyInfo, itemType, itemName)
    }
    fun getReservationFacilityNotDoneLogDataList(itemType : String, itemName : String): LiveData<List<ReservationFacilityLog>> {
        //return reservationRepository.getReservationFacilityNotDoneLogDataList(agencyInfo, itemType, itemName)
        return this.getReservationFacilityNotDoneLogDataList(agencyInfo, itemType, itemName)
    }

    fun finishReservationFacilityLogData(logDocumentId : String){
        reservationRepository.makeReservationLogFinished(agencyInfo, logDocumentId)
    }

    fun cancelReservationFacilityLogData(logDocumentId : String){
        reservationRepository.makeReservationLogForcedCancel(agencyInfo, logDocumentId)
    }

    fun startReservationFacility(itemName : String){
        apiCall(reservationRepository.startReservationFacility(agencyInfo, itemName))
    }

    fun stopReservationFacility(itemName : String) {
        apiCall(reservationRepository.stopReservationFacility(agencyInfo, itemName ))
    }

    fun makeReservationLogFinished (documentId: String){
        reservationRepository.makeReservationLogFinished(agencyInfo, documentId)
    }


    //-------------------------------------------------------------------------------------------------------

    companion object{
        private val FIRESTORE_RESERVATION = "RESERVATION"
        private val FIRESTORE_RESERVATION_LOG = "LOG"
        private val FIRESTORE_RESERVATION_EQUIPMENT = "EQUIPMENT"
        private val FIRESTORE_RESERVATION_EQUIPMENT_SETTINGS = "EQUIPMENT_SETTINGS"
        private val FIRESTORE_RESERVATION_FACILITY = "FACILITY"
        private val FIRESTORE_RESERVATION_FACILITY_SETTINGS = "FACILITY_SETTINGS"
    }

    private val firestore = FirebaseFirestore.getInstance()
    private var _onSuccessGetReservationFacilitySettingData = MutableLiveData<ReceiverFacilitySettingData>()
    private val onSuccessGetReservationFacilitySettingData: LiveData<ReceiverFacilitySettingData> get() = _onSuccessGetReservationFacilitySettingData
    private var _onSuccessGetReservationFacilityLogData = SingleLiveEvent<ReceiverFacilityLog>()
    private val onSuccessGetReservationFacilityLogData: LiveData<ReceiverFacilityLog> get() = _onSuccessGetReservationFacilityLogData
    private var _onSuccessGetReservationFacilityDataList = SingleLiveEvent<List<ReservationFacilityLog>>()
    private val onSuccessGetReservationFacilityDataList : LiveData<List<ReservationFacilityLog>> get() = _onSuccessGetReservationFacilityDataList
    private var _onSuccessGetReservationFacilityNotDoneDataList = SingleLiveEvent<List<ReservationFacilityLog>>()
    private val onSuccessGetReservationFacilityNotDoneDataList : LiveData<List<ReservationFacilityLog>> get() = _onSuccessGetReservationFacilityNotDoneDataList


    private fun changeHashMapToReservationUnableTimeItem(it : HashMap<String, ReservationUnableTimeItem>) : ReservationUnableTimeItem{
        var timeType : ReservationUnableTimeType = ReservationUnableTimeType.HALF_HOUR
        if ((it["type"] as String) == "HOUR") timeType = ReservationUnableTimeType.HOUR
        val reservationTimeData : ReservationTimeData = (it["data"] as HashMap<String, ReservationTimeData>).let {
            ReservationTimeData(it["hour"] as Long, it["min"] as Long)
        }
        return ReservationUnableTimeItem(timeType, reservationTimeData, it["unable"] as Boolean)
    }

    fun getReservationFacilitySettingData(agency: String, itemName : String): LiveData<ReceiverFacilitySettingData> {
        getReservationFacilitySettingDataFromFirebase(agency, itemName)
        return onSuccessGetReservationFacilitySettingData }

    private fun getReservationFacilitySettingDataFromFirebase(agency: String, itemName : String) {
        firestore.collection(agency).document(FIRESTORE_RESERVATION)
            .collection(FIRESTORE_RESERVATION_FACILITY_SETTINGS).whereEqualTo("name", itemName)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(ContentValues.TAG, "Listen failed.", e)
                    _onSuccessGetReservationFacilitySettingData.postValue(ReceiverFacilitySettingData(false, null))
                    return@addSnapshotListener
                }
                when {
                    snapshot == null -> _onSuccessGetReservationFacilitySettingData.postValue(ReceiverFacilitySettingData(false, null))
                    snapshot.isEmpty ->  _onSuccessGetReservationFacilitySettingData.postValue(ReceiverFacilitySettingData(false, null))
                    else -> {
                        val it = snapshot.documents[0]
                        val unableTimeList = (it.get("unableTimeList") as ArrayList<HashMap<String,ReservationUnableTimeItem>>)
                            .map { hashMap -> changeHashMapToReservationUnableTimeItem(hashMap) }
                        _onSuccessGetReservationFacilitySettingData.postValue(
                            ReceiverFacilitySettingData(
                                true,
                                ReservationFacilitySettingData(
                                    CategoryResources.makeIconStringToDrawableID((it.get("icon") as String)),
                                    it.get("name") as String,
                                    it.get("intervalTime") as Long,
                                    it.get("maxTime") as Long,
                                    unableTimeList,
                                    it.get("usable") as Boolean
                                )
                            )
                        )
                    }
                }
            }
    }

    fun  getReservationFacilityLogData(agency: String, itemName : String): LiveData<ReceiverFacilityLog> {
        getReservationFacilityLogDataFromFirebase(agency, itemName)
        return onSuccessGetReservationFacilityLogData }

    private fun getReservationFacilityLogDataFromFirebase(agency: String, itemName : String) {
        firestore.collection(agency).document(FIRESTORE_RESERVATION)
            .collection(FIRESTORE_RESERVATION_LOG)
            .whereEqualTo("reservationType", "예약 사용").whereEqualTo("reservationState", "사용중")
            .whereEqualTo("name", itemName)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(ContentValues.TAG, "Listen failed.", e)
                    _onSuccessGetReservationFacilityLogData.postValue(ReceiverFacilityLog(false, null))
                    return@addSnapshotListener
                }
                when {
                    snapshot == null -> _onSuccessGetReservationFacilityLogData.postValue(ReceiverFacilityLog(false, null))
                    snapshot.isEmpty -> _onSuccessGetReservationFacilityLogData.postValue(ReceiverFacilityLog(false, null))
                    else -> {
                        val it = snapshot.documents[0]
                        _onSuccessGetReservationFacilityLogData.postValue(
                            ReceiverFacilityLog(
                                true,
                                ReservationFacilityLog(
                                    CategoryResources.makeIconStringToDrawableID((it.get("icon") as String)),
                                    it.get("name") as String,
                                    it.get("userId") as String,
                                    it.get("userName") as String,
                                    it.get("reservationState") as String,
                                    it.get("reservationType") as String,
                                    it.get("startTime") as String,
                                    it.get("endTime") as String,
                                    it.get("documentId") as String,
                                    it.getLong("maxTime")!!,
                                    it.get("usable") as Boolean
                                )
                            )
                        )
                    }
                }
            }
    }

    fun getReservationFacilityLogDataList(agency: String, itemType : String, itemName : String): LiveData<List<ReservationFacilityLog>> {
        getReservationFacilityLogDataListFromFirebase(agency, itemType, itemName)
        return onSuccessGetReservationFacilityDataList }

    private fun getReservationFacilityLogDataListFromFirebase(agency: String, itemType : String, itemName : String) {
        firestore.collection(agency).document(FIRESTORE_RESERVATION)
            .collection(FIRESTORE_RESERVATION_LOG)
            .whereEqualTo("reservationType", itemType).whereEqualTo("name", itemName)
            .orderBy("startTime", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(ContentValues.TAG, "Listen failed.", e)
                    _onSuccessGetReservationFacilityDataList.postValue(emptyList())
                    return@addSnapshotListener }
                if (snapshot == null) _onSuccessGetReservationFacilityDataList.postValue(emptyList())
                else {
                    val facilityList: MutableList<ReservationFacilityLog> = mutableListOf()
                    for (document in snapshot) {
                        facilityList.add(
                            ReservationFacilityLog(
                                CategoryResources.makeIconStringToDrawableID((document.get("icon") as String)),
                                document.get("name") as String,
                                document.get("userId") as String,
                                document.get("userName") as String,
                                document.get("reservationState") as String,
                                document.get("reservationType") as String,
                                document.get("startTime") as String,
                                document.get("endTime") as String,
                                document.get("documentId") as String,
                                document.getLong("maxTime")!!,
                                document.get("usable") as Boolean))
                    }
                    _onSuccessGetReservationFacilityDataList.postValue(facilityList)
                }
            }
    }

    fun getReservationFacilityNotDoneLogDataList(agency: String, itemType : String, itemName : String): LiveData<List<ReservationFacilityLog>> {
        getReservationFacilityNotDoneLogDataListFromFirebase(agency, itemType, itemName)
        Log.e("checking/in", "$itemType $itemName")
        return onSuccessGetReservationFacilityNotDoneDataList }

    private fun getReservationFacilityNotDoneLogDataListFromFirebase(agency: String, itemType : String, itemName : String) {
        firestore.collection(agency).document(FIRESTORE_RESERVATION)
            .collection(FIRESTORE_RESERVATION_LOG)
            .whereEqualTo("reservationType", itemType).whereEqualTo("name", itemName).whereIn("reservationState", listOf("사용중", "예약중"))
            .orderBy("startTime", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(ContentValues.TAG, "Listen failed.", e)
                    return@addSnapshotListener }
                if (snapshot == null) {
                    _onSuccessGetReservationFacilityNotDoneDataList.postValue(emptyList())
                }
                else {
                    val facilityLogList: MutableList<ReservationFacilityLog> = mutableListOf()
                    for (document in snapshot) {
                        facilityLogList.add(
                            ReservationFacilityLog(
                                CategoryResources.makeIconStringToDrawableID((document.get("icon") as String)),
                                document.get("name") as String,
                                document.get("userId") as String,
                                document.get("userName") as String,
                                document.get("reservationState") as String,
                                document.get("reservationType") as String,
                                document.get("startTime") as String,
                                document.get("endTime") as String,
                                document.get("documentId") as String,
                                document.getLong("maxTime")!!,
                                document.get("usable") as Boolean))
                    }
                    _onSuccessGetReservationFacilityNotDoneDataList.postValue(facilityLogList)
                }
            }
    }



}