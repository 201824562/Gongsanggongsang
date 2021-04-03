package com.example.gongsanggongsang.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//[관리자용]
@Entity(tableName = "USER_SIGNUP_TABLE")
data class UserDataEntity(
    @ColumnInfo(name="ID")
    val id: String,

    @ColumnInfo(name="PASSWORD")
    val pwd: String,

    @ColumnInfo(name="NAME")
    val name: String,

    @ColumnInfo(name="NICKNAME")
    val nickname: String,

    @ColumnInfo(name="BIRTHDAY")
    val birthday: String,

    @ColumnInfo(name="ALLOWED")
    val allowed: Boolean = false,

    @PrimaryKey(autoGenerate = true)
    val primary_id: Int = 0

)