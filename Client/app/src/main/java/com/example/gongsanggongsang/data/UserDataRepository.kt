package com.example.gongsanggongsang.data

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.gongsanggongsang.Login.LoginDataClass
import com.google.firebase.firestore.FirebaseFirestore

class UserDataRepository(database: AppDatabase) {

    private val userDataDao = database.userDataDao()
    var firestore = FirebaseFirestore.getInstance()   //.getReference()

    companion object {
        private var sInstance: UserDataRepository? = null
        fun getInstance(database: AppDatabase): UserDataRepository {
            return sInstance
                    ?: synchronized(this) {
                        val instance = UserDataRepository(database)
                        sInstance = instance
                        instance
                    }
        }
    }

    val isLoginDataValid = MutableLiveData<Boolean>(false)

    fun insert(it: UserDataClass) {
        firestore.collection("USER_INFO_WAITING").document(it.id)
                .set(it)
                .addOnSuccessListener {success ->
                    Log.d(ContentValues.TAG, "Signup Successed!!")
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents: ", exception)
                }
    }

    fun checkLoginValid(it : LoginDataClass) {
        firestore.collection("USER_INFO").document(it.id)
                .get()
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "Signup Successed!!")
                    if (it.exists()) isLoginDataValid.postValue(true)
                    else isLoginDataValid.postValue(false)
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents: ", exception)
                }
    }


    //userDataDao.insert(mappingUserDataItemToUserDataEntity(it))
   fun getUser() : LiveData<UserDataClass>{
       return Transformations.map(userDataDao.getUser()){mappingUserDataEntityToUserDataItem(it)}   //토큰 관리용
   }



    private fun mappingUserDataItemToUserDataEntity(it : UserDataClass) : UserDataEntity {
        return (UserDataEntity(it.id, it.pwd, it.name, it.nickname, it.birthday, it.smsinfo, it.allowed))
    }

    private fun mappingUserDataEntityToUserDataItem(it : UserDataEntity) : UserDataClass {
        return (UserDataClass(it.id, it.pwd, it.name, it.nickname, it.birthday, it.smsinfo, it.allowed))
    }

}

