package com.example.userapp.data.entity

data class DayTimeSlot(
    var buttonSelected: Boolean,
    var data: ReservationTimeData?,
    var index: Long,
    var user: String
//    var weekday: Int
){
    fun modifyBtnSlctFalse(dayTimeSlot: DayTimeSlot){
        dayTimeSlot.buttonSelected = false
    }
}

data class ReservationTimeData (val hour : Long, val min: Long){
}