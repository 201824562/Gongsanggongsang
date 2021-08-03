package com.example.userapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

data class PostCommentDataClass (
    var commentName : String,
    var commentContents : String,
    var commentDate : String,
    var commentTime : String,
    var commentAnonymous : Boolean,
    var commentId : String,
)
