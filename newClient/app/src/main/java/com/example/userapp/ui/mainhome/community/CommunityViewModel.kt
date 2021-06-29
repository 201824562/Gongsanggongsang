package com.example.userapp.ui.mainhome.community

import android.util.Log
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
    fun updatePostData(collection_name: String, document_name: String, modifyTitle : String, modifyContent : String){
        postDataRepository.modifyPostData(collection_name, document_name, modifyTitle, modifyContent)
    }
    fun getDocumentCommentData(it: PostDataInfo) : ArrayList<PostCommentDataClass>{
        val post_comments_array : ArrayList<PostCommentDataClass> = arrayListOf()
        if(it.post_comments.toString() != "null") {
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
        return post_comments_array
    }

}