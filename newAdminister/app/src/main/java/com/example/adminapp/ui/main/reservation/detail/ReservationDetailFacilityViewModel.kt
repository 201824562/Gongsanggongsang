package com.example.adminapp.ui.main.reservation.detail

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.adminapp.base.BaseSessionViewModel
import com.example.adminapp.data.model.ReceiverEquipment
import com.example.adminapp.data.model.ReservationEquipmentData
import com.example.adminapp.data.model.ReservationEquipmentLog
import com.example.adminapp.data.model.ReservationEquipmentSettingData
import com.example.adminapp.data.repository.ReservationRepository
import com.example.adminapp.utils.SingleLiveEvent

class ReservationDetailFacilityViewModel (application: Application) : BaseSessionViewModel(application) {

    private val reservationRepository: ReservationRepository = ReservationRepository.getInstance()

    private val _onSuccessGettingReserveEquipmentSettingData = SingleLiveEvent<ReservationEquipmentSettingData>()
    val onSuccessGettingReserveEquipmentSettingData : LiveData<ReservationEquipmentSettingData> get() = _onSuccessGettingReserveEquipmentSettingData

    fun getReservationEquipmentSettingData(itemName : String)  {
        apiCall(reservationRepository.getReservationEquipmentSettingData(agencyInfo, itemName), {
            _onSuccessGettingReserveEquipmentSettingData.postValue(it)
        })
    }

    fun getReservationEquipmentData(itemName : String): LiveData<ReceiverEquipment> {
        return reservationRepository.getReservationEquipmentData(agencyInfo, itemName)
    }

    fun getReservationEquipmentLogData(itemType : String, itemName : String): LiveData<List<ReservationEquipmentLog>> {
        return reservationRepository.getReservationEquipmentLogData(agencyInfo, itemType, itemName)
    }

    fun startReservationEquipment(itemName : String){
        apiCall(reservationRepository.startReservationEquipment(agencyInfo, itemName))
    }

    fun stopReservationEquipment(item : ReservationEquipmentData){
        apiCall(reservationRepository.stopReservationEquipment(agencyInfo, ReservationEquipmentData(item.icon, item.name, usable = false, maxTime = item.maxTime) ))
    }


}