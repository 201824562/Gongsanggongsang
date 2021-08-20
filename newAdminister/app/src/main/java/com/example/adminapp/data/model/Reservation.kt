package com.example.adminapp.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.time.LocalTime

@Parcelize
data class ReservationFacilityBundle(var using : Boolean, val name : String, var logData :  @RawValue ReservationFacilityLog?, var settingData : @RawValue ReservationFacilitySettingData? ) : Parcelable

data class ReservationLogItem(val type : ReservationType, val equipmentLog : ReservationEquipmentLog?, val facilityLog: ReservationFacilityLog? )

data class ReservationEquipmentLog(val icon : Int, val name : String, val userId : String="", val userName : String="",
                                   val reservationState : String = "", val reservationType : String = "", val startTime: String = "", val endTime: String = "")

data class ReservationFacilityLog(val icon : Int, val name : String, val userId : String="", val userName : String="",
                                    val reservationState : String = "", val reservationType : String = "",val startTime: String = "", val endTime: String = "",
                                    val documentId : String, val maxTime: Long, val usable: Boolean)

data class ReservationEquipmentItem(val data : ReservationEquipmentSettingData, val equipmentData : ReservationEquipmentData)

data class ReservationFacilityItem(val data : ReservationFacilitySettingData, val unableTimeList : List<ReservationUnableTimeItem>){
    fun makeReservationFacilityListData() : ReservationFacilityListData {
        val list = unableTimeList.mapIndexed { index, it ->
            val user = when (it.unable){
                true -> "X"
                else -> "Nope" }
            ReservationFacilityData(index, user, it.data) }
        return ReservationFacilityListData(/*data.name, data.unableTimeList[0].type, */ list, list, list, list, list, list, list)
    }
}

@Parcelize
data class ReservationEquipmentData(val icon : Int, val name : String, val user : String="", val startTime: String = "", val endTime: String = "", val intervalTime: Long=0L, val using: Boolean = false, val usable : Boolean = true , val maxTime : Long) :
    Parcelable

data class ReservationFacilityData(val index : Int, val user: String, val data : ReservationTimeData, var buttonSelected : Boolean = false)

data class ReservationFacilityListData(/*val name : String, val type : ReservationUnableTimeType, */val monday : List<ReservationFacilityData>, val tuesday : List<ReservationFacilityData>,
                                       val wednesday : List<ReservationFacilityData>, val thursday : List<ReservationFacilityData>, val friday : List<ReservationFacilityData>,
                                       val saturday : List<ReservationFacilityData>, val sunday : List<ReservationFacilityData>)
@Parcelize
data class ReservationEquipmentSettingData(val icon : Int, val name : String, val maxTime : Long) :
    Parcelable {
    fun getMaxTimeView() : String = maxTime.toString() + "분"
}

data class ReservationFacilitySettingData(val icon : Int, val name : String, val intervalTime: Long, val maxTime : Long, val unableTimeList : List<ReservationUnableTimeItem>, val usable : Boolean = true){
    fun getIntervalTimeView() : String = intervalTime.toString() + "분"
    fun getMaxTimeView() : String = maxTime.toString() + "분"
}

@Parcelize
data class ReservationItem(val type: ReservationType, val data : @RawValue ReservationData, var unableTimeList : @RawValue List<ReservationUnableTimeItem>) : Parcelable

data class ReservationData(var icon : Int, var name : String, var intervalTime : Long = 0L, var maxTime : Long)

@Parcelize
enum class ReservationType : Parcelable { EQUIPMENT, FACILITY }
