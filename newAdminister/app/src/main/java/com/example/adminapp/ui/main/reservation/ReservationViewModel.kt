package com.example.adminapp.ui.main.reservation

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.adminapp.base.BaseSessionViewModel
import com.example.adminapp.data.model.ReservationEquipmentData
import com.example.adminapp.data.model.ReservationEquipmentSettingData
import com.example.adminapp.data.model.ReservationFacilitySettingData
import com.example.adminapp.data.model.ReservationType
import com.example.adminapp.data.repository.ReservationRepository
import com.example.adminapp.utils.SingleLiveEvent
import io.reactivex.Completable

class ReservationViewModel(application: Application) : BaseSessionViewModel(application)  {

    private val reservationRepository: ReservationRepository = ReservationRepository.getInstance()

    private val _onSuccessGettingReserveEquipmentSettingData = SingleLiveEvent<ReservationEquipmentSettingData>()
    val onSuccessGettingReserveEquipmentSettingData : LiveData<ReservationEquipmentSettingData> get() = _onSuccessGettingReserveEquipmentSettingData

    fun getReservationEquipmentDataList(): LiveData<List<ReservationEquipmentData>> {
        return reservationRepository.getReservationEquipmentDataList(agencyInfo)
    }

    fun getReservationEquipmentSettingData(itemName : String)  {
        apiCall(reservationRepository.getReservationEquipmentSettingData(agencyInfo, itemName), {
            _onSuccessGettingReserveEquipmentSettingData.postValue(it)
        })
    }

    fun finishReservationEquipmentData(itemName : String)  {
        reservationRepository.finishReservationEquipment(agencyInfo, itemName)
    }


   /* fun getReservationFacilitySettingDataList(): LiveData<List<ReservationFacilitySettingData>> {
        return reservationRepository.getReservationFacilityDataList(agencyInfo)
    }*/


}