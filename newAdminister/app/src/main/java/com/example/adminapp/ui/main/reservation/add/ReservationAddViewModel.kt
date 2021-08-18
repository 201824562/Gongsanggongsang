package com.example.adminapp.ui.main.reservation.add

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.adminapp.base.BaseSessionViewModel
import com.example.adminapp.data.AppDatabase
import com.example.adminapp.data.model.*
import com.example.adminapp.data.repository.CategoryRepository
import com.example.adminapp.data.repository.ReservationRepository
import com.example.adminapp.utils.SingleLiveEvent

class ReservationAddViewModel(application: Application) : BaseSessionViewModel(application) {

    private val reservationRepository: ReservationRepository = ReservationRepository.getInstance()
    val categoryRepository: CategoryRepository = CategoryRepository.getInstance(AppDatabase.getDatabase(application, viewModelScope))

    private val _categoryItemViewList = SingleLiveEvent<List<CategoryItem>>()
    val categoryItemItemList: LiveData<List<CategoryItem>> get() = _categoryItemViewList
    private val _checkingSelectedUsingTime = SingleLiveEvent<Long?>()
    val checkingSelectedUsingTime: LiveData<Long?> get() = _checkingSelectedUsingTime
    private val _onSuccessSaveCategoryItem = SingleLiveEvent<Any>()
    val onSuccessSaveCategoryItem: LiveData<Any> get() = _onSuccessSaveCategoryItem
    private val _onSuccessDeleteCategoryItem = SingleLiveEvent<Any>()
    val onSuccessDeleteCategoryItem: LiveData<Any> get() = _onSuccessDeleteCategoryItem
    private val _onSuccessSaveReservationItem = SingleLiveEvent<Any>()
    val onSuccessSaveReservationItem: LiveData<Any> get() = _onSuccessSaveReservationItem

    fun loadCategoryItemViewList() {
        apiCall(categoryRepository.getCategoryItemInfoList(authToken),{ _categoryItemViewList.postValue(it) })
    }

    fun saveCategoryItem(item : CategoryItem){
        apiCall(categoryRepository.saveCategoryItemInfo(authToken, item),{ _onSuccessSaveCategoryItem.call() })
    }

    fun deleteCategoryItem(data: CategoryData){
        apiCall(categoryRepository.deleteCategoryItemInfo(authToken, data),{ _onSuccessDeleteCategoryItem.call()})
    }

    fun checkSelectedUsingTimeSetting(selected : Long?){
        _checkingSelectedUsingTime.postValue(selected)
    }

    fun saveReservationData(data : ReservationItem) {
        when (data.type){
            ReservationType.EQUIPMENT -> {apiCall(reservationRepository.saveEquipmentReservationData(agencyInfo, ReservationEquipmentItem
                (ReservationEquipmentSettingData(data.data.icon, data.data.name, data.data.maxTime), ReservationEquipmentData(data.data.icon, data.data.name, maxTime = data.data.maxTime)) ), {
                _onSuccessSaveReservationItem.call() }) }
            ReservationType.FACILITY ->apiCall(reservationRepository.saveFacilityReservationData(agencyInfo, ReservationFacilityItem
                (ReservationFacilitySettingData(data.data.icon, data.data.name, data.data.intervalTime, data.data.maxTime, data.unableTimeList), data.unableTimeList )), {
                _onSuccessSaveReservationItem.call() })
        }
    }


}