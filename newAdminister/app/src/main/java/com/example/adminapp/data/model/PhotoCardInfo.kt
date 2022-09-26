package com.example.adminapp.data.model

data class PhotoCardInfo(val icon : String, val id : String, val name : String, val nickName : String,
                         val job : String, val email : String, val introduce : String)

data class ReceiverPhotoCardInfo(val exists : Boolean, val data : PhotoCardInfo?)