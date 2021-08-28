package com.example.adminapp.ui.main.reservation.detail

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.adminapp.base.BaseSessionViewModel
import com.example.adminapp.data.model.*
import com.example.adminapp.data.repository.ReservationRepository
import com.example.adminapp.utils.SingleLiveEvent
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ReservationDetailEquipmentViewModel(application: Application) : BaseSessionViewModel(application) {

    private val reservationRepository: ReservationRepository = ReservationRepository.getInstance()

    private val _onSuccessGettingReserveEquipmentSettingData = SingleLiveEvent<ReservationEquipmentSettingData>()
    val onSuccessGettingReserveEquipmentSettingData : LiveData<ReservationEquipmentSettingData> get() = _onSuccessGettingReserveEquipmentSettingData

    fun initDetailEquipmentLiveData() {
        reservationRepository.initDetailEquipmentLiveData()
    }

    fun getReservationEquipmentSettingData(itemName : String)  {
        apiCall(reservationRepository.getReservationEquipmentSettingData(agencyInfo, itemName), {
            _onSuccessGettingReserveEquipmentSettingData.postValue(it)
        })
    }

    fun getReservationEquipmentData(itemName : String): LiveData<ReceiverEquipment> {
        //return reservationRepository.getReservationEquipmentData(agencyInfo, itemName)
        return this.getReservationEquipmentData(agencyInfo, itemName)
    }

    fun getReservationEquipmentLogData(itemType : String, itemName : String): LiveData<List<ReservationEquipmentLog>> {
        //return reservationRepository.getReservationEquipmentLogDataList(agencyInfo, itemType, itemName)
        return this.getReservationEquipmentLogDataList(agencyInfo, itemType, itemName)
    }

    fun startReservationEquipment(itemName : String){
        apiCall(reservationRepository.startReservationEquipment(agencyInfo, itemName))
    }

    fun stopReservationEquipment(itemName : String){
        reservationRepository.stopReservationEquipment(false, agencyInfo, itemName)
    }

    fun cancelReservationEquipment(documentId : String)  {
        reservationRepository.makeReservationLogForcedCancel(agencyInfo, documentId)
    }

    fun makeReservationLogFinished (documentId: String){
        reservationRepository.makeReservationLogFinished(agencyInfo, documentId)
    }


    //-----------------------------------------------------------------------------------------------------------

    companion object{
        private val FIRESTORE_RESERVATION = "RESERVATION"
        private val FIRESTORE_RESERVATION_LOG = "LOG"
        private val FIRESTORE_RESERVATION_EQUIPMENT = "EQUIPMENT"
    }

    private val firestore = FirebaseFirestore.getInstance()
    private var _onSuccessGetReservationEquipmentData = SingleLiveEvent<ReceiverEquipment>()
    private val onSuccessGetReservationEquipmentData: LiveData<ReceiverEquipment> get() = _onSuccessGetReservationEquipmentData
    private var _onSuccessGetReservationEquipmentLogData = SingleLiveEvent<List<ReservationEquipmentLog>>()
    private val onSuccessGetReservationEquipmentLogData: LiveData<List<ReservationEquipmentLog>> get() = _onSuccessGetReservationEquipmentLogData

    private fun getReservationEquipmentData(agency: String, itemName : String): LiveData<ReceiverEquipment> {
        getReservationEquipmentDataFromFirebase(agency, itemName)
        return onSuccessGetReservationEquipmentData }

    private fun getReservationEquipmentDataFromFirebase(agency: String, itemName : String) {
        firestore.collection(agency).document(FIRESTORE_RESERVATION)
            .collection(FIRESTORE_RESERVATION_EQUIPMENT).whereEqualTo("name", itemName)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(ContentValues.TAG, "Listen failed.", e)
                    _onSuccessGetReservationEquipmentData.postValue(ReceiverEquipment(false, null))
                    return@addSnapshotListener }
                when {
                    snapshot == null -> _onSuccessGetReservationEquipmentData.postValue(ReceiverEquipment(false, null))
                    snapshot.isEmpty -> _onSuccessGetReservationEquipmentData.postValue(ReceiverEquipment(false, null))
                    else -> { val it = snapshot.documents[0]
                        _onSuccessGetReservationEquipmentData.postValue(
                            ReceiverEquipment(
                                true,
                                ReservationEquipmentData(
                                    CategoryResources.makeIconStringToDrawableID((it.get("icon") as String)),
                                    it.get("name") as String,
                                    it.get("user") as String,
                                    it.get("startTime") as String,
                                    it.get("endTime") as String,
                                    it.getLong("intervalTime")!!,
                                    it.get("using") as Boolean,
                                    it.get("usable") as Boolean,
                                    it.getLong("maxTime")!!,
                                    it.get("documentId") as String
                                )
                            )
                        )
                    }
                }
            }
    }

    fun getReservationEquipmentLogDataList(agency: String, itemType : String, itemName : String) :  LiveData<List<ReservationEquipmentLog>> {
        getReservationEquipmentLogDataFromFirebase(agency, itemType, itemName)
        return onSuccessGetReservationEquipmentLogData
    }

    private fun getReservationEquipmentLogDataFromFirebase(agency: String, itemType : String, itemName : String) {
        firestore.collection(agency).document(FIRESTORE_RESERVATION).collection(FIRESTORE_RESERVATION_LOG)
            .whereEqualTo("reservationType", itemType).whereEqualTo("name", itemName)
            .orderBy("startTime", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(ContentValues.TAG, "Listen failed.", e)
                    _onSuccessGetReservationEquipmentLogData.postValue(emptyList())
                    return@addSnapshotListener }
                if (snapshot == null) _onSuccessGetReservationEquipmentLogData.postValue(emptyList())
                else {
                    val equipmentLogList: MutableList<ReservationEquipmentLog> = mutableListOf()
                    for (document in snapshot) {
                        equipmentLogList.add(
                            ReservationEquipmentLog(
                                CategoryResources.makeIconStringToDrawableID((document.get("icon") as String)),
                                document.get("name") as String,
                                document.get("userId") as String,
                                document.get("userName") as String,
                                document.get("reservationState") as String,
                                document.get("reservationType") as String,
                                document.get("startTime") as String,
                                document.get("endTime") as String,
                                document.get("documentId") as String))
                    }
                    _onSuccessGetReservationEquipmentLogData.postValue(equipmentLogList)
                }
            }
    }


}
