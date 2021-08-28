package com.example.adminapp.ui.main.community

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.adminapp.base.BaseSessionViewModel
import com.example.adminapp.base.BaseViewModel
import com.example.adminapp.data.model.PostCommentDataClass
import com.example.adminapp.data.model.PostDataInfo
import com.example.adminapp.data.repository.CommunityDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CommunityViewModel(application: Application) : BaseSessionViewModel(application) {
    var selectedItems : ArrayList<String> = arrayListOf()
    private val communityDataRepository : CommunityDataRepository = CommunityDataRepository.getInstance()
    fun getCategoryAllPostData(collection_name: String) : LiveData<ArrayList<PostDataInfo>> {
        return communityDataRepository.getCategoryAllPostData(agencyInfo, collection_name)
    }
    fun getPostData(collection_name: String, document_name : String) : MutableLiveData<PostDataInfo>{
        return communityDataRepository.getPostData(agencyInfo, collection_name, document_name)
    }
    fun insertPostData(it: PostDataInfo) : MutableLiveData<Boolean> {
        return communityDataRepository.insertPostData(agencyInfo, it)
    }
    fun insertPostCommentData(collection_name: String, document_name: String, postComment : PostCommentDataClass) : MutableLiveData<Boolean> {
        return communityDataRepository.insertPostCommentData(agencyInfo, collection_name, document_name, postComment)
    }
    fun deletePostCommentData(collection_name: String, document_name: String, postComment : PostCommentDataClass) : MutableLiveData<Boolean> {
        return communityDataRepository.deletePostCommentData(agencyInfo, collection_name, document_name, postComment)
    }
    fun deletePostData(collection_name: String, document_name: String) : MutableLiveData<Boolean> {
        return communityDataRepository.deletePostDataSuccess(agencyInfo, collection_name, document_name)
    }
    fun modifyPostPartData(collection_name: String, document_name: String, partKey: String, modifyContent: Any) : MutableLiveData<Boolean>{
        return communityDataRepository.modifyPostPartData(agencyInfo, collection_name, document_name, partKey, modifyContent)
    }
    fun getPostCommentData(collection_name: String, document_name: String) : MutableLiveData<ArrayList<PostCommentDataClass>> {
        return communityDataRepository.getPostCommentData(agencyInfo, collection_name, document_name)
    }

    fun deletePostPhoto(){
        communityDataRepository.deletePhotoData()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun uploadPhoto(bitmapArrayList: ArrayList<Bitmap>, uriArrayList : ArrayList<Uri>)= viewModelScope.launch(
        Dispatchers.Main){
        communityDataRepository.uploadPhoto(bitmapArrayList, uriArrayList)
    }


    fun getNoticeCategoryPostData(collectionName : String) : MutableLiveData<ArrayList<PostDataInfo>> {
        return communityDataRepository.getNoticeCategoryPostData(agencyInfo, collectionName)
    }
    fun getNoticePostData() : MutableLiveData<ArrayList<PostDataInfo>>{
        return communityDataRepository.getNoticePostData(agencyInfo)
    }
    fun getSearchPostData(collectionName: String, searchKeyword : String) : MutableLiveData<ArrayList<PostDataInfo>>{
        return communityDataRepository.getSearchPostData(agencyInfo, collectionName, searchKeyword)
    }

}