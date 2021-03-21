package com.example.gongsanggongsang.Data

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.firebase.firestore.FirebaseFirestore

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

    fun insert(it: UserDataClass) {

        var firestore = FirebaseFirestore.getInstance()   //.getReference()

        firestore.collection("USER_Data").document() //이거 무작위로 부여되네. (수정 필요하지 않나? ) -> 나중에 고려하기.
            .set(it)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "Signup Successed!!")
                }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }

    }

}