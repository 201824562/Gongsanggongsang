package com.example.userapp.data.model

data class ReservationEquipment(
    val document_name: String,
    var using: String,
    var endTime: String,
    val icon: Int,
    var maxTime: Long
)
