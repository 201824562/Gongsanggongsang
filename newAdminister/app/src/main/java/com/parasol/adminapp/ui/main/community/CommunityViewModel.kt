package com.parasol.adminapp.ui.main.community

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.parasol.adminapp.ui.base.BaseSessionViewModel
import com.parasol.adminapp.data.AppDatabase
import com.parasol.adminapp.data.entity.PostCommentDataClass
import com.parasol.adminapp.data.model.AlarmItem
import com.parasol.adminapp.data.model.PostDataInfo
import com.parasol.adminapp.data.model.RemoteUserInfo
import com.parasol.adminapp.data.repository.AlarmRepository
import com.parasol.adminapp.data.repository.CommunityDataRepository
import com.parasol.adminapp.utils.SingleLiveEvent
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CommunityViewModel(application: Application) : BaseSessionViewModel(application) {
    private val firestore = FirebaseFirestore.getInstance()
    var postCommentUploadSuccess : MutableLiveData<Boolean> = MutableLiveData()
    var getTokenArrayList : MutableLiveData<ArrayList<RemoteUserInfo>> = MutableLiveData()
    private val alarmRepository: AlarmRepository = AlarmRepository.getInstance(AppDatabase.getDatabase(application, viewModelScope))
    private val communityDataRepository : CommunityDataRepository = CommunityDataRepository.getInstance()
    fun getPostDataInCategory(collection_name: String) : LiveData<ArrayList<PostDataInfo>> {
        return communityDataRepository.getPostDataInCategory(agencyInfo, collection_name)
    }
    fun initCategoryPostData(){
        communityDataRepository.initCategoryPostData()
    }
    fun getPostData(collection_name: String, document_name : String) : MutableLiveData<PostDataInfo>{
        return communityDataRepository.getPostData(agencyInfo, collection_name, document_name)
    }
    fun initPostData(){
        communityDataRepository.initPostData()
    }
    fun insertPostData(it: PostDataInfo) : MutableLiveData<Boolean> {
        return communityDataRepository.insertPostData(agencyInfo, it)
    }
    fun insertPostCommentData(collection_name: String, document_name: String, postComment : PostCommentDataClass) : MutableLiveData<Boolean> {
        var postCommentUploadBool : Boolean = false
        firestore
            .collection(agencyInfo)
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
    fun getPostPhotoSuccess() : MutableLiveData<ArrayList<String>>{
        return communityDataRepository.getDataPhoto()
    }
    fun getPostPhotoData(photoUri : ArrayList<String>) = viewModelScope.launch(Dispatchers.IO) {
        communityDataRepository.updatePhotoData(photoUri)
    }

    fun getUploadPhoto() : MutableLiveData<Boolean>{
        return communityDataRepository.getUploadPhotoSuccess()
    }
    @RequiresApi(Build.VERSION_CODES.P)
    fun uploadPhoto(bitmapArrayList: ArrayList<Bitmap>, uriArrayList : ArrayList<Uri>)= viewModelScope.launch(Dispatchers.Main){
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
    fun getUserToken(userNickname : String) : MutableLiveData<ArrayList<RemoteUserInfo>> {
        var remoteUserInfo: ArrayList<RemoteUserInfo> = arrayListOf()
        firestore.collection("USER_INFO")
            .whereEqualTo("nickname", userNickname)
            .get()
            .addOnSuccessListener {
                for (result in it) {
                    if(result["fcmToken"] != null){
                        val remoteInfo = RemoteUserInfo (result["id"] as String, result["fcmToken"] as ArrayList<String>)
                        remoteUserInfo.add(remoteInfo)
                    }
                }
                getTokenArrayList.postValue(remoteUserInfo)
            }
        return getTokenArrayList
    }
    fun getAllUserIdToken() : MutableLiveData<ArrayList<RemoteUserInfo>> {
        var remoteUserInfo: ArrayList<RemoteUserInfo> = arrayListOf()
        firestore.collection("USER_INFO")
            .get()
            .addOnSuccessListener {
                for (result in it) {
                    var remoteInfo : RemoteUserInfo = RemoteUserInfo()
                    if(result["fcmToken"] != null ){
                        remoteInfo = RemoteUserInfo (result["id"] as String, result["fcmToken"] as ArrayList<String>)
                    }
                    remoteUserInfo.add(remoteInfo)
                }
                getTokenArrayList.postValue(remoteUserInfo)
            }
        return getTokenArrayList
    }
    fun getUserNameToken(userNickname : String) : MutableLiveData<ArrayList<RemoteUserInfo>> {
        var remoteUserInfo: ArrayList<RemoteUserInfo> = arrayListOf()
        firestore.collection("USER_INFO")
            .whereEqualTo("name", userNickname)
            .get()
            .addOnSuccessListener {
                for (result in it) {
                    if(result["fcmToken"] != null){
                        val remoteInfo = RemoteUserInfo (result["id"] as String, result["fcmToken"] as ArrayList<String>)
                        remoteUserInfo.add(remoteInfo)
                    }
                }
                getTokenArrayList.postValue(remoteUserInfo)
            }
        return getTokenArrayList
    }

    private val _onSuccessRegisterAlarmData = SingleLiveEvent<AlarmItem>()
    val onSuccessRegisterAlarmData : LiveData<AlarmItem> get() = _onSuccessRegisterAlarmData

    fun registerAlarmData(toOther : String, documentId : String, alarmData : AlarmItem)  {
        apiCall(alarmRepository.registerAlarmData(agencyInfo, toOther, documentId, alarmData), { _onSuccessRegisterAlarmData.call() }, {})
    }
}