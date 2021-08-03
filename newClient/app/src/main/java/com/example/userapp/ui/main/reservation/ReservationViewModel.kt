package com.example.userapp.ui.main.reservation

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.userapp.base.BaseViewModel
import com.example.userapp.data.entity.DayTimeSlot
import com.example.userapp.data.entity.ReservationTimeData
import com.example.userapp.data.entity.UnableTimeList
import com.example.userapp.data.model.*
import com.google.common.collect.ArrayListMultimap
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import okhttp3.internal.userAgent
import java.time.LocalDateTime

class ReservationViewModel : BaseViewModel() {
    val database = FirebaseFirestore.getInstance()

    val UseEquipmentLiveData = MutableLiveData<List<ReservationUseEquipment>>()
    val EquipmentLiveData = MutableLiveData<List<ReservationEquipment>>()
    val FacilityLiveData = MutableLiveData<List<ReservationFacility>>()
    val FacilityTimeSlotLiveData = MutableLiveData<List<ReservationFacilityTimeSlot>>()
    val FacilityReserveLiveData = MutableLiveData<List<ReservationReserveFacility>>()
    val FacilityDayInfoLiveData = MutableLiveData<ReservationFacilityDayInfo>()
    val FacilitySelectedTimeSlotLiveData = MutableLiveData<ReservationFacilityDayInfo>()


    private val UseEquipmentData = arrayListOf<ReservationUseEquipment>()
    private val EquipmentData = arrayListOf<ReservationEquipment>()
    private val FacilityData = arrayListOf<ReservationFacility>()
    private val FacilityTimeSlotData = arrayListOf<ReservationFacilityTimeSlot>()
    private val FacilityReserveData = arrayListOf<ReservationReserveFacility>()
    private var FacilityDayInfoData = ReservationFacilityDayInfo(
        "default",
        "default",
        mutableListOf()
    )
    private var FacilitySelectedTimeSlotData = ReservationFacilityDayInfo(
        "default",
        "default",
        mutableListOf()
    )


    //뷰모델스코프를 이용해서 제어
    var viewmodelscope = viewModelScope

    fun cancelViewModelScope() {
        viewmodelscope.cancel()
    }

    //파이어베이스에서 데이터 가져오는 함수 getEquipmentData, getUseEquipmentData, getFacilityData 통일해야함
    fun getEquipmentData() {
        lateinit var document_name: String
        lateinit var using: String
        lateinit var end_time: String
        lateinit var category: String

        database.collection("COMMUNAL_Equipment")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                EquipmentData.clear()
                for (document in value!!) {
                    document_name = document.id
                    category = document.get("category") as String
                    if (document.contains("name")) {
                        using = "using"
                        end_time = document.get("end") as String
                    } else {
                        using = "no_using"
                        end_time = "no_endtime"
                    }

                    EquipmentData.add(
                        ReservationEquipment(
                            document_name,
                            using,
                            end_time,
                            category
                        )
                    )
                }
                EquipmentLiveData.value = EquipmentData
            }
    }

    fun getUseEquipmentData() {
        lateinit var document_name: String
        lateinit var name: String
        lateinit var start_time: String
        lateinit var end_time: String
        lateinit var category: String
        var coroutine: CoroutineScope = CoroutineScope(Dispatchers.Main)

        database.collection("COMMUNAL_Equipment")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                UseEquipmentData.clear()
                for (document in value!!) {
                    document_name = document.id
                    if (document.getString("name") != null) {
                        name = document.getString("name") ?: " "
                        start_time = document.get("start") as String
                        end_time = document.get("end") as String
                        category = document.get("category") as String

                        if (name == "kijung") { //userdata
                            UseEquipmentData.add(
                                ReservationUseEquipment(
                                    document_name,
                                    name,
                                    start_time,
                                    end_time,
                                    0,
                                    category,
                                    coroutine
                                )
                            )
                        }
                    }
                }
                UseEquipmentLiveData.value = UseEquipmentData
            }
    }

    fun getFacilityData() {
        database.collection("한국장학재단_부산").document("RESERVATION")
            .collection("FACILITY_SETTINGS")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                FacilityData.clear()
                for (document in value!!) {
                    FacilityData.add(
                        ReservationFacility(
                            document.id,
                            document.get("icon") as Long,
                            document.get("maxTime") as Long,
                            document.get("intervalTime") as Long,
                            document.get("unableTimeList") as List<UnableTimeList>
                        )
                    )
                    Log.e("dataclass",ReservationFacility(
                        document.id,
                        document.get("icon") as Long,
                        document.get("maxTime") as Long,
                        document.get("intervalTime") as Long,
                        document.get("unableTimeList") as List<UnableTimeList>
                    ).toString())
                }
                FacilityLiveData.value = FacilityData
            }
    }


