package com.example.gongsanggongsang.data

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Query

@Dao
interface UserDataDao {


    @Query("SELECT * from USER_SIGNUP_TABLE ORDER BY primary_id ASC LIMIT 1")
    fun getUser(): LiveData<UserDataEntity>   //사용자용

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userEntity: UserDataEntity)



}