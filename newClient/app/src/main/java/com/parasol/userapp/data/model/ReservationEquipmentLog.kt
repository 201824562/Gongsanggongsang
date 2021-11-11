package com.parasol.userapp.data.model

data class ReservationEquipmentLog(
    val documentId: String,
    val icon: String,
    val name: String,
    val userName: String,
    val userId: String,
    val reservationState: String,
    val reservationType: String,
    val startTime: String,
    val endTime: String
)
