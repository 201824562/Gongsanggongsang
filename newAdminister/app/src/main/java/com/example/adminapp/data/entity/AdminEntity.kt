package com.example.adminapp.data.entity

import androidx.lifecycle.LiveData
import androidx.room.*
import com.google.firebase.firestore.auth.User
import io.reactivex.Completable

@Entity(tableName = "admin")
data class AdminEntity(
    @PrimaryKey @ColumnInfo(name = "id")
    var id: String = "",
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "phone")
    var phone: String = "",
    @ColumnInfo(name = "agency")
    var agency: String = ""
)