package com.example.userapp.data.model

import android.net.Uri
import com.example.userapp.data.entity.PostCommentDataClass


data class PostDataInfo (
    val post_category : String,

    val post_name : String,

    val post_title : String,

    val post_contents : String,

    val post_date : String,

    val post_comments : ArrayList<PostCommentDataClass> = arrayListOf(),

    val post_id : String,

    val post_photo_uri : ArrayList<String>
)