package com.example.userapp.data.model

import com.example.userapp.data.entity.DayTimeSlot

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

data class SettingDeliveryOutReserveData(
    var modifiedSettingItemList: MutableList<DayTimeSlot>,
    var outStartTime : String,
    var weekDay: String
)

data class SettingData(var hour: Int = 0, var min: Int = 0)
