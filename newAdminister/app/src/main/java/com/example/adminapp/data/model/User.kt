package com.example.adminapp.data.model

data class User(
    val agency : String = "",
    val id: String = "",
    val pwd: String,
    val name: String = "",
    val nickname: String = "",
    val birthday: String,
    val smsInfo : String,
    var allowed: Boolean
)