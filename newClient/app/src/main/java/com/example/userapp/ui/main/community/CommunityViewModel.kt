package com.example.userapp.ui.main.community

import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.userapp.base.BaseViewModel
import com.example.userapp.data.entity.PostCommentDataClass
import com.example.userapp.data.model.PostDataInfo
import com.example.userapp.data.repository.CommunityDataRepository

class CommunityViewModel() : BaseViewModel() {
    var selected_items : ArrayList<String> = arrayListOf()
    private val communityDataRepository : CommunityDataRepository = CommunityDataRepository.getInstance()

    fun getCollectionPostData(agency: String, collection_name: String) : LiveData<ArrayList<PostDataInfo>> {
        return communityDataRepository.getCollectionPostData(agency, collection_name)
    }
    fun getDocumentPostData(agency: String, collection_name: String, document_name : String) : MutableLiveData<PostDataInfo>{
        return communityDataRepository.getDocumentPostData(agency, collection_name, document_name)
    }
    fun insertPostData(agency : String, it: PostDataInfo) : MutableLiveData<Boolean> {
        return communityDataRepository.insertPostData(agency, it)
    }
    fun insertPostCommentData(agency: String, collection_name: String, document_name: String, postComment : PostCommentDataClass) : MutableLiveData<Boolean> {
        return communityDataRepository.insertPostCommentData(agency, collection_name, document_name, postComment)
    }
    fun deletePostCommentData(agency: String, collection_name: String, document_name: String, postComment : PostCommentDataClass) : MutableLiveData<Boolean> {
        return communityDataRepository.deletePostCommentData(agency, collection_name, document_name, postComment)
    }
    fun deletePostData(agency: String, collection_name: String, document_name: String) {
        communityDataRepository.deleteDocumentPostData(agency, collection_name, document_name)
    }
    fun updatePostData(agency: String, collection_name: String, document_name: String, modifyTitle : String, modifyContent : String){
        communityDataRepository.modifyPostData(agency, collection_name, document_name, modifyTitle, modifyContent)
    }
    fun modifyPostPartData(agency: String, collection_name: String, document_name: String, partKey: String, modifyContent: Any) : MutableLiveData<Boolean>{
        return communityDataRepository.modifyPostPartData(agency, collection_name, document_name, partKey, modifyContent)
    }
    fun getDocumentCommentData(agency: String, collection_name: String, document_name: String) : MutableLiveData<ArrayList<PostCommentDataClass>> {
        return communityDataRepository.getDocumentPostCommentData(agency, collection_name, document_name)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun uploadPhoto(bitmap_array : ArrayList<Bitmap>, uri_array : ArrayList<Uri>){
        communityDataRepository.uploadPhoto(bitmap_array, uri_array)
    }

    fun getPostPhotoData(photoUri : ArrayList<String>) : MutableLiveData<ArrayList<String>> {
        return communityDataRepository.getDataPhoto(photoUri)
    }
    fun deletePostPhoto(){
        communityDataRepository.deletePhotoData()
    }
    fun getPostPhotoThumbnailData(uri : String) : MutableLiveData<String> {
        return communityDataRepository.getPhotoThumbnailData(uri)
    }
    fun getNoticeCategoryPostData(agency: String, collectionName : String) : MutableLiveData<ArrayList<PostDataInfo>> {
        return communityDataRepository.getNoticeCategoryPostData(agency, collectionName)
    }
    fun getNoticePostData(agency: String) : MutableLiveData<ArrayList<PostDataInfo>>{
        return communityDataRepository.getNoticePostData(agency)
    }
    fun getSearchPostData(agency: String, collectionName: String, searchKeyword : String) : MutableLiveData<ArrayList<PostDataInfo>>{
        return communityDataRepository.getSearchPostData(agency, collectionName, searchKeyword)
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