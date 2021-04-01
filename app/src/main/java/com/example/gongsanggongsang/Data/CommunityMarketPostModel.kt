package com.example.gongsanggongsang.Data

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.sql.Time
import java.time.LocalDateTime
import java.util.*

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


