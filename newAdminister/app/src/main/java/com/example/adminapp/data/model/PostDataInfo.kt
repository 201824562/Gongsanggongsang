package com.example.adminapp.data.model

import com.example.adminapp.data.repository.PostCommentDataClass


data class PostDataInfo(
    val post_category: String,

    val post_name: String,

    val post_title: String,

    val post_contents: String,

    val post_date: String,

    val post_time: String,

    val post_comments: ArrayList<PostCommentDataClass> = arrayListOf(),

    val post_id: String,

    val post_photo_uri: ArrayList<String>,

    val post_state: String,

    val post_anonymous: Boolean
)