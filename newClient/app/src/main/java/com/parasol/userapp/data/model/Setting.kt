package com.parasol.userapp.data.model

import android.os.Parcelable
import com.parasol.userapp.data.entity.DayTimeSlot
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

data class SettingWeekItem(
    val monday : List<SettingItem>,
    val tuesday : List<SettingItem>,
    val wednesday : List<SettingItem>,
    val thursday : List<SettingItem>,
    val friday : List<SettingItem>,
    val saturday : List<SettingItem>,
    val sunday : List<SettingItem>,
)

data class SettingItem(
    var buttonSelected : Boolean,
    val data: SettingData,
    val index: Int,
    var user: String
)

@Parcelize
data class SettingDeliveryOutReserveData(
    var modifiedSettingItemList: MutableList<DayTimeSlot>,
    var outStartTime : String,
    var weekDay: String
): Parcelable


data class SettingData(var hour: Int = 0, var min: Int = 0)
