package com.parasol.adminapp.data.model

data class RemoteUserInfo (
    val id: String = "",
    val fcmToken : ArrayList<String> = arrayListOf(),
)