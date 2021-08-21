package com.example.userapp.data.model

import com.example.userapp.data.entity.DayTimeSlot

data class ReservationFacilityLog(
    val icon: Long,
    val documentId: String,
    val name: String,
    val userName: String,
    val userId: String,
    var usable: Boolean,
    var maxTime: Long,
    val reservationState: String,
    val reservationType: String,
    val startTime: String,
    val endTime: String,
    var day_time_slot_list: MutableList<DayTimeSlot>
)
