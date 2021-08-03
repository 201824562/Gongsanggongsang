package com.example.adminapp.ui.main.reservation

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.adminapp.base.BaseSessionViewModel
import com.example.adminapp.data.model.ReservationEquipmentData
import com.example.adminapp.data.model.ReservationEquipmentSettingData
import com.example.adminapp.data.repository.ReservationRepository
import com.example.adminapp.utils.SingleLiveEvent

class ReservationDetailViewModel(application: Application) : BaseSessionViewModel(application) {

    private val reservationRepository: ReservationRepository = ReservationRepository.getInstance()

    private val _onSuccessGettingReserveEquipmentSettingData = SingleLiveEvent<ReservationEquipmentSettingData>()
    val onSuccessGettingReserveEquipmentSettingData : LiveData<ReservationEquipmentSettingData> get() = _onSuccessGettingReserveEquipmentSettingData
    private val _onSuccessGettingReserveEquipmentData = SingleLiveEvent<ReservationEquipmentData>()
    val onSuccessGettingReserveEquipmentData : LiveData<ReservationEquipmentData> get() = _onSuccessGettingReserveEquipmentData
    private val _onSuccessStartStopReserveEquipment = SingleLiveEvent<Any>()
    val onSuccessStartStopReserveEquipment : LiveData<Any> get() = _onSuccessStartStopReserveEquipment

    fun getReservationEquipmentSettingData(itemName : String)  {
        apiCall(reservationRepository.getReservationEquipmentSettingData(agencyInfo, itemName), {
            _onSuccessGettingReserveEquipmentSettingData.postValue(it)
        })
    }

    fun getReservationEquipmentData(itemName : String)  {
        apiCall(reservationRepository.getReservationEquipmentData(agencyInfo, itemName), {
            _onSuccessGettingReserveEquipmentData.postValue(it)
        })
    }

    fun startReservationEquipment(itemName : String){
        apiCall(reservationRepository.startReservationEquipment(agencyInfo, itemName),{
            _onSuccessStartStopReserveEquipment.call() })
    }

    fun stopReservationEquipment(item : ReservationEquipmentData){
        apiCall(reservationRepository.stopReservationEquipment(agencyInfo, ReservationEquipmentData(item.icon, item.name, usable = false) ),{
            _onSuccessStartStopReserveEquipment.call() })
    }


}
