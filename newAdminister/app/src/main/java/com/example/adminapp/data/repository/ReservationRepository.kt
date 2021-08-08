package com.example.adminapp.data.repository

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.adminapp.data.model.*
import com.example.adminapp.utils.SingleLiveEvent
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable
import io.reactivex.Single

class ReservationRepository() {

    companion object {
        private var sInstance: ReservationRepository ? = null
        fun getInstance(): ReservationRepository {
            return sInstance
                ?: synchronized(this) {
                    val instance = ReservationRepository()
                    sInstance = instance
                    instance
                }
        }

        private val FIRESTORE_RESERVATION = "RESERVATION"
        private val FIRESTORE_RESERVATION_EQUIPMENT = "EQUIPMENT"
        private val FIRESTORE_RESERVATION_EQUIPMENT_SETTINGS = "EQUIPMENT_SETTINGS"
        private val FIRESTORE_RESERVATION_FACILITY = "FACILITY"
        private val FIRESTORE_RESERVATION_FACILITY_SETTINGS = "FACILITY_SETTINGS"
    }

    private val firestore = FirebaseFirestore.getInstance()

    private val _onSuccessGetReservationEquipmentDataList = SingleLiveEvent<List<ReservationEquipmentData>>()
    private val onSuccessGetReservationEquipmentDataList: LiveData<List<ReservationEquipmentData>> get() = _onSuccessGetReservationEquipmentDataList
    private val _onSuccessGetReservationFacilityDataList = SingleLiveEvent<List<ReservationFacilityData>>()
    private val onSuccessGetReservationFacilityDataList: LiveData<List<ReservationFacilityData>> get() = _onSuccessGetReservationFacilityDataList
    private val _onSuccessGetReservationEquipmentSettingDataList = SingleLiveEvent<List<ReservationEquipmentSettingData>>()
    private val onSuccessGetReservationEquipmentSettingDataList: LiveData<List<ReservationEquipmentSettingData>> get() = _onSuccessGetReservationEquipmentSettingDataList
    private val _onSuccessGetReservationFacilitySettingDataList = SingleLiveEvent<List<ReservationFacilitySettingData>>()
    private val onSuccessGetReservationFacilitySettingDataList: LiveData<List<ReservationFacilitySettingData>> get() = _onSuccessGetReservationFacilitySettingDataList


    fun getReservationEquipmentData(agency: String, itemName : String): Single<ReservationEquipmentData> {
        return Single.create { emitter ->
            firestore.collection(agency).document(FIRESTORE_RESERVATION)
                .collection(FIRESTORE_RESERVATION_EQUIPMENT)
                .document(itemName).get()
                .addOnSuccessListener {
                    if (it.data != null && it.get("name") == itemName) {
                        emitter.onSuccess(
                            ReservationEquipmentData(
                                it.getLong("icon")!!.toInt(),
                                it.get("name") as String,
                                it.get("user") as String,
                                it.get("startTime") as String,
                                it.get("endTime") as String,
                                it.getLong("intervalTime")!!,
                                it.get("using") as Boolean,
                                it.get("usable") as Boolean
                            )
                        )
                    } else emitter.onError(Throwable("Error getting EQUIPMENT of $itemName"))
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                    emitter.onError(Throwable("Error getting EQUIPMENT of $itemName"))
                }
        }
    }

    fun getReservationEquipmentSettingData(agency: String, itemName : String): Single<ReservationEquipmentSettingData> {
        return Single.create { emitter ->
            firestore.collection(agency).document(FIRESTORE_RESERVATION)
                .collection(FIRESTORE_RESERVATION_EQUIPMENT_SETTINGS)
                .document(itemName).get()
                .addOnSuccessListener {
                    if (it.data != null && it.get("name") == itemName) {
                        emitter.onSuccess(
                            ReservationEquipmentSettingData(
                                it.getLong("icon")!!.toInt(),
                                it.get("name") as String,
                                it.get("maxTime") as Long
                            )
                        )
                    } else emitter.onError(Throwable("Error getting EQUIPMENT_SETTINGS of $itemName"))
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                    emitter.onError(Throwable("Error getting EQUIPMENT_SETTINGS of $itemName"))
                }
        }
    }

