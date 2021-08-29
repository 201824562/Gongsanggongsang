package com.example.adminapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

data class PostCommentDataClass (
    var commentName : String = "",
    var commentContents : String = "",
    var commentDate : String = "",
    var commentTime : String = "",
    var commentAnonymous : Boolean = false,
    var commentId : String = "",
)
