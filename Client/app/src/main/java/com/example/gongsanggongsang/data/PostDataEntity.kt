package com.example.gongsanggongsang.data

import androidx.room.ColumnInfo
import androidx.room.Entity


@Entity(tableName = "POST_TABLE")
data class PostDataEntity(
    @ColumnInfo(name = "post_category")
    var post_category : String,
    @ColumnInfo(name = "post_name")
    var post_name : String,
    @ColumnInfo(name = "post_title")
    var post_title : String,
    @ColumnInfo(name = "post_contents")
    var post_contents : String,
    @ColumnInfo(name = "post_date")
    var post_date : String,
    @ColumnInfo(name = "post_comments")
    var post_comments : ArrayList<PostCommentDataClass>
)


