package com.example.gongsanggongsang.data

import androidx.room.ColumnInfo
import androidx.room.Entity


@Entity(tableName = "CommunityPost")
data class CommunityPostModel(
    @ColumnInfo(name = "name")
    var name : String,
    @ColumnInfo(name = "title")
    var title : String,
    @ColumnInfo(name = "contents")
    var contents : String,
    @ColumnInfo(name = "date")
    var date : String,
    @ColumnInfo(name = "comments")
    var comments : ArrayList<CommunityCommentsModel>
)


