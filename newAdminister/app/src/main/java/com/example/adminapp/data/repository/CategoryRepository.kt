package com.example.adminapp.data.repository

import com.example.adminapp.data.AppDatabase
import com.example.adminapp.data.entity.CategoryEntity
import com.example.adminapp.data.model.CategoryData
import com.example.adminapp.data.model.CategoryItem
import com.example.adminapp.data.model.RecyclerDataType
import io.reactivex.Completable
import io.reactivex.Single

class CategoryRepository(appDatabase: AppDatabase) {
    companion object {
        private var sInstance: CategoryRepository? = null
        fun getInstance(database: AppDatabase): CategoryRepository {
            return sInstance
                ?: synchronized(this) {
                    val instance = CategoryRepository(database)
                    sInstance = instance
                    instance
                }
        }
    }

    private val categoryDao = appDatabase.categoryDao()

    fun getCategoryItemInfoList(token : String) : Single<List<CategoryItem>> {
        return categoryDao.getCategoryItemList(token).map { list-> list.map { it.makeCategoryItem() } }
    }

    fun saveCategoryItemInfo(token: String, item : CategoryItem) : Completable {
        return categoryDao.insertCategoryItemData(item.makeCategoryEntity(token))
    }

    fun deleteCategoryItemInfo(token: String, data : CategoryData) : Completable {
        return categoryDao.deleteCategoryItemData(data.drawableID, data.name, token)
    }

}