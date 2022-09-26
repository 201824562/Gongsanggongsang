package com.example.userapp.data.model

import android.os.CountDownTimer

data class ReservationEquipment(
    val document_name: String,
    var using: String,
    var endTime: String,
    val icon: Int,
    var maxTime: Long
)
