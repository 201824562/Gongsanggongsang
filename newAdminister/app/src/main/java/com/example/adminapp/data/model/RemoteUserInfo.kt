package com.example.adminapp.data.model

data class RemoteUserInfo (
    val id: String,
    val pwd: String,
    val name: String,
    val fcmToken : ArrayList<String>,
    val nickname: String,
    val birthday: String,
    val smsinfo : String,
    var allowed: Boolean
)