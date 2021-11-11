package com.parasol.adminapp.data.model


data class ReservationTimeData (val hour : Long, val min: Long){
    fun makeTimeDataToString() : String {
        var name = ""
        name += if (hour < 10) "0$hour : " else "$hour : "
        name += if (min == 0L) "0$min" else min.toString()
        return name
    }
}

data class ReservationUnableTimeItem (val type: ReservationUnableTimeType = ReservationUnableTimeType.HALF_HOUR,
                                      val data : ReservationTimeData = ReservationTimeData(0L, 0L), var unable : Boolean = false)

enum class ReservationUnableTimeType() {
    HALF_HOUR, HOUR;
    companion object{
        fun getReservationUnableTimeList(type : ReservationUnableTimeType) : List<ReservationUnableTimeItem>{
            val list = mutableListOf<ReservationUnableTimeItem>()
            when (type){
                HALF_HOUR -> {
                    for (hour in 0..23){
                        list.add(ReservationUnableTimeItem(HALF_HOUR, ReservationTimeData(hour.toLong(),0), false))
                        list.add(ReservationUnableTimeItem(HALF_HOUR, ReservationTimeData(hour.toLong(),30), false)) }
                }
                HOUR ->{
                    for (hour in 0..23){ list.add(ReservationUnableTimeItem(HOUR, ReservationTimeData(hour.toLong(),0), false)) }
                }
            }
            return list
        }
    }
}
