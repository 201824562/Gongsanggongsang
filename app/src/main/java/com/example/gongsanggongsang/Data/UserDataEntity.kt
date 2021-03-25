package com.example.gongsanggongsang.Data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "USER_SIGNUP_TABLE")
data class UserDataEntity(
    @ColumnInfo(name="ID")
    val id: String,

    @ColumnInfo(name="PASSWORD")
    val pwd: String,

    @ColumnInfo(name="NICKNAME")
    val nickname: String?,

    @PrimaryKey(autoGenerate = true)
    val primary_id: Int = 0

)