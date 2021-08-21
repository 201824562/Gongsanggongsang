package com.example.adminapp.data.model

import com.example.adminapp.data.entity.Admin

data class AdminModel(
    val agency : String = "",
    val id: String = "",
    val name: String = "",
    val birth: String = "",
    val phone: String = ""
){
    fun getAdminEntity() : Admin { return Admin(id, name, birth, phone, agency) }
}