package com.example.gongsanggongsangAdmin.Fifth

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.gongsanggongsangAdmin.data.AppDatabase
import com.example.gongsanggongsangAdmin.data.UserDataClass
import com.google.firebase.firestore.FirebaseFirestore

class AdministerRepository(database: AppDatabase) {

    val firestore = FirebaseFirestore.getInstance()
    var Final_Alluserlist : MutableLiveData<List<UserDataClass>> = MutableLiveData()

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

    fun updateAllusers() {

        var Alluserlist : MutableList<UserDataClass> = mutableListOf()

        firestore.collection("USER_Data")
            .whereEqualTo("allowed", false)
            .get()
            .addOnSuccessListener { documents ->
                Log.d(ContentValues.TAG, "Signup Successed!!")
                for (document in documents) {
                    Alluserlist.add(UserDataClass(document.data["id"].toString(), document.data["pwd"].toString(), document.data["name"].toString(),
                        document.data["nickname"] as String, document.data["birthday"].toString(), document.data["smsinfo"].toString(), document.data["allowed"] as Boolean
                    ))
                }
                Final_Alluserlist.value = Alluserlist
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }

    fun acceptUser(userdata : UserDataClass){
        var map= mutableMapOf<String,Any>()
        map["allowed"] = true
        firestore.collection("USER_Data").document(userdata.id)
            .update(map)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "Signup Successed!!")
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }

    fun denyUser(userdata: UserDataClass){
        firestore.collection("USER_Data").document(userdata.id)
            .delete()
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "Signup Successed!!")
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }


}