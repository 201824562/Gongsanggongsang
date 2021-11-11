package com.parasol.adminapp.data.entity

import android.graphics.drawable.Drawable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.parasol.adminapp.data.model.CategoryData
import com.parasol.adminapp.data.model.CategoryItem
import com.parasol.adminapp.data.model.RecyclerDataType

@Entity(tableName = "category")
data class CategoryEntity (
    @ColumnInfo(name = "drawableID")
    var drawableID: Int,
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "userID")
    var token: String = "",
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    var id : Int = 0
){
    fun makeCategoryItem() : CategoryItem { return CategoryItem(RecyclerDataType.DATA, CategoryData(drawableID, name))  }
}