package com.example.userapp.data.model

import com.example.userapp.data.entity.ReservationTimeData

data class DayTimeSlotForFirebaseUpdate(
    var buttonSelected: Boolean,
    var data: ReservationTimeData?,
    var index: Long,
    var user: String,
)

