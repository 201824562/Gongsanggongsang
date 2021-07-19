package com.example.userapp.data.model

data class SignUpInfo (
    val agency : String,
    val name: String,
    val birthday: String,
    val SmsInfo : String,
    val id: String,
    val pwd: String,
    val nickname: String,
    var allowed: Boolean
    )