package com.example.userapp.data.model

import kotlinx.coroutines.CoroutineScope

data class ReservationUseEquipment(
    val document_name: String,
    var user: String,
    var startTime: String,
    var endTime: String,
    var remain_time: Long = 0,
    val icon : Long,
    var coroutine: CoroutineScope
)
