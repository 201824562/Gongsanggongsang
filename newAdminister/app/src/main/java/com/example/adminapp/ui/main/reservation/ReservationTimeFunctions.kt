package com.example.adminapp.ui.main.reservation

import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

 fun getHourMinuteString(stringTime : String) : String {
    var timeResult = ""
    val localTime = changeStringToLocalDateTime(stringTime)
    timeResult += if (localTime.hour < 10) "0${localTime.hour}:" else "${localTime.hour}:"
    timeResult += if (localTime.minute < 10) "0${localTime.minute}" else "${localTime.minute}"
    return timeResult
}
fun getMonthString(stringTime : String) : String {
    val localTime = changeStringToLocalDateTime(stringTime)
    return if (localTime.monthValue < 10 ) "0${localTime.monthValue}"
    else "${localTime.monthValue}"
}
fun getMonthDayString(stringTime : String) : String {
    val localTime = changeStringToLocalDateTime(stringTime)
    return if (localTime.dayOfMonth < 10 ) "0${localTime.dayOfMonth}"
    else "${localTime.dayOfMonth}"
}
 fun changeStringToLocalDateTime(str : String) : LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
    return LocalDateTime.parse(str, formatter)
}

 fun calculateDuration(compareTime : String) : Duration {
    val currentLocalTime = LocalDateTime.now()
    val compareLocalTime = changeStringToLocalDateTime(compareTime)
    return Duration.between(currentLocalTime, compareLocalTime)
}