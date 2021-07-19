package com.example.userapp.data.repository

import android.app.Activity
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import com.example.userapp.data.entity.PostCommentDataClass
import com.example.userapp.data.model.PostDataInfo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.net.URL

class CommunityDataRepository() {
    private val firestore = FirebaseFirestore.getInstance()
    private val fireStorage = FirebaseStorage.getInstance("gs://gongsanggongsang-94f86.appspot.com/")
    private var collectionPostDataInfoList : MutableLiveData<ArrayList<PostDataInfo>> = MutableLiveData()
    private var documentPostDataInfo : MutableLiveData<PostDataInfo> = MutableLiveData()
    private var post_photo_uri : MutableLiveData<ArrayList<String>> = MutableLiveData()
    companion object {
        private var sInstance: CommunityDataRepository? = null
        fun getInstance(): CommunityDataRepository {
            return sInstance
                ?: synchronized(this) {
                    val instance = CommunityDataRepository()
                    sInstance = instance
                    instance
                }
        }
    }
    fun getCommunityMainItem(){
        val name = firestore.collection("Busan").document("community").id
    }

    fun insertPostData(it: PostDataInfo){
        var collection_name = it.post_category
        var document_name = it.post_id
        firestore.collection("Busan").document("community").
        collection("1_free").document(document_name)
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
            .collection("Busan")
            .document("community")
            .collection(collection_name)
            .document(document_name)
            .update(map)
            .addOnSuccessListener {

            }
    }

    fun updateCollectionPostData(collection_name: String) {
        var postDataList : ArrayList<PostDataInfo> = ArrayList<PostDataInfo>()
        firestore.collection("Busan")
            .document("community")
            .collection(collection_name)
            .get()
            .addOnSuccessListener { result ->
                for (document in result){
                    val item = PostDataInfo(
                        document["post_category"] as String,
                        document["post_name"] as String,
                        document["post_title"] as String,
                        document["post_contents"] as String,
                        document["post_date"] as String,
                        document["post_time"] as String,
                        document["post_comments"] as ArrayList<PostCommentDataClass>,
                        document["post_id"] as String,
                        document["post_photo_uri"] as ArrayList<String>,
                        document["post_state"] as String,
                        document["post_anonymous"] as Boolean)
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
        firestore.collection("Busan")
            .document("community")
            .collection(collection_name)
            .document(document_name)
            .get()
            .addOnSuccessListener { result ->
                var postData = PostDataInfo(
                    result["post_category"].toString(),
                    result["post_name"].toString(),
                    result["post_title"].toString(),
                    result["post_contents"].toString(),
                    result["post_date"].toString(),
                    result["post_time"].toString(),
                    if(result["post_comments"] != null){
                        result["post_comments"] as ArrayList<PostCommentDataClass>
                    }else{
                        arrayListOf()
                    },
                    result["post_id"].toString(),
                    if(result["post_photo_uri"] != null){
                        result["post_photo_uri"] as ArrayList<String>
                    }else{
                        arrayListOf()
                    },
                    result["post_state"].toString(),
                result["post_anonymous"] as Boolean)
                documentPostDataInfo.value = postData
            }
    }
    fun getDocumentPostData(collection_name: String, document_name: String) : MutableLiveData<PostDataInfo>{
        updateDocumentPostData(collection_name, document_name)
        return documentPostDataInfo
    }
    fun deleteDocumentPostData(collection_name: String, document_name: String) {
        firestore.collection("Busan")
            .document("community")
            .collection(collection_name)
            .document(document_name).delete()
    }
    fun modifyPostData(collection_name: String, document_name: String, modifyTitle: String, modifyContent: String) {
        var titleMap = mutableMapOf<String, Any>()
        var contentMap = mutableMapOf<String, Any>()
        titleMap["post_title"] = modifyTitle
        contentMap["post_contents"] = modifyContent
        firestore.collection("Busan")
            .document("community")
            .collection(collection_name)
            .document(document_name)
            .update(titleMap)
            .addOnSuccessListener { result ->

            }

        firestore.collection("Busan")
            .document("community")
            .collection(collection_name)
            .document(document_name)
            .update(contentMap)
            .addOnSuccessListener { result ->
            }
    }
    @RequiresApi(Build.VERSION_CODES.P)
    fun uploadPhoto(bitmap_array : ArrayList<Bitmap>, uri_array : ArrayList<Uri>) {
        var i : Int = 0
        for(bitmap in bitmap_array){
            val file_name = uri_array[i].toString()
            var storageRefer : StorageReference = fireStorage.reference.child("images/").child(file_name)
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()
            var uploadTask = storageRefer.putBytes(data)
            uploadTask.addOnFailureListener {
                // Handle unsuccessful uploads
            }.addOnSuccessListener { taskSnapshot ->
                // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                // ...
                System.out.println("sucess");
            }
            i++
        }
    }
    fun getPhoto(uri_array: ArrayList<String>) : MutableLiveData<ArrayList<String>> {
        var post_data: ArrayList<String> = arrayListOf()
        for (uri in uri_array) {
            var u = "file://" + uri
            var storageRefer: StorageReference = fireStorage.reference.child("images/").child(u)
            storageRefer.downloadUrl.addOnSuccessListener {
                System.out.println(it.toString())
                post_data.add(it.toString())
            }
        }
        post_photo_uri.value = post_data
        return post_photo_uri
    }
    fun getPostPhotoData(collection_name: String, document_name: String) : MutableLiveData<ArrayList<String>>{
        firestore.collection("Busan")
            .document("community")
            .collection(collection_name)
            .document(document_name)
            .get()
            .addOnSuccessListener { result ->
                var photo_server_uri =
                    if(result["post_photo_uri"] != null){
                        result["post_photo_uri"] as ArrayList<String>
                    }else{
                        arrayListOf()
                    }
                post_photo_uri.value = photo_server_uri
            }
        return post_photo_uri
    }


}