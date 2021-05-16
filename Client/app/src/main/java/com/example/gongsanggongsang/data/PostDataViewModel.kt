package com.example.gongsanggongsang.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope

class PostDataViewModel(application: Application) :  AndroidViewModel(application){
    private val postDataRepository : PostDataRepository = PostDataRepository(AppDatabase.getDatabase(application, viewModelScope))

    fun insertPostData(it: PostDataClass) {
        postDataRepository.insertPostData(it)
    }

}
