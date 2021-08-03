package com.example.adminapp.ui.main.reservation.edit

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.adminapp.base.BaseSessionViewModel
import com.example.adminapp.base.BaseViewModel
import com.example.adminapp.data.model.ReservationEquipmentSettingData
import com.example.adminapp.data.model.ReservationFacilitySettingData
import com.example.adminapp.data.repository.ReservationRepository

class ReservationEditViewModel(application: Application) : BaseSessionViewModel(application) {
    private val reservationRepository: ReservationRepository = ReservationRepository.getInstance()

    fun getReservationEquipmentSettingDataList(): LiveData<List<ReservationEquipmentSettingData>> {
        return reservationRepository.getReservationEquipmentSettingDataList(agencyInfo)
    }
    fun getReservationFacilitySettingDataList(): LiveData<List<ReservationFacilitySettingData>> {
        return reservationRepository.getReservationFacilitySettingDataList(agencyInfo)
    }
}