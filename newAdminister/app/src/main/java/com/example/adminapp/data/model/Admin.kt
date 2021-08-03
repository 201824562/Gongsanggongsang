package com.example.adminapp.data.model

import com.example.adminapp.data.entity.AdminEntity

data class Admin(
    val agency : String = "",
    val id: String = "",
    val name: String = "",
    val phone: String = ""
){
    fun makeAdminEntity() : AdminEntity { return AdminEntity(id, name, phone, agency) }
}