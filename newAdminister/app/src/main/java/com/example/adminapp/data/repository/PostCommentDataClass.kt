package com.example.adminapp.data.repository

import androidx.room.ColumnInfo
import androidx.room.Entity

data class PostCommentDataClass (
    var name : String,
    var contents : String,
    var date : String
)
