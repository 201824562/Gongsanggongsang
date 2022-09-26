package com.parasol.userapp.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DayTimeSlot(
    var buttonSelected: Boolean,
    var data: ReservationTimeData?,
    var index: Long,
    var user: String
//    var weekday: Int
) : Parcelable {
    fun modifyBtnSlctFalse(dayTimeSlot: DayTimeSlot){
        dayTimeSlot.buttonSelected = false
    }
}
@Parcelize
data class ReservationTimeData (val hour : Long, val min: Long) : Parcelable