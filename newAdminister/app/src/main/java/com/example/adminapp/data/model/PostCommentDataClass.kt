package com.example.adminapp.data.model



data class PostCommentDataClass (
    var commentName : String = "",
    var commentContents : String = "",
    var commentDate : String = "",
    var commentTime : String = "",
    var commentAnonymous : Boolean = false,
    var commentId : String = "",
)
