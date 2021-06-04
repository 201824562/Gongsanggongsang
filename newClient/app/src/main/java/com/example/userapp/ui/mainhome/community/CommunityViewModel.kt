package com.example.userapp.ui.mainhome.community

import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import com.example.userapp.R
import com.example.userapp.base.BaseViewModel
import com.example.userapp.data.entity.PostCommentDataClass
import com.example.userapp.data.model.PostDataInfo
import com.example.userapp.data.repository.PostDataRepository

class CommunityViewModel : BaseViewModel() {

    private val postDataRepository : PostDataRepository = PostDataRepository.getInstance()
    private var postItem : ArrayList<PostDataInfo> = arrayListOf()




    fun getCollectionPostData(collection_name: String) : MutableLiveData<ArrayList<PostDataInfo>>{
        return postDataRepository.getCollectionPostData(collection_name)
    }

    fun getDocumentPostData(collection_name: String, document_name : String) : MutableLiveData<PostDataInfo>{
        return postDataRepository.getDocumentPostData(collection_name, document_name)
    }
    fun insertPostData(it: PostDataInfo) {
        postDataRepository.insertPostData(it)
    }
    fun insertPostCommentData(collection_name: String, document_name: String, post_comments_array : ArrayList<PostCommentDataClass>){
        postDataRepository.insertPostCommentData(collection_name, document_name, post_comments_array)
    }
    fun deletePostData(collection_name: String, document_name: String) {
        postDataRepository.deleteDocumentPostData(collection_name, document_name)
    }

}