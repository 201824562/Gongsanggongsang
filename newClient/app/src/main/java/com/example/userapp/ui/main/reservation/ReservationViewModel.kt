package com.example.userapp.ui.main.reservation

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.service.autofill.UserData
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.userapp.base.BaseSessionViewModel
import com.example.userapp.base.BaseViewModel
import com.example.userapp.data.dto.UserModel
import com.example.userapp.data.entity.DayTimeSlot
import com.example.userapp.data.entity.ReservationTimeData
import com.example.userapp.data.entity.UnableTimeList
import com.example.userapp.data.entity.User
import com.example.userapp.data.model.*
import com.google.common.collect.ArrayListMultimap
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import okhttp3.internal.userAgent
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ReservationViewModel(application: Application) : BaseSessionViewModel(application) {
    init {
        val userInfo : User?= null
    }
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
        0L,
        0L,
        "default",
        mutableListOf()
    )
    private var FacilitySelectedTimeSlotData = ReservationFacilityDayInfo(
        "default",
        0L,
        0L,
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
        lateinit var using: String
        lateinit var end_time: String

        database.collection("한국장학재단_부산").document("RESERVATION")
            .collection("EQUIPMENT")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                EquipmentData.clear()
                for (document in value!!) {
//                    Log.e("error checking!",(document.get("user") as String))
                    if((document.get("usable") as Boolean)){
                        if ((document.get("using") as Boolean)) {
                            using = "using"
                            end_time = document.get("endTime") as String
                        } else {
                            using = "no_using"
                            end_time = "no_endtime"
                        }
                        EquipmentData.add(
                            ReservationEquipment(
                                document.id,
                                using,
                                end_time,
                                document.getLong("icon")!!.toInt(),
                                document.get("maxTime") as Long
                            )
                        )
                    }
                }
                EquipmentLiveData.value = EquipmentData
            }
    }

    fun getUseEquipmentData() {
        lateinit var tmpId: String
        var coroutine: CoroutineScope = CoroutineScope(Dispatchers.Main)


        database.collection("한국장학재단_부산").document("RESERVATION")
        .collection("EQUIPMENT")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                UseEquipmentData.clear()
                for (document in value!!) {
                    if (document.get("user") as String != "") {
                        tmpId = document.getString("user") ?: " "
                        if (tmpId == authToken) { //userdata
                            UseEquipmentData.add(
                                ReservationUseEquipment(
                                    document.id,
                                    tmpId,
                                    document.get("startTime") as String,
                                    document.get("endTime") as String,
                                    0,
                                    document.get("icon") as Long,
                                    coroutine
                                )
                            )
                            Log.e("ReservationUseEquipment",ReservationUseEquipment(
                                document.id,
                                tmpId,
                                document.get("startTime") as String,
                                document.get("endTime") as String,
                                0,
                                document.get("icon") as Long,
                                coroutine
                            ).toString())
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
                            document.get("usable") as Boolean,
                            document.get("unableTimeList") as List<UnableTimeList>
                        )
                    )
                    Log.e("dataclass",ReservationFacility(
                        document.id,
                        document.get("icon") as Long,
                        document.get("maxTime") as Long,
                        document.get("intervalTime") as Long,
                        document.get("usable") as Boolean,
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

    fun getFacilityTimeSlotData(document_name: String, weekday: String, icon: Long, interval_time: Long) {
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
                            icon,
                            interval_time,
                            weekday,
                            tmpList
                        )
                    }
                }
                //선택 데이터 초기화
                FacilitySelectedTimeSlotData.document_name = document_name
                FacilitySelectedTimeSlotData.weekday = weekday
                FacilitySelectedTimeSlotData.icon = icon
                FacilitySelectedTimeSlotData.interval_time = interval_time
                FacilitySelectedTimeSlotData.day_time_slot_list.clear()

                FacilitySelectedTimeSlotLiveData.value = FacilitySelectedTimeSlotData
                FacilityDayInfoLiveData.value = FacilityDayInfoData
            }
    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun add_use(userInfo : UserModel, ReservationEquipment: ReservationEquipment, usingtime: Int) {
        var map1 = mutableMapOf<String, Any>()
        var map2 = mutableMapOf<String, Any>()
        var map3 = mutableMapOf<String, Any>()
        var map4 = mutableMapOf<String, Any>()

        map1["startTime"] = LocalDateTime.now().toString()
        map2["endTime"] = LocalDateTime.now().plusMinutes(usingtime.toLong()).toString()
        map3["user"] = authToken
        map4["using"] = true

        database.collection("한국장학재단_부산").document("RESERVATION")
            .collection("EQUIPMENT").document(ReservationEquipment.document_name)
            .update(map1)
        database.collection("한국장학재단_부산").document("RESERVATION")
            .collection("EQUIPMENT").document(ReservationEquipment.document_name)
            .update(map2)
        database.collection("한국장학재단_부산").document("RESERVATION")
            .collection("EQUIPMENT").document(ReservationEquipment.document_name)
            .update(map3)
        database.collection("한국장학재단_부산").document("RESERVATION")
            .collection("EQUIPMENT").document(ReservationEquipment.document_name)
            .update(map4)

        database.collection("한국장학재단_부산").document("RESERVATION")
            .collection("LOG").document(ReservationEquipment.document_name + "_" + map1["startTime"].toString())
            .set(ReservationEquipmentLog(
                    ReservationEquipment.icon,
                    ReservationEquipment.document_name,
                    userInfo.name,
                    authToken,
                    "사용중",
                    "바로 사용",
                    map1["startTime"].toString(),
                    map2["endTime"].toString()
            )
        )

        EquipmentLiveData.value = EquipmentData
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun end_use(ReservationUseEquipment: ReservationUseEquipment) {
        ReservationUseEquipment.coroutine.cancel()
        var map1 = mutableMapOf<String, Any?>()
        var map2 = mutableMapOf<String, Any?>()
        var map3 = mutableMapOf<String, Any?>()
        var map4 = mutableMapOf<String, Any>()

        map1["user"] = ""
        map2["startTime"] = ""
        map3["endTime"] = ""
        map4["using"] = false

        //LOG UPDATE
        database.collection("한국장학재단_부산").document("RESERVATION")
            .collection("LOG").document(ReservationUseEquipment.document_name + "_" + ReservationUseEquipment.startTime)
            .update("reservationState","사용 완료")
        database.collection("한국장학재단_부산").document("RESERVATION")
            .collection("LOG").document(ReservationUseEquipment.document_name + "_" + ReservationUseEquipment.startTime)
            .update("endTime",LocalDateTime.now().toString())

        //EQUIPMENT UPDATE
        database.collection("한국장학재단_부산").document("RESERVATION")
            .collection("EQUIPMENT").document(ReservationUseEquipment.document_name)
            .update(map1)
        database.collection("한국장학재단_부산").document("RESERVATION")
            .collection("EQUIPMENT").document(ReservationUseEquipment.document_name)
            .update(map2)
        database.collection("한국장학재단_부산").document("RESERVATION")
            .collection("EQUIPMENT").document(ReservationUseEquipment.document_name)
            .update(map3)
        database.collection("한국장학재단_부산").document("RESERVATION")
            .collection("EQUIPMENT").document(ReservationUseEquipment.document_name)
            .update(map4)

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
                        FacilitySelectedTimeSlotData.day_time_slot_list.sortBy { it.index }
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
            //선택취소해도 기존 데이터베이스에 남아있는 현상
            FacilitySelectedTimeSlotData.day_time_slot_list.sortBy { it.index }
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

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    fun add_reserve(userInfo : UserModel, reservationFacility: ReservationFacility) {
        var map1 = mutableMapOf<String, Any>()
        var map2 = mutableMapOf<String, Any>()
        var map3 = mutableMapOf<String, Any>()

        var tempList = mutableListOf<String>()
        var startTime :String = "default"
        var endTime :String = "default"
        val dateFmt : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val timeFmt : SimpleDateFormat = SimpleDateFormat("HH:mm:00.000")
        var startCal : Calendar = getReserveFacilityStartTime()
        var endCal: Calendar = getReserveFacilityEndTime()

        Log.e("FacilityDayInfoData.day_time_slot_list2222",FacilityDayInfoData.day_time_slot_list.toString())
        Log.e("FacilitySelectedTimeSlotData.day_time_slot_list2222",FacilitySelectedTimeSlotData.day_time_slot_list.toString())

        database.collection("한국장학재단_부산").document("RESERVATION")
            .collection("LOG").document(FacilitySelectedTimeSlotData.document_name + "_" + dateFmt.format(startCal.time) + "T" + timeFmt.format(startCal.time))
            .set(ReservationFacilityLog(
                FacilitySelectedTimeSlotData.icon,
                FacilitySelectedTimeSlotData.document_name + "_" + dateFmt.format(startCal.time) + "T" + timeFmt.format(startCal.time),
                FacilitySelectedTimeSlotData.document_name,
                userInfo.name,
                userInfo.id,
                reservationFacility.usable,
                reservationFacility.max_time,
                "예약중",
                "예약 사용",
                dateFmt.format(startCal.time) + "T" + timeFmt.format(startCal.time),
                dateFmt.format(endCal.time) + "T" + timeFmt.format(endCal.time),
                FacilitySelectedTimeSlotData.day_time_slot_list
                )
            )

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
        map2["test"] = FacilitySelectedTimeSlotData
        map3["user"] = authToken

        database.collection("한국장학재단_부산").document("RESERVATION")
            .collection("FACILITY").document(FacilitySelectedTimeSlotData.document_name)
            .update(map1)

//        database.collection("한국장학재단_부산").document("RESERVATION")
//            .collection("LOG").document(FacilitySelectedTimeSlotData.document_name + "_" + LocalDateTime.now())
//            .set(FacilitySelectedTimeSlotData.day_time_slot_list[0])



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

    fun weekdayStringToInt(weekdayString : String) : Int{
        return when(weekdayString) {
            "monday" -> 2
            "tuesday" -> 3
            "wednesday" -> 4
            "thursday" -> 5
            "friday" -> 6
            "saturday" -> 7
            "sunday" -> 1
            else -> 1
        }
    }

    fun getReserveFacilityStartTime() : Calendar{
        var startCal : Calendar = Calendar.getInstance()
        startCal.set(Calendar.DAY_OF_WEEK, weekdayStringToInt(FacilitySelectedTimeSlotData.weekday))
        startCal.set(Calendar.HOUR_OF_DAY,Math.toIntExact(FacilitySelectedTimeSlotData.day_time_slot_list.first().data?.hour!!))
        startCal.set(Calendar.MINUTE,Math.toIntExact(FacilitySelectedTimeSlotData.day_time_slot_list.first().data?.min!!))

        return startCal
    }

    fun getReserveFacilityEndTime() : Calendar{
        var endCal : Calendar = Calendar.getInstance()
        endCal.set(Calendar.DAY_OF_WEEK, weekdayStringToInt(FacilitySelectedTimeSlotData.weekday))
        endCal.set(Calendar.HOUR_OF_DAY,Math.toIntExact(FacilitySelectedTimeSlotData.day_time_slot_list.last().data?.hour!!))
        endCal.set(Calendar.MINUTE,Math.toIntExact(FacilitySelectedTimeSlotData.day_time_slot_list.last().data?.min!!))
        endCal.add(Calendar.MINUTE,Math.toIntExact(FacilitySelectedTimeSlotData.interval_time))

        return endCal
    }
}

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