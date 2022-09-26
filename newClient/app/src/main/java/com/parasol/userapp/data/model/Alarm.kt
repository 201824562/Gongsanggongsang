package com.parasol.userapp.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class AlarmItem(val documentId : String, val time : String, val otherUser : String,
                     val message : String, val type : String,
                     val reservationData : ReservationAlarmData?, val postData : PostAlarmData?, val signData : SignUpAlarmData?,
                     val clicked : Boolean = false)

data class ReservationAlarmData(val documentId: String, val name : String,
                                val startTime : String, val endTime : String)

@Parcelize
data class PostAlarmData(
    val post_category: String = "", val post_name: String = "", val post_title: String = "",
    val post_contents: String = "", val post_date: String = "", val post_time: String = "",
    val post_comments: Long = 0, val post_id: String = "", val post_photo_uri: ArrayList<String> = arrayListOf(),
    val post_state: String = "", val post_anonymous: Boolean = false ) : Parcelable {
    fun makeToPostDataInfo() : PostDataInfo {
        return PostDataInfo(post_category, post_name, post_title, post_contents, post_date, post_time,
            post_comments, post_id, post_photo_uri, post_state, post_anonymous)
    }
}

data class SignUpAlarmData(
    val agency : String = "", val id: String = "", val pwd: String, val name: String = "",
    val nickname: String = "", val birthday: String, val smsInfo : String, var allowed: Boolean)

enum class AlarmType(val type : String) {
    NOTICE("공지"), RESERVATION("공용"), EMERGENCY("긴급"), FREE("자유"),
    TOGETHER("함께"), SUGGEST("건의"), MARKET("장터"), OUT("퇴실") , SIGNUP("가입승인") ;
    companion object{
        fun makeStringToEnumData(type : String) : AlarmType{
            return values().first { it.type == type} }
        fun makeEnumDataToString(enumData : AlarmType) : String{
            return enumData.type
        }
    }
}

