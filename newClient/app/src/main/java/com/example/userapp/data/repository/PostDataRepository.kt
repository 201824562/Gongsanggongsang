package com.example.userapp.data.repository

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.userapp.data.entity.PostCommentDataClass
import com.example.userapp.data.model.PostDataInfo
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Observable

class PostDataRepository() {

    private val firestore = FirebaseFirestore.getInstance()
    private var collectionPostDataInfoList : MutableLiveData<ArrayList<PostDataInfo>> = MutableLiveData()
    private var documentPostDataInfo : MutableLiveData<PostDataInfo> = MutableLiveData()
    companion object {
        private var sInstance: PostDataRepository? = null
        fun getInstance(): PostDataRepository {
            return sInstance
                ?: synchronized(this) {
                    val instance = PostDataRepository()
                    sInstance = instance
                    instance
                }
        }
    }

    fun insertPostData(it: PostDataInfo){
        var collection_name = it.post_category
        var document_name = it.post_title + it.post_date
        firestore.collection(collection_name).document(document_name)
            .set(it)
            .addOnSuccessListener {success ->
                Log.d(ContentValues.TAG, "Post Success")
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error", exception)
            }
    }
    fun insertPostCommentData(collection_name: String, document_name: String, post_comments_array : ArrayList<PostCommentDataClass>){
        var map= mutableMapOf<String,Any>()
        map["post_comments"] = post_comments_array
        firestore
            .collection(collection_name)
            .document(document_name)
            .update(map)
            .addOnSuccessListener {

            }
    }

    fun updateCollectionPostData(collection_name: String) {
        var postDataList : ArrayList<PostDataInfo> = ArrayList<PostDataInfo>()
        firestore.collection(collection_name)
            .get()
            .addOnSuccessListener { result ->
                for (document in result){
                    val item = PostDataInfo(
                        document["post_category"] as String,
                        document["post_name"] as String,
                        document["post_title"] as String,
                        document["post_contents"] as String,
                        document["post_date"] as String,
                        document["post_comments"] as ArrayList<PostCommentDataClass>)
                    postDataList.add(item)
                }
                collectionPostDataInfoList.value= postDataList
            }
    }

    fun getCollectionPostData(collection_name: String) : MutableLiveData<ArrayList<PostDataInfo>>{
        updateCollectionPostData(collection_name)
        return collectionPostDataInfoList
    }

    fun updateDocumentPostData(collection_name: String, document_name : String){
        firestore.collection(collection_name).document(document_name)
            .get()
            .addOnSuccessListener { result ->
                var postData = PostDataInfo(
                    result["post_category"].toString(),
                    result["post_name"].toString(),
                    result["post_title"].toString(),
                    result["post_contents"].toString(),
                    result["post_date"].toString(),
                    arrayListOf())
                documentPostDataInfo.value = postData
            }
    }
    fun getDocumentPostData(collection_name: String, document_name: String) : MutableLiveData<PostDataInfo>{
        updateDocumentPostData(collection_name, document_name)
        return documentPostDataInfo
    }
    fun deleteDocumentPostData(collection_name: String, document_name: String) {
        firestore.collection(collection_name).document(document_name).delete()
    }

}