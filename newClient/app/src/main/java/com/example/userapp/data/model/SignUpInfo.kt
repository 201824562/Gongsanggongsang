package com.example.userapp.data.model

data class SignUpInfo (
    val agency : String,
    val name: String,
    val birthday: String,
    val smsInfo : String,
    val id: String,
    val pwd: String,
    val nickname: String,
    var allowed: Boolean){
    fun makeToSignUpAlarmData() : SignUpAlarmData = SignUpAlarmData(agency, id, pwd, name, nickname, birthday,
    smsInfo, allowed)
}
