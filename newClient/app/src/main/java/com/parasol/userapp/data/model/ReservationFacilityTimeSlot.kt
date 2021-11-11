package com.parasol.userapp.data.model

data class ReservationFacilityTimeSlot(
    val timeSlot: String,
    var name: String,
    var buttonSelect: Boolean,
    var index: Int,
    val document_name:String,
    val collection_name:String
  /*  val year: Int,
    val month: Int,
    val day: Int*/
)
