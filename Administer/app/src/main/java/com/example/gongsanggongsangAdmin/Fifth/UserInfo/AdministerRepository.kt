package com.example.gongsanggongsangAdmin.Fifth.UserInfo

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.gongsanggongsangAdmin.data.AppDatabase
import com.example.gongsanggongsangAdmin.data.UserDataClass
import com.google.firebase.firestore.FirebaseFirestore

class AdministerRepository(database: AppDatabase) {

    val firestore = FirebaseFirestore.getInstance()
    var Final_AllWaitinguserlist : MutableLiveData<List<UserDataClass>> = MutableLiveData()

    companion object {
        private var sInstance: AdministerRepository? = null
        fun getInstance(database: AppDatabase): AdministerRepository {
            return sInstance
                ?: synchronized(this) {
                    val instance = AdministerRepository(database)
                    sInstance = instance
                    instance
                }
        }
    }

    fun updateAllWaitingUsers() {
        var Allwaitinguserlist : MutableList<UserDataClass> = mutableListOf()

        firestore.collection("USER_INFO_WAITING")
            .whereEqualTo("allowed", false)
            .get()
            .addOnSuccessListener { documents ->
                Log.d(ContentValues.TAG, "Signup Successed!!")
                for (document in documents) {
                    Allwaitinguserlist.add(UserDataClass(document.data["id"].toString(), document.data["pwd"].toString(), document.data["name"].toString(),
                        document.data["nickname"] as String, document.data["birthday"].toString(), document.data["smsinfo"].toString(), document.data["allowed"] as Boolean
                    ))
                }
                Final_AllWaitinguserlist.value = Allwaitinguserlist
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }

    fun acceptUser(userdata: UserDataClass) {
        deleteWaitingUser(userdata) //대기목록에서 삭제
        userdata.allowed = true //승인여부
        firestore.collection("USER_INFO").document(userdata.id) //회원목록에 등록시켜줌.
            .set(userdata)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "Signup Successed!!")
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }

    fun deleteUser(userdata: UserDataClass){    //최종 User를 삭제하는 거!!!!!!!!!!!!!!!!!!!!!!
        firestore.collection("USER_INFO").document(userdata.id)
            .delete()
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "Signup Successed!!")
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }

    fun deleteWaitingUser(userdata: UserDataClass){
        firestore.collection("USER_INFO_WAITING").document(userdata.id)
            .delete()
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "Signup Successed!!")
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }



}