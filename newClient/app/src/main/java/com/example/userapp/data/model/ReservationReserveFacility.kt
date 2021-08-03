package com.example.userapp.data.model

data class ReservationReserveFacility(
    val document_name: String,
    val name: String,
    val month: Long,
    val day: Long,
    val weekday: String,
    val timeSlotList: List<String>
)
