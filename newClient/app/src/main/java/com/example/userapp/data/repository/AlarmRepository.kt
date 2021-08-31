package com.example.userapp.data.repository

import com.example.userapp.data.AppDatabase

class AlarmRepository (appDatabase: AppDatabase) {

    companion object {
        private var sInstance: AlarmRepository? = null
        fun getInstance(database: AppDatabase): AlarmRepository {
            return sInstance
                ?: synchronized(this) {
                    val instance = AlarmRepository(database)
                    sInstance = instance
                    instance
                }
        }
    }

}