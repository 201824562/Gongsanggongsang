package com.example.userapp.data.model

import android.os.Parcelable
import com.example.userapp.data.entity.UnableTimeList
import kotlinx.android.parcel.Parcelize

// navigation safe args로 데이터를 전달하려면 Parcelable데이터 형식으로 바꿔줘야함
@Parcelize
data class ReservationFacility(
    val document_name: String,
    val category_icon: Int,
    var max_time: Long,
    var interval_time: Long,
    var usable: Boolean,
    val unableTimeList: List<UnableTimeList>
) : Parcelable
