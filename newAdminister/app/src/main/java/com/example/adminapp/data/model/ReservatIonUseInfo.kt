package com.example.adminapp.data.model

//TODO : 기중이랑 상의하기 -> 사용자 예약 데이터

data class ReservationEquipmentUseInfo (var userName : String = "", val usingTime: Long=0L, val startTime : String, val usable : Boolean = true )

data class ReservationFacilityUseInfo (var userName : String = "", val data : List<ReservationTimeData>)
