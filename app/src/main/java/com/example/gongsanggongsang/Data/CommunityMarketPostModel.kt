package com.example.gongsanggongsang.Data

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "CommunityPost")
data class CommunityMarketPostModel(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = 0,
    @ColumnInfo(name = "name")
    var name : String,
    @ColumnInfo(name = "title")
    var title : String,
    @ColumnInfo(name = "contents")
    var contents : String,

){
    constructor():this(null,"", "", "")
}


