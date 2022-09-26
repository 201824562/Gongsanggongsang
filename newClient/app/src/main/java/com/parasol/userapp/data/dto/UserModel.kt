package com.parasol.userapp.data.dto

import com.parasol.userapp.data.entity.User

data class UserModel(
    val id: String = "",
    val name: String = "",
    val nickname: String = "",
    val birth: String = "",
    val phone: String = "",
    val agency : String = ""/*,
    val fcmTokenList : List<String> = listOf()*/
){
    fun getUserEntity() : User { return User(id, name, nickname, birth, phone, agency) }
}