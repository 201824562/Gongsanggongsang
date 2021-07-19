package com.example.userapp.data.model

data class SignUpInfo (
    val name: String,
    val birthday: String,
    val smsinfo : String,
    val id: String,
    val pwd: String,
    val nickname: String,
    var allowed: Boolean
    )