//    fun getReserveFacilityData() {
//        lateinit var document_name: String
//        lateinit var category: String
//        var max_time: Long  //long타입은 lateinit이 안돼나?
//        var time_interval: Long
//
//        database.collection("COMMUNAL_Facility")
//            .addSnapshotListener { value, e ->
//                if (e != null) {
//                    return@addSnapshotListener
//                }
//                FacilityData.clear()
//                for (document in value!!) {
//                    database.collection(document.id)
//                        .addSnapshotListener { value, e ->
//                            if (e != null) {
//                                return@addSnapshotListener
//                            }
//                            for (document in value!!) {
//
//                            }
//
//                    document_name = document.id
//                    category = document.get("category") as String
//                    max_time = document.get("max_time") as Long
//                    time_interval = document.get("time_interval") as Long
//
//                    FacilityData.add(
//                        ReservationFacility(
//                            document_name,
//                            category,
//                            max_time,
//                            time_interval
//                        )
//                    )
//                }
//                FacilityLiveData.value = FacilityData
//            }
//    }

    fun getFacilityTimeSlotData(document_name: String, weekday: String) {
        //파이어베이스에서 해쉬맵스타일로 받아서 클래스형식으로 바꿔서 코드에 이용하는 방식
        var tmpList = mutableListOf<DayTimeSlot>()
        lateinit var tmp : DayTimeSlot

        database.collection("한국장학재단_부산").document("RESERVATION")
            .collection("FACILITY")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                FacilityDayInfoData.day_time_slot_list.clear()
                for (document in value!!) {
                    Log.e("print!!!!",document.id)
                    if( document.id == document_name){
                        (document.get(weekday) as ArrayList<HashMap<String, DayTimeSlot>>).let {
                            for (element in it){
                                tmp = DayTimeSlot(
                                    element["buttonSelected"] as Boolean,
                                    (element["data"] as HashMap<String, Long>)["hour"]?.let { it1 ->
                                        (element["data"] as HashMap<String, Long>)["min"]?.let { it2 ->
                                            ReservationTimeData(
                                                it1, it2)
                                        }
                                    },
                                    element["index"] as Long,
                                    element["user"] as String
                                )
                                tmpList.add(tmp)
                            }
                        }
                        FacilityDayInfoData = ReservationFacilityDayInfo(
                            document_name,
                            weekday,
                            tmpList
                        )
                    }
                }
                //선택 데이터 초기화
                FacilitySelectedTimeSlotData.document_name = document_name
                FacilitySelectedTimeSlotData.weekday = weekday
                FacilitySelectedTimeSlotData.day_time_slot_list.clear()

                FacilitySelectedTimeSlotLiveData.value = FacilitySelectedTimeSlotData
                FacilityDayInfoLiveData.value = FacilityDayInfoData
            }
    }



    fun add_use(ReservationEquipment: ReservationEquipment, usingtime: Int) {
        var map1 = mutableMapOf<String, Any>()
        var map2 = mutableMapOf<String, Any>()
        var map3 = mutableMapOf<String, Any>()

        map1["start"] = LocalDateTime.now().toString()
        map2["end"] = LocalDateTime.now().plusMinutes(usingtime.toLong()).toString()
        map3["name"] = "kijung" // name을 맨마지막으로 지우지 않으면 nullpointerexception 발생

        database.collection("COMMUNAL_Equipment").document(ReservationEquipment.document_name)
            .update(map1)
        database.collection("COMMUNAL_Equipment").document(ReservationEquipment.document_name)
            .update(map2)
        database.collection("COMMUNAL_Equipment").document(ReservationEquipment.document_name)
            .update(map3)

        EquipmentLiveData.value = EquipmentData
    }

    fun end_use(ReservationUseEquipment: ReservationUseEquipment) {
        ReservationUseEquipment.coroutine.cancel()
        var map1 = mutableMapOf<String, Any>()
        var map2 = mutableMapOf<String, Any>()
        var map3 = mutableMapOf<String, Any>()

        map1["name"] = FieldValue.delete()
        map2["start"] = FieldValue.delete()
        map3["end"] = FieldValue.delete()

        database.collection("COMMUNAL_Equipment").document(ReservationUseEquipment.document_name)
            .update(map1)
        database.collection("COMMUNAL_Equipment").document(ReservationUseEquipment.document_name)
            .update(map2)
        database.collection("COMMUNAL_Equipment").document(ReservationUseEquipment.document_name)
            .update(map3)


        UseEquipmentLiveData.value = UseEquipmentData
    }

    fun add_select_time_slot(dayTimeSlot: DayTimeSlot, maxSlot: Long): Boolean {
        if (FacilitySelectedTimeSlotData.day_time_slot_list.isEmpty()) {
            dayTimeSlot.buttonSelected = true
            dayTimeSlot.user = "kijung" //룸에서 데이터 가져오기
            FacilitySelectedTimeSlotData.day_time_slot_list.add(dayTimeSlot)
            FacilitySelectedTimeSlotLiveData.value = FacilitySelectedTimeSlotData
            Log.e("FacilityDayInfoData.day_time_slot_list333",FacilityDayInfoData.day_time_slot_list.toString())
            return true
        } else {
            if (maxSlot > FacilitySelectedTimeSlotData.day_time_slot_list.size.toLong()) {
                for (timeslot in FacilitySelectedTimeSlotData.day_time_slot_list) {
                    if (timeslot.index - dayTimeSlot.index == 1L || timeslot.index - dayTimeSlot.index == -1L) {
                        dayTimeSlot.buttonSelected = true
                        dayTimeSlot.user = "kijung" //룸에서 데이터 가져오기
                        FacilitySelectedTimeSlotData.day_time_slot_list.add(dayTimeSlot)
                        FacilitySelectedTimeSlotLiveData.value = FacilitySelectedTimeSlotData
                        Log.e("FacilityDayInfoData.day_time_slot_list44",FacilityDayInfoData.day_time_slot_list.toString())
                        return true
                    }
                }
            }
        }
        Log.e("FacilityDayInfoData.day_time_slot_list5555",FacilityDayInfoData.day_time_slot_list.toString())

        return false
        //최대 갯수제한 추가해줘야함!!
    }

    fun delete_select_time_slot(dayTimeSlot: DayTimeSlot): Boolean {
        var cnt: Int = 0
        for (timeslot in FacilitySelectedTimeSlotData.day_time_slot_list) {
            if (timeslot.index - dayTimeSlot.index == 1L || timeslot.index - dayTimeSlot.index == -1L) {
                cnt++
            }
        }
        if (cnt < 2) {
            FacilitySelectedTimeSlotData.day_time_slot_list.remove(dayTimeSlot)
            //t선택취소해도 기존 데이터베이스에 남아있는 현상
            Log.e("FacilitySelectedTimeSlotData.day_time_slot_list",FacilitySelectedTimeSlotData.day_time_slot_list.toString())
            Log.e("FacilityDayInfoData.day_time_slot_list666",FacilityDayInfoData.day_time_slot_list.toString())
            FacilitySelectedTimeSlotLiveData.value = FacilitySelectedTimeSlotData
            return true
        }
        Log.e("FacilityDayInfoData.day_time_slot_list7777",FacilityDayInfoData.day_time_slot_list.toString())

        return false

    }

    fun clear_select_time_slot() {
        FacilitySelectedTimeSlotData.day_time_slot_list.clear()
        FacilitySelectedTimeSlotLiveData.value = FacilitySelectedTimeSlotData
    }

    fun getDayTimeSlotListSize(): Long {
        return FacilitySelectedTimeSlotData.day_time_slot_list.size.toLong()
    }

    fun add_reserve() {
        var map1 = mutableMapOf<String, Any>()
        var tempList = mutableListOf<String>()

//        //시설 예약 데이터에 추가
//        database.collection("한국장학재단_부산").document("RESERVATION")
//            .collection("FACILITY_LOG").document(FacilitySelectedTimeSlotData.document_name)
//            .update(FacilitySelectedTimeSlotData)

//        ReservationReserveFacility(
//            FacilitySelectedTimeSlotData.first().document_name,
//            "kijung",
//            0L,
//            0L,
//            FacilitySelectedTimeSlotData.first().collection_name,
//            tempList
//        ).also{
//            FacilityReserveData.add(it)
//            database.collection("COMMUNAL_Log")
//                .add(it)
//        }
//
        Log.e("FacilityDayInfoData.day_time_slot_list2222",FacilityDayInfoData.day_time_slot_list.toString())
        Log.e("FacilitySelectedTimeSlotData.day_time_slot_list2222",FacilitySelectedTimeSlotData.day_time_slot_list.toString())

        for (timeslot in FacilityDayInfoData.day_time_slot_list){
            if (timeslot !in FacilitySelectedTimeSlotData.day_time_slot_list){
                FacilitySelectedTimeSlotData.day_time_slot_list.add(timeslot)
            }
        }
        Log.e("FacilityDayInfoData.day_time_slot_list",FacilityDayInfoData.day_time_slot_list.toString())
        Log.e("FacilitySelectedTimeSlotData.day_time_slot_list",FacilitySelectedTimeSlotData.day_time_slot_list.toString())

        FacilitySelectedTimeSlotData.day_time_slot_list.sortBy { it.index }
        for (timeslot in FacilityDayInfoData.day_time_slot_list){
            timeslot.buttonSelected = false
        }

        map1[FacilitySelectedTimeSlotData.weekday] = FacilitySelectedTimeSlotData.day_time_slot_list


        database.collection("한국장학재단_부산").document("RESERVATION")
            .collection("FACILITY").document(FacilitySelectedTimeSlotData.document_name)
            .update(map1)


        clear_select_time_slot()
        FacilityReserveLiveData.value = FacilityReserveData
    }

    fun cancel_reserve(reservationReserveFacility: ReservationReserveFacility) {
        var map1 = mutableMapOf<String, Any>()

        map1["name"] = "Nope" // name을 맨마지막으로 지우지 않으면 nullpointerexception 발생

        for(timeslot in reservationReserveFacility.timeSlotList){
            database.collection("COMMUNAL_Facility").document(reservationReserveFacility.document_name)
                .collection(reservationReserveFacility.weekday).document(timeslot)
                .update(map1)
        }
        FacilityReserveLiveData.value = FacilityReserveData
    }
}