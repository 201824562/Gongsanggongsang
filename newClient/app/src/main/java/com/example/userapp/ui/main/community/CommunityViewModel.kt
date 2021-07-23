package com.example.userapp.ui.main.community

import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.userapp.base.BaseViewModel
import com.example.userapp.data.entity.PostCommentDataClass
import com.example.userapp.data.model.PostDataInfo
import com.example.userapp.data.repository.CommunityDataRepository

class CommunityViewModel : BaseViewModel() {
    var selected_items : ArrayList<String> = arrayListOf()
    private val communityDataRepository : CommunityDataRepository = CommunityDataRepository.getInstance()

    fun getCommunityMainItem() {
        return communityDataRepository.getCommunityMainItem()
    }

    fun getCollectionPostData(collection_name: String) : MutableLiveData<ArrayList<PostDataInfo>>{
        return communityDataRepository.getCollectionPostData(collection_name)
    }
    fun getDocumentPostData(collection_name: String, document_name : String) : MutableLiveData<PostDataInfo>{
        return communityDataRepository.getDocumentPostData(collection_name, document_name)
    }
    fun insertPostData(it: PostDataInfo) {
        communityDataRepository.insertPostData(it)
    }
    fun insertPostCommentData(collection_name: String, document_name: String, post_comments_array : ArrayList<PostCommentDataClass>){
        communityDataRepository.insertPostCommentData(collection_name, document_name, post_comments_array)
    }
    fun deletePostData(collection_name: String, document_name: String) {
        communityDataRepository.deleteDocumentPostData(collection_name, document_name)
    }
    fun updatePostData(collection_name: String, document_name: String, modifyTitle : String, modifyContent : String){
        communityDataRepository.modifyPostData(collection_name, document_name, modifyTitle, modifyContent)
    }
    fun getDocumentCommentData(it: PostDataInfo) : ArrayList<PostCommentDataClass>{
        val post_comments_array : ArrayList<PostCommentDataClass> = arrayListOf()
        if(it.post_comments.toString() != "[]") {
            var server_comments = it.post_comments.toString().replace("[", "").replace("]", "")
                .replace("{", "").replace("}", "").split(",")
            Log.v("i", server_comments.toString())
            for (i in 0 until server_comments.size step 3) {
                var comments_element = PostCommentDataClass(
                    server_comments[i + 2].split("=")[1],
                    server_comments[i + 1].split("=")[1],
                    server_comments[i].split("=")[1]
                )
                post_comments_array.add(comments_element)
            }
        }
        else{
            return arrayListOf()
        }
        return post_comments_array
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun uploadPhoto(bitmap_array : ArrayList<Bitmap>, uri_array : ArrayList<Uri>){
        communityDataRepository.uploadPhoto(bitmap_array, uri_array)
    }

    fun getPostPhotoData(photoUri : ArrayList<String>) : MutableLiveData<ArrayList<String>> {
        return communityDataRepository.getPhoto(photoUri)
    }



    fun selectPhoto(select_photo_uri : String) {
        if(select_photo_uri in selected_items){
            selected_items.remove(select_photo_uri)
        }
        else{
            selected_items.add(select_photo_uri)
        }
        System.out.println(select_photo_uri)
    }
    fun getPhoto() : ArrayList<String>{
        return selected_items
    }

}