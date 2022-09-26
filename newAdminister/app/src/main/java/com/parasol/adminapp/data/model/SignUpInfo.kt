package com.parasol.adminapp.data.model

data class SignUpInfo (
    val id: String,
    val pwd: String,
    val name: String,
    val nickname: String,
    val birthday: String,
    val smsinfo : String,
    var allowed: Boolean
    )