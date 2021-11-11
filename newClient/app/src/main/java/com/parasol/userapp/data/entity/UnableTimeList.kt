package com.parasol.userapp.data.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UnableTimeList(
    val data:Map<String,Long>,
    val type: String,
    val unable: Boolean
) : Parcelable
