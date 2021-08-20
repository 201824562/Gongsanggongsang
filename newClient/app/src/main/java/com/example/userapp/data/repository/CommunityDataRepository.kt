package com.example.userapp.data.repository

import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.userapp.data.entity.PostCommentDataClass
import com.example.userapp.data.model.PostDataInfo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream

class CommunityDataRepository() {
    private val firestore = FirebaseFirestore.getInstance()
    private val fireStorage = FirebaseStorage.getInstance("gs://gongsanggongsang-94f86.appspot.com/")
    private var collectionPostDataInfoList : MutableLiveData<ArrayList<PostDataInfo>> = MutableLiveData()
    private var noticePostDataInfoList : MutableLiveData<ArrayList<PostDataInfo>> = MutableLiveData()
    private var documentPostDataInfo : MutableLiveData<PostDataInfo> = MutableLiveData()

    private var postDataPhotoUrl : MutableLiveData<ArrayList<String>> = MutableLiveData()
    private var postDataCommentList : MutableLiveData<ArrayList<PostCommentDataClass>> = MutableLiveData()
    private var postDataPhotoThumbnailUrl : MutableLiveData<String> = MutableLiveData()
    private var postCommentUploadSuccess : MutableLiveData<Boolean> = MutableLiveData()
    private var postCommentDeleteSuccess : MutableLiveData<Boolean> = MutableLiveData()
    private var postModifyPartDataSuccess : MutableLiveData<Boolean> = MutableLiveData()
    private var postDataInsertSuccess : MutableLiveData<Boolean> = MutableLiveData()
    private var postSearchPostDataList : MutableLiveData<ArrayList<PostDataInfo>> = MutableLiveData()
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

    fun insertPostData(agency: String, it: PostDataInfo) : MutableLiveData<Boolean> {
        var collectionName = it.post_category
        var documentName = it.post_id
        var isSuccess : Boolean = false
        firestore.collection(agency).document("community").
        collection(collectionName).document(documentName)
            .set(it)
            .addOnSuccessListener {success ->
                isSuccess = true
                postDataInsertSuccess.postValue(isSuccess)
                Log.e("checkingSuccess", "${isSuccess}, ${postDataInsertSuccess.value}")
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error", exception)
            }
        return postDataInsertSuccess
    }
    fun insertPostCommentData(agency: String, collection_name: String, document_name: String, postComment : PostCommentDataClass) : MutableLiveData<Boolean> {
        var postCommentUploadBool : Boolean = false
        firestore
            .collection(agency)
            .document("community")
            .collection(collection_name)
            .document(document_name)
            .collection("post_comment")
            .document(postComment.commentDate + postComment.commentTime + postComment.commentName)
            .set(postComment)
            .addOnSuccessListener {
                postCommentUploadBool = true
                postCommentUploadSuccess.postValue(postCommentUploadBool)
            }
        return postCommentUploadSuccess
    }
    fun deletePostCommentData(agency: String, collection_name: String, document_name: String, postComment : PostCommentDataClass) : MutableLiveData<Boolean> {
        var postCommentDeleteBool : Boolean = false
        firestore
            .collection(agency)
            .document("community")
            .collection(collection_name)
            .document(document_name)
            .collection("post_comment")
            .document(postComment.commentId)
            .delete()
            .addOnSuccessListener {
                postCommentDeleteBool = true
                postCommentDeleteSuccess.postValue(postCommentDeleteBool)
            }
        return postCommentDeleteSuccess
    }

