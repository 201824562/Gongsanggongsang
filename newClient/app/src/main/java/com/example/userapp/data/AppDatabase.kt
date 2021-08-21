package com.example.userapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.userapp.data.dao.UserDao
import com.example.userapp.data.entity.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Database(entities = [User::class], version=2, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun userDao() : UserDao

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
                        UserDatabase(appdb.userDao())
                    }
                }
            }
            suspend fun UserDatabase(userDao: UserDao){}
        }

}
