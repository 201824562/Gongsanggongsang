package com.parasol.adminapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.parasol.adminapp.data.entity.Admin

import io.reactivex.Completable

@Dao
interface AdminDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAdminData(adminData : Admin) : Completable

    @Query("DELETE from admin WHERE id=:userId")
    fun deleteAdminData(userId : String)

    @Query("SELECT * from admin ORDER BY id DESC LIMIT 1 ")
    fun getAdminData() : Admin?

    @Query("SELECT id from admin ORDER BY id DESC LIMIT 1 ")
    fun getAdminToken() : String?
}