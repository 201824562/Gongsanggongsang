package com.parasol.userapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.parasol.userapp.data.dto.UserModel
import com.parasol.userapp.data.model.Agency

@Entity(tableName = "user")
data class User(
    @PrimaryKey @ColumnInfo(name = "id")
    var id: String = "",
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "nickname")
    var nickname: String = "",
    @ColumnInfo(name = "birth")
    var birth: String = "",
    @ColumnInfo(name = "phone")
    var phone: String = "",
    @ColumnInfo(name = "agency")
    var agency: String = ""
){
    fun getUserModel() : UserModel { return UserModel(id, name, nickname, birth, phone, agency)}
}