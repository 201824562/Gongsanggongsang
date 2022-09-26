package com.example.adminapp.data.model

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


data class SettingData(var hour: Int = 0, var min: Int = 0)
