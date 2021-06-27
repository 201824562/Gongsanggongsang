package com.example.userapp.data.model

data class ReservationUseEquipment(
    val document_name: String,
    var name: String,
    var start_time: String,
    var end_time: String,
    var remain_time: Long = 0,
    val category : String
)
