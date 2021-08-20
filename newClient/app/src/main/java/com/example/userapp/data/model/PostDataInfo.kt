package com.example.userapp.data.model


data class PostDataInfo(
    val post_category: String,

    val post_name: String,

    val post_title: String,

    val post_contents: String,

    val post_date: String,

    val post_time: String,

    val post_comments: Long,

    val post_id: String,

    val post_photo_uri: ArrayList<String>,

    val post_state: String,

    val post_anonymous: Boolean
)