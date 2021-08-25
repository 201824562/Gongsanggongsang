package com.example.adminapp.ui.main.community

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.adminapp.base.BaseSessionViewModel
import com.example.adminapp.base.BaseViewModel
import com.example.adminapp.data.model.PostCommentDataClass
import com.example.adminapp.data.model.PostDataInfo
import com.example.adminapp.data.repository.CommunityDataRepository

class CommunityViewModel(application: Application) : BaseSessionViewModel(application) {
    var selectedItems : ArrayList<String> = arrayListOf()
    private val communityDataRepository : CommunityDataRepository = CommunityDataRepository.getInstance()

    fun getCategoryAllPostData(agency: String, collection_name: String) : LiveData<ArrayList<PostDataInfo>> {
        return communityDataRepository.getCategoryAllPostData(agency, collection_name)
    }
    fun getPostData(agency: String, collection_name: String, document_name : String) : MutableLiveData<PostDataInfo>{
        return communityDataRepository.getPostData(agency, collection_name, document_name)
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
    fun deletePostData(agency: String, collection_name: String, document_name: String) : MutableLiveData<Boolean> {
        return communityDataRepository.deletePostDataSuccess(agency, collection_name, document_name)
    }
    fun modifyPostPartData(agency: String, collection_name: String, document_name: String, partKey: String, modifyContent: Any) : MutableLiveData<Boolean>{
        return communityDataRepository.modifyPostPartData(agency, collection_name, document_name, partKey, modifyContent)
    }
    fun getPostCommentData(agency: String, collection_name: String, document_name: String) : MutableLiveData<ArrayList<PostCommentDataClass>> {
        return communityDataRepository.getPostCommentData(agency, collection_name, document_name)
    }

    fun getPostPhotoData(photoUri : ArrayList<String>) : MutableLiveData<ArrayList<String>> {
        return communityDataRepository.getDataPhoto(photoUri)
    }
    fun deletePostPhoto(){
        communityDataRepository.deletePhotoData()
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
    fun selectPhoto(selectPhotoUri : String) {
        if(selectPhotoUri in selectedItems){
            selectedItems.remove(selectPhotoUri)
        }
        else{
            selectedItems.add(selectPhotoUri)
        }
    }

}