    fun updateCollectionPostData(agency: String, collection_name: String) {
        var postDataList : ArrayList<PostDataInfo> = arrayListOf()
        firestore.collection(agency)
            .document("community")
            .collection(collection_name)
            .orderBy("post_id", Query.Direction.DESCENDING)
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
                        document["post_comments"] as ArrayList<String>,
                        document["post_id"] as String,
                        document["post_photo_uri"] as ArrayList<String>,
                        document["post_state"] as String,
                        document["post_anonymous"] as Boolean)
                    postDataList.add(item)
                }
                collectionPostDataInfoList.value= postDataList
            }
    }
    fun getCollectionPostData(agency: String, collection_name: String) : MutableLiveData<ArrayList<PostDataInfo>>{
        //collectionPostDataInfoList.postValue(arrayListOf())
        updateCollectionPostData(agency, collection_name)
        return collectionPostDataInfoList
    }
    fun updateNoticePostData(agency: String) {
        var postDataList : ArrayList<PostDataInfo> = arrayListOf()
        firestore.collection(agency)
            .document("community")
            .collection("notice")
            .orderBy("post_id", Query.Direction.DESCENDING)
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
                        document["post_comments"] as ArrayList<String>,
                        document["post_id"] as String,
                        document["post_photo_uri"] as ArrayList<String>,
                        document["post_state"] as String,
                        document["post_anonymous"] as Boolean)
                    postDataList.add(item)
                }
                noticePostDataInfoList.value= postDataList
            }
    }

    fun getNoticePostData(agency: String) : MutableLiveData<ArrayList<PostDataInfo>>{
        updateNoticePostData(agency)
        return noticePostDataInfoList
    }
    fun updateDocumentPostCommentData(agency: String, collection_name: String, document_name : String) {
        var postCommentDataList : ArrayList<PostCommentDataClass> = arrayListOf()
        firestore.collection(agency)
            .document("community")
            .collection(collection_name)
            .document(document_name)
            .collection("post_comment")
            .orderBy("commentId", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { result ->
                for (document in result){
                    val item = PostCommentDataClass(
                        document["commentName"] as String,
                        document["commentContents"] as String,
                        document["commentDate"] as String,
                        document["commentTime"] as String,
                        document["commentAnonymous"] as Boolean,
                        document["commentId"] as String,)
                    postCommentDataList.add(item)
                }
                postDataCommentList.value= postCommentDataList
            }

    }
    fun getDocumentPostCommentData(agency: String, collection_name: String, document_name : String) : MutableLiveData<ArrayList<PostCommentDataClass>>{
        updateDocumentPostCommentData(agency, collection_name, document_name)
        return postDataCommentList
    }

    fun updateDocumentPostData(agency: String, collection_name: String, document_name : String){
        firestore.collection(agency)
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
                    result["post_comments"] as ArrayList<String>,
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
    fun getDocumentPostData(agency: String, collection_name: String, document_name: String) : MutableLiveData<PostDataInfo>{
        updateDocumentPostData(agency, collection_name, document_name)
        return documentPostDataInfo
    }
    fun deleteDocumentPostData(agency: String, collection_name: String, document_name: String) {
        firestore.collection(agency)
            .document("community")
            .collection(collection_name)
            .document(document_name).delete()
    }
    fun modifyPostPartData(agency: String, collection_name: String, document_name: String, partKey: String, modifyContent: Any) : MutableLiveData<Boolean>{
        var modifyMap = mutableMapOf<String, Any>()
        modifyMap[partKey] = modifyContent
        firestore.collection(agency)
            .document("community")
            .collection(collection_name)
            .document(document_name)
            .update(modifyMap)
            .addOnSuccessListener { result ->
                postModifyPartDataSuccess.postValue(true)
            }
        return postModifyPartDataSuccess
    }
    fun modifyPostData(agency: String, collection_name: String, document_name: String, modifyTitle: String, modifyContent: String) {
        var titleMap = mutableMapOf<String, Any>()
        var contentMap = mutableMapOf<String, Any>()
        titleMap["post_title"] = modifyTitle
        contentMap["post_contents"] = modifyContent
        firestore.collection(agency)
            .document("community")
            .collection(collection_name)
            .document(document_name)
            .update(titleMap)
            .addOnSuccessListener { result ->

            }

        firestore.collection(agency)
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
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos)
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

    fun upDatePhotoData(uri_array: ArrayList<String>) {
        val postData: ArrayList<String> = arrayListOf()
        for (uri in uri_array) {
            val u = "file://$uri"
            val storageRefer: StorageReference = fireStorage.reference.child("images/").child(u)
            storageRefer.downloadUrl.addOnSuccessListener {
                System.out.println(it.toString())
                postData.add(it.toString())
                postDataPhotoUrl.value = postData
            }
        }
    }


    fun getDataPhoto(uri_array: ArrayList<String>) : MutableLiveData<ArrayList<String>>{
        deletePhotoData()
        upDatePhotoData(uri_array)
        return postDataPhotoUrl
    }
    fun deletePhotoData() {
        postDataPhotoUrl.postValue(arrayListOf())
    }
    fun updatePhotoThumbnailData(uri : String) {
        var postDataThumbnail : String = ""
        val u = "file://" + uri
        val storageRefer: StorageReference = fireStorage.reference.child("images/").child(u)
        storageRefer.downloadUrl.addOnSuccessListener {
            //System.out.println(it.toString())
            postDataThumbnail = it.toString()
            postDataPhotoThumbnailUrl.value = postDataThumbnail
            Log.e("check_preview", "${postDataThumbnail}")
        }
    }
    fun getPhotoThumbnailData(uri : String) : MutableLiveData<String> {
        updatePhotoThumbnailData(uri)
        return postDataPhotoThumbnailUrl
    }

    fun updateNoticeCategoryPostData(agency: String, noticeCategory : String) {
        var noticeDataList: ArrayList<PostDataInfo> = arrayListOf()
        firestore.collection(agency).document("community").collection("notice")
            .whereEqualTo("post_category", noticeCategory)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val item = PostDataInfo(
                        document["post_category"] as String,
                        document["post_name"] as String,
                        document["post_title"] as String,
                        document["post_contents"] as String,
                        document["post_date"] as String,
                        document["post_time"] as String,
                        document["post_comments"] as ArrayList<String>,
                        document["post_id"] as String,
                        document["post_photo_uri"] as ArrayList<String>,
                        document["post_state"] as String,
                        document["post_anonymous"] as Boolean
                    )
                    noticeDataList.add(item)
                }
                collectionPostDataInfoList.value = noticeDataList
            }
    }
    fun getNoticeCategoryPostData(agency: String, noticeCategory: String) : MutableLiveData<ArrayList<PostDataInfo>>{
        updateNoticeCategoryPostData(agency, noticeCategory)
        return collectionPostDataInfoList
    }

    fun updateSearchPostData(agency: String, collectionName : String, searchKeyword : String){
        var searchPostList : ArrayList<PostDataInfo> = arrayListOf()
        firestore.collection(agency)
            .document("community")
            .collection(collectionName)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val postTitle = document["post_title"] as String
                    val postContent = document["post_contents"] as String
                    if(postTitle.contains(searchKeyword) || postContent.contains(searchKeyword)){
                        val item = PostDataInfo(
                            document["post_category"] as String,
                            document["post_name"] as String,
                            document["post_title"] as String,
                            document["post_contents"] as String,
                            document["post_date"] as String,
                            document["post_time"] as String,
                            document["post_comments"] as ArrayList<String>,
                            document["post_id"] as String,
                            document["post_photo_uri"] as ArrayList<String>,
                            document["post_state"] as String,
                            document["post_anonymous"] as Boolean
                        )
                        searchPostList.add(item)
                    }
                }
                Log.v("s", "{$searchPostList}")
                postSearchPostDataList.value = searchPostList
            }
    }
    fun getSearchPostData(agency: String, collectionName: String, searchKeyword: String) : MutableLiveData<ArrayList<PostDataInfo>>{
        updateSearchPostData(agency, collectionName, searchKeyword)
        return postSearchPostDataList
    }


}