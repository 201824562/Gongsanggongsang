package com.example.gongsanggongsang.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PostDataDao {
    @Query("SELECT * from POST_TABLE ORDER BY post_date ASC LIMIT 1")
    fun getPostData(): LiveData<PostDataEntity>   //사용자용

    @Insert
    fun insertPostData(postDataEntity: PostDataEntity)

}