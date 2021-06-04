package com.example.gongsanggongsang.data

import androidx.room.ColumnInfo

data class PostDataClass(
    var post_category : String,

    var post_name : String,

    var post_title : String,

    var post_contents : String,

    var post_date : String,

    val  post_comments : ArrayList<PostCommentDataClass>
)