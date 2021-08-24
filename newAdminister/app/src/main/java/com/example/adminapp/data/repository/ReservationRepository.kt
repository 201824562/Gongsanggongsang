package com.example.adminapp.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.adminapp.data.model.*
import com.example.adminapp.utils.SingleLiveEvent
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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
        private val FIRESTORE_RESERVATION_LOG = "LOG"
        private val FIRESTORE_RESERVATION_EQUIPMENT = "EQUIPMENT"
        private val FIRESTORE_RESERVATION_EQUIPMENT_SETTINGS = "EQUIPMENT_SETTINGS"
        private val FIRESTORE_RESERVATION_FACILITY = "FACILITY"
        private val FIRESTORE_RESERVATION_FACILITY_SETTINGS = "FACILITY_SETTINGS"
    }

    private val firestore = FirebaseFirestore.getInstance()

    private val _onSuccessGetReservationEquipmentSettingDataList = SingleLiveEvent<List<ReservationEquipmentSettingData>>()
    private val onSuccessGetReservationEquipmentSettingDataList: LiveData<List<ReservationEquipmentSettingData>> get() = _onSuccessGetReservationEquipmentSettingDataList
    private val _onSuccessGetReservationEquipmentLogData = SingleLiveEvent<List<ReservationEquipmentLog>>()
    private val onSuccessGetReservationEquipmentLogData: LiveData<List<ReservationEquipmentLog>> get() = _onSuccessGetReservationEquipmentLogData
    private val _onSuccessGetReservationEquipmentData = SingleLiveEvent<ReceiverEquipment>()
    private val onSuccessGetReservationEquipmentData: LiveData<ReceiverEquipment> get() = _onSuccessGetReservationEquipmentData
    private val _onSuccessGetReservationUsingEquipmentDataList = SingleLiveEvent<List<ReservationEquipmentData>>()
    private val onSuccessGetReservationUsingEquipmentDataList: LiveData<List<ReservationEquipmentData>> get() = _onSuccessGetReservationUsingEquipmentDataList
    private val _onSuccessGetReservationEquipmentDataList = SingleLiveEvent<List<ReservationEquipmentData>>()
    private val onSuccessGetReservationEquipmentDataList: LiveData<List<ReservationEquipmentData>> get() = _onSuccessGetReservationEquipmentDataList
    private val _onSuccessGetReservationFacilityLogData = SingleLiveEvent<ReceiverFacilityLog>()
    private val onSuccessGetReservationFacilityLogData: LiveData<ReceiverFacilityLog> get() = _onSuccessGetReservationFacilityLogData
    private val _onSuccessGetReservationUsingFacilityLogList = SingleLiveEvent<List<ReservationFacilityLog>>()
    private val onSuccessGetReservationUsingFacilityLogList : LiveData<List<ReservationFacilityLog>> get() = _onSuccessGetReservationUsingFacilityLogList
    private val _onSuccessGetReservationFacilityLogList = SingleLiveEvent<List<ReservationFacilityLog>>()
    private val onSuccessGetReservationFacilityLogList : LiveData<List<ReservationFacilityLog>> get() = _onSuccessGetReservationFacilityLogList
    private val _onSuccessGetReservationLogAllList = SingleLiveEvent<List<ReservationLogItem>>()
    private val onSuccessGetReservationLogAllList : LiveData<List<ReservationLogItem>> get() = _onSuccessGetReservationLogAllList
    private val _onSuccessGetReservationLogEquipmentList = SingleLiveEvent<List<ReservationLogItem>>()
    private val onSuccessGetReservationLogEquipmentList : LiveData<List<ReservationLogItem>> get() = _onSuccessGetReservationLogEquipmentList
    private val _onSuccessGetReservationLogFacilityList = SingleLiveEvent<List<ReservationLogItem>>()
    private val onSuccessGetReservationLogFacilityList : LiveData<List<ReservationLogItem>> get() = _onSuccessGetReservationLogFacilityList
    private val _onSuccessGetReservationFacilitySettingData = SingleLiveEvent<ReceiverFacilitySettingData>()
    private val onSuccessGetReservationFacilitySettingData: LiveData<ReceiverFacilitySettingData> get() = _onSuccessGetReservationFacilitySettingData
    private val _onSuccessGetReservationFacilityDataList = SingleLiveEvent<List<ReservationFacilityLog>>()
    private val onSuccessGetReservationFacilityDataList : LiveData<List<ReservationFacilityLog>> get() = _onSuccessGetReservationFacilityDataList
    private val _onSuccessGetReservationFacilityNotDoneDataList = SingleLiveEvent<List<ReservationFacilityLog>>()
    private val onSuccessGetReservationFacilityNotDoneDataList : LiveData<List<ReservationFacilityLog>> get() = _onSuccessGetReservationFacilityNotDoneDataList
    private val _onSuccessGetReservationFacilitySettingDataList = SingleLiveEvent<List<ReservationFacilitySettingData>>()
    private val onSuccessGetReservationFacilitySettingDataList: LiveData<List<ReservationFacilitySettingData>> get() = _onSuccessGetReservationFacilitySettingDataList


    fun getReservationEquipmentSettingData(agency: String, itemName : String): Single<ReservationEquipmentSettingData> {
        return Single.create { emitter ->
            firestore.collection(agency).document(FIRESTORE_RESERVATION)
                .collection(FIRESTORE_RESERVATION_EQUIPMENT_SETTINGS)
                .document(itemName).get()
                .addOnSuccessListener {
                    if (it.data != null && it.get("name") == itemName) {
                        emitter.onSuccess(
                            ReservationEquipmentSettingData(
                                CategoryResources.makeIconStringToDrawableID((it.get("icon") as String)),
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
                                CategoryResources.makeIconStringToDrawableID((document.get("icon") as String)),
                                document.get("name") as String,
                                document.get("maxTime") as Long)
                        )
                    }
                    _onSuccessGetReservationEquipmentSettingDataList.postValue(equipmentSettingList)
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
                    Log.w(TAG, "Listen failed.", e)
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
                                document.get("endTime") as String))
                    }
                    _onSuccessGetReservationEquipmentLogData.postValue(equipmentLogList)
                }
            }
    }


    fun getReservationEquipmentData(agency: String, itemName : String): LiveData<ReceiverEquipment> {
        getReservationEquipmentDataFromFirebase(agency, itemName)
        return onSuccessGetReservationEquipmentData }

    private fun getReservationEquipmentDataFromFirebase(agency: String, itemName : String) {
        firestore.collection(agency).document(FIRESTORE_RESERVATION)
            .collection(FIRESTORE_RESERVATION_EQUIPMENT).whereEqualTo("name", itemName)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
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
                                    it.getLong("maxTime")!!
                                )
                            )
                        )
                    }
                }
            }
    }
    fun getReservationUsingEquipmentDataList(agency: String) : LiveData<List<ReservationEquipmentData>> {
        getReservationUsingEquipmentDataListFromFirebase(agency)
        return onSuccessGetReservationUsingEquipmentDataList
    }

    private fun getReservationUsingEquipmentDataListFromFirebase(agency: String) {
        firestore.collection(agency).document(FIRESTORE_RESERVATION)
            .collection(FIRESTORE_RESERVATION_EQUIPMENT).whereEqualTo("using", true)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener }
                if (snapshot == null) _onSuccessGetReservationUsingEquipmentDataList.postValue(emptyList())
                else {
                    val equipmentList: MutableList<ReservationEquipmentData> = mutableListOf()
                    for (document in snapshot) {
                        var startTime : String = ""
                        var endTime : String = ""
                        if (document.get("startTime") != null) startTime=(document.get("startTime") as String)
                        if (document.get("endTime") != null) endTime = (document.get("endTime") as String)
                        equipmentList.add(
                            ReservationEquipmentData(
                                CategoryResources.makeIconStringToDrawableID((document.get("icon") as String)),
                                document.get("name") as String,
                                document.get("user") as String,
                                startTime,
                                endTime,
                                document.getLong("intervalTime")!!,
                                document.getBoolean("using")!!,
                                document.getBoolean("usable")!!,
                                document.getLong("maxTime")!!
                            ))
                    }
                    _onSuccessGetReservationUsingEquipmentDataList.postValue(equipmentList)
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
                                CategoryResources.makeIconStringToDrawableID((document.get("icon") as String)),
                                document.get("name") as String,
                                document.get("user") as String,
                                startTime,
                                endTime,
                                document.getLong("intervalTime")!!,
                                document.getBoolean("using")!!,
                                document.getBoolean("usable")!!,
                                document.getLong("maxTime")!!))
                    }
                    _onSuccessGetReservationEquipmentDataList.postValue(equipmentList)
                }
            }
    }

    fun getReservationFacilitySettingData(agency: String, itemName : String): LiveData<ReceiverFacilitySettingData> {
        getReservationFacilitySettingDataFromFirebase(agency, itemName)
        return onSuccessGetReservationFacilitySettingData }

    private fun getReservationFacilitySettingDataFromFirebase(agency: String, itemName : String) {
        firestore.collection(agency).document(FIRESTORE_RESERVATION)
            .collection(FIRESTORE_RESERVATION_FACILITY_SETTINGS).whereEqualTo("name", itemName)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
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

    fun getReservationUsingFacilityLogList(agency: String, index: Int) : LiveData<List<ReservationFacilityLog>> {
        getReservationUsingFacilityLogListFromFirebase(agency, index)
        return when (index) {
            0 -> onSuccessGetReservationUsingFacilityLogList
            1 -> onSuccessGetReservationFacilityLogList
            else -> onSuccessGetReservationUsingFacilityLogList
        }
    }
    private fun getReservationUsingFacilityLogListFromFirebase(agency: String, index: Int) {
        firestore.collection(agency).document(FIRESTORE_RESERVATION).collection(FIRESTORE_RESERVATION_LOG)
            .whereEqualTo("reservationType", "예약 사용").whereEqualTo("reservationState", "사용중")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener }
                if (snapshot == null) {
                    when (index) {
                        0 -> _onSuccessGetReservationUsingFacilityLogList.postValue(emptyList())
                        1 -> _onSuccessGetReservationFacilityLogList.postValue(emptyList())
                        else -> _onSuccessGetReservationUsingFacilityLogList.postValue(emptyList())
                    }
                }
                else {
                    val facilityLogList: MutableList<ReservationFacilityLog> = mutableListOf()
                    Log.e("checking", "${snapshot.documents.size}")
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
                    when (index) {
                        0 -> _onSuccessGetReservationUsingFacilityLogList.postValue(facilityLogList)
                        1 -> _onSuccessGetReservationFacilityLogList.postValue(facilityLogList)
                        else -> _onSuccessGetReservationUsingFacilityLogList.postValue(facilityLogList)
                    }
                }
            }
    }

    fun getReservationLogDataList(agency: String, index: Int) : LiveData<List<ReservationLogItem>> {
        getReservationLogDataListFromFirebase(agency, index)
        return when (index) {
            0 -> onSuccessGetReservationLogAllList
            1 -> onSuccessGetReservationLogEquipmentList
            2 -> onSuccessGetReservationLogFacilityList
            else -> onSuccessGetReservationLogAllList
        }
    }

    //TODO : 로그 전체 말고 분리하기 (함수)
    private fun getReservationLogDataListFromFirebase(agency: String, index: Int) {
        when (index){
            0 -> {
                firestore.collection(agency).document(FIRESTORE_RESERVATION).collection(FIRESTORE_RESERVATION_LOG)
                .orderBy("endTime", Query.Direction.DESCENDING)
                    .addSnapshotListener { snapshot, e ->
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e)
                            return@addSnapshotListener }
                        if (snapshot == null)  _onSuccessGetReservationLogAllList.postValue(emptyList())
                        else {
                            val logItemList: MutableList<ReservationLogItem> = mutableListOf()
                            for (document in snapshot) {
                                if ((document.get("reservationType") as String) == "바로 사용"){
                                    logItemList.add(
                                        ReservationLogItem(
                                            ReservationType.EQUIPMENT,
                                            ReservationEquipmentLog(
                                                CategoryResources.makeIconStringToDrawableID((document.get("icon") as String)),
                                                document.get("name") as String,
                                                document.get("userId") as String,
                                                document.get("userName") as String,
                                                document.get("reservationState") as String,
                                                document.get("reservationType") as String,
                                                document.get("startTime") as String,
                                                document.get("endTime") as String ),
                                            null )) }
                                else {
                                    logItemList.add(
                                        ReservationLogItem(
                                            ReservationType.FACILITY,
                                            null,
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
                                                document.get("usable") as Boolean)))
                                }
                            }
                            _onSuccessGetReservationLogAllList.postValue(logItemList)
                        }
                    }
            }
            1 -> { firestore.collection(agency).document(FIRESTORE_RESERVATION).collection(FIRESTORE_RESERVATION_LOG)
                    .whereEqualTo("reservationType", "바로 사용").orderBy("startTime", Query.Direction.DESCENDING)
                    .addSnapshotListener { snapshot, e ->
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e)
                            return@addSnapshotListener }
                        if (snapshot == null)  _onSuccessGetReservationLogEquipmentList.postValue(emptyList())
                        else {
                            val logItemList: MutableList<ReservationLogItem> = mutableListOf()
                            for (document in snapshot) {
                                logItemList.add(
                                    ReservationLogItem(
                                        ReservationType.EQUIPMENT,
                                        ReservationEquipmentLog(
                                            CategoryResources.makeIconStringToDrawableID((document.get("icon") as String)),
                                            document.get("name") as String,
                                            document.get("userId") as String,
                                            document.get("userName") as String,
                                            document.get("reservationState") as String,
                                            document.get("reservationType") as String,
                                            document.get("startTime") as String,
                                            document.get("endTime") as String ),
                                        null ))
                            }
                            _onSuccessGetReservationLogEquipmentList.postValue(logItemList)
                        }
                    }
            }
            else -> { firestore.collection(agency).document(FIRESTORE_RESERVATION).collection(FIRESTORE_RESERVATION_LOG)
                .whereEqualTo("reservationType", "예약 사용").orderBy("startTime", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e)
                        return@addSnapshotListener }
                    if (snapshot == null)  _onSuccessGetReservationLogFacilityList.postValue(emptyList())
                    else {
                        val logItemList: MutableList<ReservationLogItem> = mutableListOf()
                        for (document in snapshot) {
                            logItemList.add(
                                ReservationLogItem(
                                    ReservationType.FACILITY,
                                    null,
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
                                        document.get("usable") as Boolean)))
                        }
                        _onSuccessGetReservationLogFacilityList.postValue(logItemList)
                    }
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
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }
                when {
                    snapshot == null -> _onSuccessGetReservationFacilityLogData.postValue(
                        ReceiverFacilityLog(false, null)
                    )
                    snapshot.isEmpty -> _onSuccessGetReservationFacilityLogData.postValue(
                        ReceiverFacilityLog(false, null)
                    )
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
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
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
        return onSuccessGetReservationFacilityNotDoneDataList }

    private fun getReservationFacilityNotDoneLogDataListFromFirebase(agency: String, itemType : String, itemName : String) {
        firestore.collection(agency).document(FIRESTORE_RESERVATION)
            .collection(FIRESTORE_RESERVATION_LOG)
            .whereEqualTo("reservationType", itemType).whereEqualTo("name", itemName).whereIn("reservationState", listOf("사용중", "예약중"))
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener }
                if (snapshot == null) _onSuccessGetReservationFacilityNotDoneDataList.postValue(emptyList())
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
                                CategoryResources.makeIconStringToDrawableID((document.get("icon") as String)),
                                document.get("name") as String,
                                document.get("intervalTime") as Long,
                                document.get("maxTime") as Long,
                                unableTimeList,
                                document.get("usable") as Boolean))
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


    fun finishReservationEquipment (agency: String, itemName: String)  {
        firestore.collection(agency).document(FIRESTORE_RESERVATION).collection(FIRESTORE_RESERVATION_EQUIPMENT)
            .document(itemName).update("using", false, "user", "", "startTime", "", "endTime", "", "intervalTime", 0)
    }

    fun finishReservationFacility (agency: String, documentId: String){
        firestore.collection(agency).document(FIRESTORE_RESERVATION).collection(FIRESTORE_RESERVATION_LOG)
            .document(documentId).update("reservationState", "사용완료")
            .addOnSuccessListener {  Log.e("checking", "Succeed updating RESERVATION_LOG OF FACILITY") }
            .addOnFailureListener { Log.e("checking", "Error updating RESERVATION_LOG OF FACILITY") }
    }
    fun cancelReservationFacility (agency: String, documentId: String){
        firestore.collection(agency).document(FIRESTORE_RESERVATION).collection(FIRESTORE_RESERVATION_LOG)
            .document(documentId).update("reservationState", "강제취소")
            .addOnSuccessListener {  Log.e("checking", "Succeed updating RESERVATION_LOG OF FACILITY") }
            .addOnFailureListener { Log.e("checking", "Error updating RESERVATION_LOG OF FACILITY") }
    }

    fun startReservationEquipment(agency: String, itemName : String) : Completable{
        return Completable.create{ emitter ->
            firestore.collection(agency).document(FIRESTORE_RESERVATION).collection(FIRESTORE_RESERVATION_EQUIPMENT)
                .document(itemName).update("usable", true)
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { emitter.onError(Throwable("Error starting RESERVATION_INFO OF EQUIPMENT")) }
        }
    }

    fun startReservationFacility(agency: String, itemName : String) : Completable{
        return Completable.create{ emitter ->
            firestore.collection(agency).document(FIRESTORE_RESERVATION).collection(FIRESTORE_RESERVATION_FACILITY_SETTINGS)
                .document(itemName).update("usable", true)
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { emitter.onError(Throwable("Error starting RESERVATION_INFO OF FACILITY")) }
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

    fun stopReservationFacility(agency: String, itemName: String): Completable {
        return Completable.create{ emitter ->
            firestore.collection(agency).document(FIRESTORE_RESERVATION).collection(FIRESTORE_RESERVATION_FACILITY_SETTINGS)
                .document(itemName).update("usable", false)
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { emitter.onError(Throwable("Error stopping RESERVATION_INFO OF FACILITY")) }
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
                .document(data.data.name).update("icon", CategoryResources.makeDrawableIDToString(data.equipmentData.icon),
                    "name", data.equipmentData.name)
                .addOnSuccessListener { Log.e("checking", "부분 수정되었습니다!") }
                .addOnFailureListener { emitter.onError(Throwable("Error saving RESERVATION_INFO OF EQUIPMENT")) }
            firestore.collection(agency).document(FIRESTORE_RESERVATION).collection(FIRESTORE_RESERVATION_EQUIPMENT_SETTINGS)
                .document(data.data.name).set(data.data.getNewThis())
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
                .document(data.data.name).set(data.data.getNewThis())
                .addOnFailureListener { emitter.onError(Throwable("Error saving RESERVATION_SETTING OF EQUIPMENT")) }
            firestore.collection(agency).document(FIRESTORE_RESERVATION).collection(FIRESTORE_RESERVATION_EQUIPMENT)
                .document(data.data.name).set(data.equipmentData.getNewThis())
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { emitter.onError(Throwable("Error saving RESERVATION_INFO OF EQUIPMENT")) }
        }
    }

    fun saveFacilityReservationData(agency: String, data : ReservationFacilityItem) : Completable {
        return Completable.create { emitter ->
            firestore.collection(agency).document(FIRESTORE_RESERVATION)
            firestore.collection(agency).document(FIRESTORE_RESERVATION).collection(FIRESTORE_RESERVATION_FACILITY_SETTINGS)
                .document(data.data.name).set(data.data.getNewThis())
                .addOnFailureListener { emitter.onError(Throwable("Error saving RESERVATION_SETTING OF FACILITY")) }
            val facilityListData = ReservationFacilityItem(data.data, data.unableTimeList).makeReservationFacilityListData()
            firestore.collection(agency).document(FIRESTORE_RESERVATION).collection(FIRESTORE_RESERVATION_FACILITY)
                .document(data.data.name).set(facilityListData)
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { emitter.onError(Throwable("Error saving RESERVATION_INFO OF FACILITY")) }
        }
    }
}

