package com.example.adminapp.data.model

data class RemoteUserInfo (
    val id: String = "",
    val fcmToken : ArrayList<String> = arrayListOf(),
)