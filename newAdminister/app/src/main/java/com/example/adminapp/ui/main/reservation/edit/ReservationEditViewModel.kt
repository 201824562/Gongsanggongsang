package com.example.adminapp.ui.main.reservation.edit

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.adminapp.base.BaseSessionViewModel
import com.example.adminapp.base.BaseViewModel
import com.example.adminapp.data.model.CategoryItem
import com.example.adminapp.data.model.ReservationEquipmentSettingData
import com.example.adminapp.data.model.ReservationFacilitySettingData
import com.example.adminapp.data.model.ReservationType
import com.example.adminapp.data.repository.ReservationRepository
import com.example.adminapp.utils.SingleLiveEvent

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