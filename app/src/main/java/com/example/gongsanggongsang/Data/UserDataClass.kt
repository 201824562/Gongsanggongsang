package com.example.gongsanggongsang.Data

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey


data class UserDataClass(  //xml에 binding으로 뷰와 연결해줌.
    val id: String,
    val pwd: String,
    val name: String,
    val nickname: String,
    val birthday: String,
    val allowed: Boolean
)

