package com.example.userapp.data.dto

import com.example.userapp.data.entity.User

data class UserModel(
    val agency : String = "",
    val id: String = "",
    val name: String = "",
    val nickname: String = "",
    val birth: String = "",
    val phone: String = ""
){
    fun getUserEntity() : User { return User(id, name, nickname, birth, phone, agency) }
}