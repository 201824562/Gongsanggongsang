package com.example.adminapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.adminapp.data.dao.AdminDao
import com.example.adminapp.data.dao.CategoryDao
import com.example.adminapp.data.entity.AdminEntity
import com.example.adminapp.data.entity.CategoryEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [AdminEntity::class, CategoryEntity::class], version=2, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun adminDao() : AdminDao
    abstract fun categoryDao() : CategoryDao

    companion object{
        @Volatile
        private var INSTANCE : AppDatabase? = null
        fun getDatabase(context : Context, scope: CoroutineScope) :AppDatabase {
            return INSTANCE?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "AppDB"
                )
                    .addCallback(AppDatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

    }
    private class AppDatabaseCallback(private val scope : CoroutineScope) : RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let{ appdb ->
                scope.launch {
                    adminDatabase(appdb.adminDao())
                    categoryResource(appdb.categoryDao())
                }
            }
        }
        suspend fun adminDatabase(userDao: AdminDao){}
        suspend fun categoryResource(categoryDao : CategoryDao) {}
    }

}