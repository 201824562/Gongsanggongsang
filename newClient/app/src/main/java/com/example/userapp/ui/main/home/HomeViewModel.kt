package com.example.userapp.ui.main.home

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.userapp.base.BaseSessionViewModel
import com.example.userapp.base.BaseViewModel
import com.example.userapp.data.model.PostDataInfo
import com.example.userapp.data.repository.CommunityDataRepository

class HomeViewModel(application: Application) : BaseSessionViewModel(application) {

    private val communityDataRepository : CommunityDataRepository = CommunityDataRepository.getInstance()

    fun getCollectionPostData(collection_name: String) : MutableLiveData<ArrayList<PostDataInfo>>{
        return communityDataRepository.getCollectionPostData(agencyInfo, collection_name)
    }

    fun getNoticeCategoryPostData(collectionName : String) : MutableLiveData<ArrayList<PostDataInfo>> {
        return communityDataRepository.getNoticeCategoryPostData(agencyInfo, collectionName)
    }
}