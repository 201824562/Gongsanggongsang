package com.example.gongsanggongsangAdmin.data


data class UserDataClass(  //xml에 binding으로 뷰와 연결해줌.
    val id: String,
    val pwd: String,
    val name: String,
    val nickname: String,
    val birthday: String,
    val smsinfo : String,
    var allowed: Boolean
)

