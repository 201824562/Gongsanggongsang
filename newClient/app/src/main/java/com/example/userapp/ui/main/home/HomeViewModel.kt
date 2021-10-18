package com.example.userapp.ui.main.home

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.userapp.data.AppDatabase
import com.example.userapp.data.model.PhotoCardInfo
import com.example.userapp.ui.base.BaseViewModel
import com.example.userapp.data.model.PostDataInfo
import com.example.userapp.data.repository.CommunityDataRepository
import com.example.userapp.data.repository.UserRepository
import com.example.userapp.ui.base.BaseSessionViewModel
import com.example.userapp.utils.SingleLiveEvent
import com.google.firebase.firestore.FirebaseFirestore

class HomeViewModel(application: Application) : BaseSessionViewModel(application) {

    private val fireStore = FirebaseFirestore.getInstance()
    private val FIRESTORE_PHOTO_CARD = "PHOTO_CARD"

    fun getNoticePostData() : LiveData<List<PhotoCardInfo>> {
        getUserPhotoCardFromFirebase(agencyInfo)
        return onSuccessGetUserPhotoCardDataList
    }
    private val _onSuccessGetUserPhotoCardDataList = SingleLiveEvent<List<PhotoCardInfo>>()
    private val onSuccessGetUserPhotoCardDataList: LiveData<List<PhotoCardInfo>> get() = _onSuccessGetUserPhotoCardDataList

    private fun getUserPhotoCardFromFirebase(agency: String) {
        fireStore.collection(FIRESTORE_PHOTO_CARD)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(ContentValues.TAG, "Listen failed.", e)
                    _onSuccessGetUserPhotoCardDataList.postValue(emptyList())
                    return@addSnapshotListener }
                if (snapshot == null) _onSuccessGetUserPhotoCardDataList.postValue(emptyList())
                else {
                    val photoCardDataList: MutableList<PhotoCardInfo> = mutableListOf()
                    for (document in snapshot) {
                        photoCardDataList.add(
                            PhotoCardInfo(
                                document.get("icon") as String,
                                document.get("id") as String,
                                document.get("name") as String,
                                document.get("nickName") as String,
                                document.get("job") as String,
                                document.get("email") as String,
                                document.get("introduce") as String) )
                    }
                    _onSuccessGetUserPhotoCardDataList.postValue(photoCardDataList)
                }
            }
    }
}