package com.example.adminapp.data.dao

import androidx.room.*
import com.example.adminapp.data.entity.CategoryEntity
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategoryItemData(categoryEntityResource : CategoryEntity) : Completable

    @Query("SELECT * from category WHERE userID =:token ORDER BY id ASC")
    fun getCategoryItemList(token : String) : Single<List<CategoryEntity>>

    @Query("DELETE from category WHERE drawableID=:drawableID AND name=:name AND userID=:token")
    fun deleteCategoryItemData(drawableID : Int, name : String, token : String)  : Completable

/*
    @Query("SELECT * from admin ORDER BY id DESC LIMIT 1 ")
    fun getAdminData() : LiveData<Admin>

    @Query("SELECT id from admin ORDER BY id DESC LIMIT 1 ")
    fun getAdminToken() : String?*/
}