    fun getReservationEquipmentDataList(agency: String): LiveData<List<ReservationEquipmentData>> {
        getReservationEquipmentDataListFromFirebase(agency)
        return onSuccessGetReservationEquipmentDataList }

    private fun getReservationEquipmentDataListFromFirebase(agency: String) {
        firestore.collection(agency).document(FIRESTORE_RESERVATION)
            .collection(FIRESTORE_RESERVATION_EQUIPMENT)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener }
                if (snapshot == null) _onSuccessGetReservationEquipmentDataList.postValue(emptyList())
                else {
                    val equipmentList: MutableList<ReservationEquipmentData> = mutableListOf()
                    for (document in snapshot) {
                        var startTime : String = ""
                        var endTime : String = ""
                        if (document.get("startTime") != null) startTime=(document.get("startTime") as String)
                        if (document.get("endTime") != null) endTime = (document.get("endTime") as String)
                        equipmentList.add(
                            ReservationEquipmentData(
                                document.getLong("icon")!!.toInt(),
                                document.get("name") as String,
                                document.get("user") as String,
                                startTime,
                                endTime,
                                document.getLong("intervalTime")!!,
                                document.getBoolean("using")!!,
                                document.getBoolean("usable")!!))
                    }
                    _onSuccessGetReservationEquipmentDataList.postValue(equipmentList)
                }
            }
    }

    //TODO : 이거 걍 답이 없는데?
   /* fun getReservationFacilityDataList(agency: String): LiveData<List<ReservationFacilityData>> {
        getReservationFacilityDataListFromFirebase(agency)
        return onSuccessGetReservationFacilityDataList }

    private fun getReservationFacilityDataListFromFirebase(agency: String) {
        firestore.collection(agency).document(FIRESTORE_RESERVATION)
            .collection(FIRESTORE_RESERVATION_FACILITY)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener }
                if (snapshot == null) _onSuccessGetReservationFacilityDataList.postValue(emptyList())
                else {
                    val facilityList: MutableList<ReservationFacilityData> = mutableListOf()
                    for (document in snapshot) {
                        val unableTimeList = (document.get("unableTimeList") as ArrayList<HashMap<String,ReservationUnableTimeItem>>).map {
                            changeHashMapToReservationUnableTimeItem(it)
                        }
                        facilityList.add(
                            //TODO : 아이콘 추가 필요.
                            ReservationFacilityData(
                                document.getLong("icon")!!.toInt(),
                                document.get("name") as String,
                                document.get("intervalTime") as Long,
                                document.get("maxTime") as Long,
                                unableTimeList ))
                    }
                    _onSuccessGetReservationFacilitySettingDataList.postValue(facilityList)
                }
            }
    }*/

    fun getReservationEquipmentSettingDataList(agency: String): LiveData<List<ReservationEquipmentSettingData>> {
        getReservationEquipmentSettingDataListFromFirebase(agency)
        return onSuccessGetReservationEquipmentSettingDataList }

    private fun getReservationEquipmentSettingDataListFromFirebase(agency: String) {
        firestore.collection(agency).document(FIRESTORE_RESERVATION)
            .collection(FIRESTORE_RESERVATION_EQUIPMENT_SETTINGS)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener }
                if (snapshot == null) _onSuccessGetReservationEquipmentSettingDataList.postValue(emptyList())
                else {
                    val equipmentSettingList: MutableList<ReservationEquipmentSettingData> = mutableListOf()
                    for (document in snapshot) {
                        equipmentSettingList.add(
                            ReservationEquipmentSettingData(
                                document.getLong("icon")!!.toInt(),
                                document.get("name") as String,
                                document.get("maxTime") as Long)
                        )
                    }
                    _onSuccessGetReservationEquipmentSettingDataList.postValue(equipmentSettingList)
                }
            }
    }

    fun getReservationFacilitySettingDataList(agency: String): LiveData<List<ReservationFacilitySettingData>> {
        getReservationFacilitySettingDataListFromFirebase(agency)
        return onSuccessGetReservationFacilitySettingDataList }

    private fun getReservationFacilitySettingDataListFromFirebase (agency: String) {
        firestore.collection(agency).document(FIRESTORE_RESERVATION)
            .collection(FIRESTORE_RESERVATION_FACILITY_SETTINGS)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener }
                if (snapshot == null) _onSuccessGetReservationFacilitySettingDataList.postValue(emptyList())
                else {
                    val facilitySettingList: MutableList<ReservationFacilitySettingData> = mutableListOf()
                    for (document in snapshot) {
                        val unableTimeList = (document.get("unableTimeList") as ArrayList<HashMap<String,ReservationUnableTimeItem>>).map {
                            changeHashMapToReservationUnableTimeItem(it)
                        }
                        facilitySettingList.add(
                            ReservationFacilitySettingData(
                                document.getLong("icon")!!.toInt(),
                                document.get("name") as String,
                                document.get("intervalTime") as Long,
                                document.get("maxTime") as Long,
                                unableTimeList ))
                    }
                    _onSuccessGetReservationFacilitySettingDataList.postValue(facilitySettingList)
                }
            }
    }

    //For Facility Data
    private fun changeHashMapToReservationUnableTimeItem(it : HashMap<String, ReservationUnableTimeItem>) : ReservationUnableTimeItem{
        var timeType : ReservationUnableTimeType = ReservationUnableTimeType.HALF_HOUR
        if ((it["type"] as String) == "HOUR") timeType = ReservationUnableTimeType.HOUR
        val reservationTimeData : ReservationTimeData = (it["data"] as HashMap<String, ReservationTimeData>).let {
            ReservationTimeData(it["hour"] as Long, it["min"] as Long)
        }
        return ReservationUnableTimeItem(timeType, reservationTimeData, it["unable"] as Boolean)
    }

    fun startReservationEquipment(agency: String, itemName : String) : Completable{
        return Completable.create{ emitter ->
            firestore.collection(agency).document(FIRESTORE_RESERVATION).collection(FIRESTORE_RESERVATION_EQUIPMENT)
                .document(itemName).update("usable", true)
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { emitter.onError(Throwable("Error starting RESERVATION_INFO OF EQUIPMENT")) }
        }
    }

    fun stopReservationEquipment(agency: String, item : ReservationEquipmentData): Completable {
        return Completable.create{ emitter ->
            firestore.collection(agency).document(FIRESTORE_RESERVATION).collection(FIRESTORE_RESERVATION_EQUIPMENT)
                .document(item.name).set(item)
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { emitter.onError(Throwable("Error stopping RESERVATION_INFO OF EQUIPMENT")) }
        }
    }

    fun deleteReservationData(agency: String, reservationType: ReservationType, reservationDataName : String) : Completable{
        return Completable.create { emitter ->
            when (reservationType){
                ReservationType.EQUIPMENT -> {
                    firestore.collection(agency).document(FIRESTORE_RESERVATION).collection(FIRESTORE_RESERVATION_EQUIPMENT_SETTINGS)
                        .document(reservationDataName).delete()
                        .addOnFailureListener { emitter.onError(Throwable("Error deleting RESERVATION_SETTING OF EQUIPMENT")) }
                    firestore.collection(agency).document(FIRESTORE_RESERVATION).collection(FIRESTORE_RESERVATION_EQUIPMENT)
                        .document(reservationDataName).delete()
                        .addOnSuccessListener { emitter.onComplete() }
                        .addOnFailureListener { emitter.onError(Throwable("Error deleting RESERVATION_INFO OF EQUIPMENT")) }
                }
                ReservationType.FACILITY -> {
                    firestore.collection(agency).document(FIRESTORE_RESERVATION).collection(FIRESTORE_RESERVATION_FACILITY_SETTINGS)
                        .document(reservationDataName).delete()
                        .addOnFailureListener { emitter.onError(Throwable("Error deleting RESERVATION_SETTING OF FACILITY")) }
                    firestore.collection(agency).document(FIRESTORE_RESERVATION).collection(FIRESTORE_RESERVATION_FACILITY)
                        .document(reservationDataName).delete()
                        .addOnSuccessListener { emitter.onComplete() }
                        .addOnFailureListener { emitter.onError(Throwable("Error deleting RESERVATION_INFO OF FACILITY")) }
                }
            }
        }
    }
    /*//Update 함수들 - 이름 변경 및 중복 이름 관련 코드 필요.*/
    fun updateEquipmentReservationData(agency: String, data : ReservationEquipmentItem, needDel : Boolean, oldItemName : String) : Completable {
        return Completable.create { emitter ->
            if (needDel){
                firestore.collection(agency).document(FIRESTORE_RESERVATION).collection(FIRESTORE_RESERVATION_EQUIPMENT_SETTINGS)
                    .document(oldItemName).delete()
                    .addOnFailureListener { emitter.onError(Throwable("Error deleting RESERVATION_SETTING OF EQUIPMENT")) }
                    .addOnSuccessListener { Log.e("checking", "삭제되었습니다!") }
                firestore.collection(agency).document(FIRESTORE_RESERVATION).collection(FIRESTORE_RESERVATION_EQUIPMENT)
                    .document(oldItemName).delete()
                    .addOnSuccessListener { Log.e("checking", "삭제되었습니다!") }
                    .addOnFailureListener { emitter.onError(Throwable("Error deleting RESERVATION_INFO OF EQUIPMENT")) }
                firestore.collection(agency).document(FIRESTORE_RESERVATION).collection(FIRESTORE_RESERVATION_EQUIPMENT)
                    .document(data.data.name).set(data.equipmentData)
                    .addOnFailureListener { emitter.onError(Throwable("Error saving RESERVATION_INFO OF EQUIPMENT")) }
            }
            firestore.collection(agency).document(FIRESTORE_RESERVATION).collection(FIRESTORE_RESERVATION_EQUIPMENT)
                .document(data.data.name).update("icon", data.equipmentData.icon, "name", data.equipmentData.name)
                .addOnSuccessListener { Log.e("checking", "부분 수정되었습니다!") }
                .addOnFailureListener { emitter.onError(Throwable("Error saving RESERVATION_INFO OF EQUIPMENT")) }
            firestore.collection(agency).document(FIRESTORE_RESERVATION).collection(FIRESTORE_RESERVATION_EQUIPMENT_SETTINGS)
                .document(data.data.name).set(data.data)
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { emitter.onError(Throwable("Error saving RESERVATION_SETTING OF EQUIPMENT")) }
        }
    }

    fun updateFacilityReservationData(timeSetChanged : Boolean, agency: String, data : ReservationFacilityItem, needDel : Boolean, oldItemName : String) : Completable {
        return Completable.create { emitter ->
            if (needDel){
                firestore.collection(agency).document(FIRESTORE_RESERVATION).collection(FIRESTORE_RESERVATION_FACILITY_SETTINGS)
                    .document(oldItemName).delete()
                    .addOnSuccessListener { Log.e("checking", "삭제되었습니다!") }
                    .addOnFailureListener { emitter.onError(Throwable("Error deleting RESERVATION_SETTING OF FACILITY")) }
                firestore.collection(agency).document(FIRESTORE_RESERVATION).collection(FIRESTORE_RESERVATION_FACILITY)
                    .document(oldItemName).delete()
                    .addOnSuccessListener { Log.e("checking", "삭제되었습니다!") }
                    .addOnFailureListener { emitter.onError(Throwable("Error deleting RESERVATION_INFO OF FACILITY")) }
                firestore.collection(agency).document(FIRESTORE_RESERVATION).collection(FIRESTORE_RESERVATION_FACILITY_SETTINGS)
                    .document(data.data.name).set(data.data)
                    .addOnFailureListener { emitter.onError(Throwable("Error updating RESERVATION_INFO OF FACILITY")) }
                val facilityListData = ReservationFacilityItem(data.data, data.unableTimeList).makeReservationFacilityListData()
                firestore.collection(agency).document(FIRESTORE_RESERVATION).collection(FIRESTORE_RESERVATION_FACILITY)
                    .document(data.data.name).set(facilityListData)
                    .addOnSuccessListener { emitter.onComplete() }
                    .addOnFailureListener { emitter.onError(Throwable("Error updating RESERVATION_INFO OF FACILITY")) }
            }
            else {
                if (timeSetChanged) {
                    val facilityListData = ReservationFacilityItem(data.data, data.unableTimeList).makeReservationFacilityListData()
                    firestore.collection(agency).document(FIRESTORE_RESERVATION).collection(FIRESTORE_RESERVATION_FACILITY)
                        .document(data.data.name).set(facilityListData)
                        .addOnSuccessListener { Log.e("checking", "시간 리스트가 수정되었습니다!") }
                        .addOnFailureListener { emitter.onError(Throwable("Error updating RESERVATION_INFO OF FACILITY")) }
                }
                firestore.collection(agency).document(FIRESTORE_RESERVATION).collection(FIRESTORE_RESERVATION_FACILITY_SETTINGS)
                    .document(data.data.name).set(data.data)
                    .addOnSuccessListener { emitter.onComplete() }
                    .addOnFailureListener { emitter.onError(Throwable("Error updating RESERVATION_INFO OF FACILITY")) }
            }
        }
    }

    fun saveEquipmentReservationData(agency: String, data : ReservationEquipmentItem) : Completable {
        return Completable.create { emitter ->
            firestore.collection(agency).document(FIRESTORE_RESERVATION)
            firestore.collection(agency).document(FIRESTORE_RESERVATION).collection(FIRESTORE_RESERVATION_EQUIPMENT_SETTINGS)
                .document(data.data.name).set(data.data)
                .addOnFailureListener { emitter.onError(Throwable("Error saving RESERVATION_SETTING OF EQUIPMENT")) }
            firestore.collection(agency).document(FIRESTORE_RESERVATION).collection(FIRESTORE_RESERVATION_EQUIPMENT)
                .document(data.data.name).set(data.equipmentData)
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { emitter.onError(Throwable("Error saving RESERVATION_INFO OF EQUIPMENT")) }
        }
    }

    fun saveFacilityReservationData(agency: String, data : ReservationFacilityItem) : Completable {
        return Completable.create { emitter ->
            firestore.collection(agency).document(FIRESTORE_RESERVATION)
            firestore.collection(agency).document(FIRESTORE_RESERVATION).collection(FIRESTORE_RESERVATION_FACILITY_SETTINGS)
                .document(data.data.name).set(data.data)
                .addOnFailureListener { emitter.onError(Throwable("Error saving RESERVATION_SETTING OF FACILITY")) }
            val facilityListData = ReservationFacilityItem(data.data, data.unableTimeList).makeReservationFacilityListData()
            firestore.collection(agency).document(FIRESTORE_RESERVATION).collection(FIRESTORE_RESERVATION_FACILITY)
                .document(data.data.name).set(facilityListData)
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { emitter.onError(Throwable("Error saving RESERVATION_INFO OF FACILITY")) }
        }
    }
}

