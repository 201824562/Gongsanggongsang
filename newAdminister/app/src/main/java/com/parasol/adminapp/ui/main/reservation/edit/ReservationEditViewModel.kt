package com.parasol.adminapp.ui.main.reservation.edit

import android.app.Application
import androidx.lifecycle.LiveData
import com.parasol.adminapp.ui.base.BaseSessionViewModel
import com.parasol.adminapp.data.model.ReservationEquipmentSettingData
import com.parasol.adminapp.data.model.ReservationFacilitySettingData
import com.parasol.adminapp.data.repository.ReservationRepository
import com.parasol.adminapp.utils.SingleLiveEvent

class ReservationEditViewModel(application: Application) : BaseSessionViewModel(application) {

    private val reservationRepository: ReservationRepository = ReservationRepository.getInstance()

    private val _selectedTabPositionData = SingleLiveEvent<Int>()
    val selectedTabPositionData : LiveData<Int> get() = _selectedTabPositionData
    var selectedTabPosition : Int? = null

    fun saveTabPosition(position : Int){
        selectedTabPosition = position
        _selectedTabPositionData.postValue(position)
    }
    fun getReservationEquipmentSettingDataList(): LiveData<List<ReservationEquipmentSettingData>> {
        return reservationRepository.getReservationEquipmentSettingDataList(agencyInfo)
    }
    fun getReservationFacilitySettingDataList(): LiveData<List<ReservationFacilitySettingData>> {
        return reservationRepository.getReservationFacilitySettingDataList(agencyInfo)
    }
}