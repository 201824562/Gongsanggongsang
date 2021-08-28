package com.example.adminapp.ui.main.reservation

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.adminapp.base.BaseSessionViewModel
import com.example.adminapp.data.model.*
import com.example.adminapp.data.repository.ReservationRepository
import com.example.adminapp.utils.SingleLiveEvent

class ReservationViewModel(application: Application) : BaseSessionViewModel(application)  {

    private val reservationRepository: ReservationRepository = ReservationRepository.getInstance()

    private val _onSuccessGettingReserveEquipmentSettingData = SingleLiveEvent<ReservationEquipmentSettingData>()
    val onSuccessGettingReserveEquipmentSettingData : LiveData<ReservationEquipmentSettingData> get() = _onSuccessGettingReserveEquipmentSettingData

    fun getReservationUsingEquipmentDataList(): LiveData<List<ReservationEquipmentData>>{
        return reservationRepository.getReservationUsingEquipmentDataList(agencyInfo)
    }
    fun getReservationEquipmentDataList(): LiveData<List<ReservationEquipmentData>> {
        return reservationRepository.getReservationEquipmentDataList(agencyInfo)
    }
    fun getReservationEquipmentSettingData(itemName : String)  {
        apiCall(reservationRepository.getReservationEquipmentSettingData(agencyInfo, itemName), {
            _onSuccessGettingReserveEquipmentSettingData.postValue(it) })
    }
    fun getReservationUsingFacilityLogList() : LiveData<List<ReservationFacilityLog>>{
        return reservationRepository.getReservationUsingFacilityLogList(agencyInfo, 0)
    }
    fun getReservationFacilitySettingDataList() : LiveData<List<ReservationFacilitySettingData>>{
        return reservationRepository.getReservationFacilitySettingDataList(agencyInfo)
    }
    fun getReservationFacilityLogList() : LiveData<List<ReservationFacilityLog>>{
        return reservationRepository.getReservationUsingFacilityLogList(agencyInfo, 1)
    }
    fun getReservationLogDataList(index: Int) : LiveData<List<ReservationLogItem>> {
        return reservationRepository.getReservationLogDataList(agencyInfo, index)
    }
    fun finishReservationEquipmentData(itemName : String)  {
        reservationRepository.stopReservationEquipment(agencyInfo, itemName)
    }
    fun finishReservationFacilityLogData(logDocumentId : String){
        reservationRepository.makeReservationLogFinished(agencyInfo, logDocumentId)
    }
    fun makeReservationLogFinished (documentId: String){
        reservationRepository.makeReservationLogFinished(agencyInfo, documentId)
    }


   /* fun getReservationFacilitySettingDataList(): LiveData<List<ReservationFacilitySettingData>> {
        return reservationRepository.getReservationFacilityDataList(agencyInfo)
    }*/


}