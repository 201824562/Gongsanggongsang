package com.parasol.adminapp.data.model


data class ReceiverAdmin(val boolean: Boolean, val adminModelData : AdminModel?)

data class ReceiverEquipment(val boolean: Boolean, val equipmentData: ReservationEquipmentData?)

data class ReceiverFacilityLog(val boolean: Boolean, val facilityLogData: ReservationFacilityLog?)

data class ReceiverFacilitySettingData(val boolean: Boolean, val facilitySettingData : ReservationFacilitySettingData?)