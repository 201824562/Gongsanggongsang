package com.example.userapp.data.model

data class ReservationEquipmentForUpdateFirebase(
    val documentId: String,
    var using: Boolean,
    var endTime: String,
    var startTime:String,
    var usable:Boolean,
    var name:String,
    var user:String,
    var intervalTime:Long,
    val icon: String,
    var maxTime: Long
)
