package com.parasol.adminapp.data.entity

import androidx.room.*
import com.parasol.adminapp.data.model.AdminModel

@Entity(tableName = "admin")
data class Admin(
    @PrimaryKey @ColumnInfo(name = "id")
    var id: String = "",
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "birth")
    var birth: String = "",
    @ColumnInfo(name = "phone")
    var phone: String = "",
    @ColumnInfo(name = "agency")
    var agency: String = ""
){
    fun getAdminModel() : AdminModel { return AdminModel(agency, id, name, birth,phone) }
}