package com.example.adminapp.ui.main.reservation.detail

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.adminapp.base.BaseSessionViewModel
import com.example.adminapp.data.model.*
import com.example.adminapp.data.repository.ReservationRepository
import com.example.adminapp.utils.SingleLiveEvent

class ReservationDetailFacilityViewModel (application: Application) : BaseSessionViewModel(application) {

    private val reservationRepository: ReservationRepository = ReservationRepository.getInstance()

    fun getReservationFacilitySettingData(itemName : String) : LiveData<ReceiverFacilitySettingData> {
        return reservationRepository.getReservationFacilitySettingData(agencyInfo, itemName)
    }

    fun getReservationFacilityLogData(itemName : String): LiveData<ReceiverFacilityLog> {
        return reservationRepository. getReservationFacilityLogData(agencyInfo, itemName)
    }

    fun getReservationFacilityLogDataList (itemType : String, itemName : String): LiveData<List<ReservationFacilityLog>> {
        return reservationRepository.getReservationFacilityLogDataList(agencyInfo, itemType, itemName)
    }
    fun getReservationFacilityNotDoneLogDataList(itemType : String, itemName : String): LiveData<List<ReservationFacilityLog>> {
        return reservationRepository.getReservationFacilityNotDoneLogDataList(agencyInfo, itemType, itemName)
    }

    fun finishReservationFacilityLogData(logDocumentId : String){
        reservationRepository.finishReservationFacility(agencyInfo, logDocumentId)
    }

    fun cancelReservationFacilityLogData(logDocumentId : String){
        reservationRepository.cancelReservationFacility(agencyInfo, logDocumentId)
    }

    fun startReservationFacility(itemName : String){
        apiCall(reservationRepository.startReservationFacility(agencyInfo, itemName))
    }

    fun stopReservationFacility(itemName : String) {
        apiCall(reservationRepository.stopReservationFacility(agencyInfo, itemName ))
    }


}