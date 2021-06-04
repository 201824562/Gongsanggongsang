package com.example.userapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

data class PostCommentDataClass (
    var name : String,
    var contents : String,
    var date : String
)
