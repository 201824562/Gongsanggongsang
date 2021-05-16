package com.example.gongsanggongsang.data

import android.content.ContentValues
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class PostDataRepository(database: AppDatabase) {

    private lateinit var postDataDao : PostDataDao
    var firestore = FirebaseFirestore.getInstance()

    companion object {
        private var sInstance: PostDataRepository? = null
        fun getInstance(database: AppDatabase): PostDataRepository {
            return sInstance
                ?: synchronized(this) {
                    val instance = PostDataRepository(database)
                    sInstance = instance
                    instance
                }
        }
    }

    fun insertPostData(it: PostDataClass) {
        var collection_name = it.post_category
        var document_name = it.post_title + it.post_date
        firestore.collection(collection_name).document(document_name)
            .set(it)
            .addOnSuccessListener {success ->
                Log.d(ContentValues.TAG, "Post Success Successed!!")
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }



}