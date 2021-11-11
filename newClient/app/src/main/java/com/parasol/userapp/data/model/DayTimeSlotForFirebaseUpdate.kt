package com.parasol.userapp.data.model

import com.parasol.userapp.data.entity.ReservationTimeData

data class DayTimeSlotForFirebaseUpdate(
    var buttonSelected: Boolean,
    var data: ReservationTimeData?,
    var index: Long,
    var user: String,
)

