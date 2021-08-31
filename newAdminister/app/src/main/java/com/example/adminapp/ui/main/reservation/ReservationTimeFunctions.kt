package com.example.adminapp.ui.main.reservation

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun getWeekDayString(stringTime: String) : String {
    val localTime = changeStringToLocalDateTime(stringTime)
    return when (localTime.dayOfWeek){
        DayOfWeek.MONDAY -> "월"
        DayOfWeek.TUESDAY -> "화"
        DayOfWeek.WEDNESDAY -> "수"
        DayOfWeek.THURSDAY -> "목"
        DayOfWeek.FRIDAY -> "금"
        DayOfWeek.SATURDAY -> "토"
        DayOfWeek.SUNDAY -> "일"
        else -> "월"
    }
}
@RequiresApi(Build.VERSION_CODES.O)
fun getHourMinuteString(stringTime : String) : String {
    var timeResult = ""
    val localTime = changeStringToLocalDateTime(stringTime)
    timeResult += if (localTime.hour < 10) "0${localTime.hour}:" else "${localTime.hour}:"
    timeResult += if (localTime.minute < 10) "0${localTime.minute}" else "${localTime.minute}"
    return timeResult
}
@RequiresApi(Build.VERSION_CODES.O)
fun getIntervalMinuteString(startTime : String, endTime : String ) : String {
    return calculateDuration(startTime, endTime).abs().toMinutes().toString()
}

@RequiresApi(Build.VERSION_CODES.O)
fun getMonthString(stringTime : String) : String {
    val localTime = changeStringToLocalDateTime(stringTime)
    return if (localTime.monthValue < 10 ) "0${localTime.monthValue}"
    else "${localTime.monthValue}"
}
@RequiresApi(Build.VERSION_CODES.O)
fun getMonthDayString(stringTime : String) : String {
    val localTime = changeStringToLocalDateTime(stringTime)
    return if (localTime.dayOfMonth < 10 ) "0${localTime.dayOfMonth}"
    else "${localTime.dayOfMonth}"
}
 @RequiresApi(Build.VERSION_CODES.O)
 fun changeStringToLocalDateTime(str : String) : LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
    return LocalDateTime.parse(str, formatter)
}
@RequiresApi(Build.VERSION_CODES.O)
fun calculateDuration(startTime : String, endTime: String) : Duration {
    val startLocalTime = changeStringToLocalDateTime(startTime)
    val endLocalTime = changeStringToLocalDateTime(endTime)
    return Duration.between(startLocalTime, endLocalTime)
}
 @RequiresApi(Build.VERSION_CODES.O)
 fun calculateDurationWithCurrent(compareTime : String) : Duration {
    val currentLocalTime = LocalDateTime.now()
    val compareLocalTime = changeStringToLocalDateTime(compareTime)
    return Duration.between(currentLocalTime, compareLocalTime)
}