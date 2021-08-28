package com.example.userapp.data.model

import com.example.userapp.data.entity.DayTimeSlot

data class ReservationFacilityDayInfo(
    var document_name: String,
    var icon : Int,
    var interval_time: Long,
    var weekday: String,
    var day_time_slot_list: MutableList<DayTimeSlot>
)