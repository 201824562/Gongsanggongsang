package com.example.gongsanggongsang.Data

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Query

@Dao
interface UserDataDao {

    @Query("SELECT * from USER_SIGNUP_TABLE ORDER BY primary_id ASC")
    fun getALLUsers(): LiveData<List<UserDataEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userEntity: UserDataEntity)



}