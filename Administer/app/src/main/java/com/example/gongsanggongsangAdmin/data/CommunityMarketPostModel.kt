package com.example.gongsanggongsangAdmin.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CommunityPost")
data class CommunityMarketPostModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    @ColumnInfo(name = "name")
    var name : String,
    @ColumnInfo(name = "title")
    var title : String,
    @ColumnInfo(name = "contents")
    var contents : String,
    @ColumnInfo(name = "date")
    var date : String
)


