package com.example.adminapp
/*
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.userapp.data.dao.ReservationDao
import com.example.userapp.data.entity.ReservationEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Database(entities = [ReservationEntity::class], version=1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun reservationDao() : ReservationDao

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
                        ReservationDatabase(appdb.reservationDao())
                    }
                }
            }
            suspend fun ReservationDatabase(reservationDao: ReservationDao){}

        }

}*/
