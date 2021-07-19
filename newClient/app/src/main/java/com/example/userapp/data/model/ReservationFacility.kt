package com.example.userapp.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

// navigation safe args로 데이터를 전달하려면 Parcelable데이터 형식으로 바꿔줘야함
@Parcelize
data class ReservationFacility(
    val document_name: String,
    val category: String,
    var max_time: Long,
    var time_interval: Long
) : Parcelable
