package com.parasol.adminapp.ui.main.home

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import com.parasol.adminapp.data.model.PhotoCardInfo
import com.parasol.adminapp.data.model.ReceiverPhotoCardInfo
import com.parasol.adminapp.ui.base.BaseSessionViewModel
import com.parasol.adminapp.ui.base.BaseViewModel
import com.parasol.adminapp.utils.SingleLiveEvent
import com.google.firebase.firestore.FirebaseFirestore


class HomeViewModel(application: Application) : BaseSessionViewModel(application) {

    private val fireStore = FirebaseFirestore.getInstance()
    private val FIRESTORE_PHOTO_CARD = "PHOTO_CARD"

    private val _onSuccessGetUserPhotoCardDataList = SingleLiveEvent<List<PhotoCardInfo>>()
    private val onSuccessGetUserPhotoCardDataList: LiveData<List<PhotoCardInfo>> get() = _onSuccessGetUserPhotoCardDataList

    fun getUserPhotoCardDataList() : LiveData<List<PhotoCardInfo>> {
        getUserPhotoCardListFromFirebase()
        return onSuccessGetUserPhotoCardDataList
    }

    private fun getUserPhotoCardListFromFirebase() {
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

    private val _onSuccessGetUserPhotoCardData = SingleLiveEvent<ReceiverPhotoCardInfo>()
    private val onSuccessGetUserPhotoCardData: LiveData<ReceiverPhotoCardInfo> get() = _onSuccessGetUserPhotoCardData

    fun getUserPhotoCardData() : LiveData<ReceiverPhotoCardInfo> {
        getUserPhotoCardFromFirebase()
        return onSuccessGetUserPhotoCardData
    }

    private fun getUserPhotoCardFromFirebase() {
        fireStore.collection(FIRESTORE_PHOTO_CARD).whereEqualTo("id", authToken).limit(1)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(ContentValues.TAG, "Listen failed.", e)
                    _onSuccessGetUserPhotoCardData.postValue(ReceiverPhotoCardInfo(false, null))
                    return@addSnapshotListener }
                if (snapshot == null) _onSuccessGetUserPhotoCardData.postValue(ReceiverPhotoCardInfo(false, null))
                else {
                    for (document in snapshot) {
                        val photoCardData = ReceiverPhotoCardInfo(true,
                            PhotoCardInfo(
                                document.get("icon") as String,
                                document.get("id") as String,
                                document.get("name") as String,
                                document.get("nickName") as String,
                                document.get("job") as String,
                                document.get("email") as String,
                                document.get("introduce") as String))
                        _onSuccessGetUserPhotoCardData.postValue(photoCardData)
                    }
                }
            }
    }

/*    private val _onSuccessDeletePhotoCardData = SingleLiveEvent<Any>()
    val onSuccessDeletePhotoCardData: LiveData<Any> get() = _onSuccessDeletePhotoCardData

    fun deleteUserPhotoCard() {
        apiCall(userRepository.deleteUserPhotoCard(authToken), {
            _onSuccessDeletePhotoCardData.call()
        })
    }*/
}