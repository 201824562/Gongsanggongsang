package com.example.gongsanggongsang.Data

import androidx.lifecycle.LiveData

class UserDataRepository(database: AppDatabase)  {

    private val userDataDao = database.userDataDao()
    val allUsers: LiveData<List<UserDataEntity>> = userDataDao.getALLUsers()

    companion object{
        private var sInstance: UserDataRepository? = null
        fun getInstance(database: AppDatabase): UserDataRepository {
            return sInstance
                ?: synchronized(this){
                    val instance = UserDataRepository(database)
                    sInstance = instance
                    instance
                }
        }
    }

}