package com.parasol.adminapp.ui.main.reservation.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.parasol.adminapp.data.model.ReservationLogItem
import com.parasol.adminapp.data.model.ReservationType
import com.parasol.adminapp.databinding.ItemReservationDetailBinding
import com.parasol.adminapp.ui.main.reservation.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReservationDetailLogRVAdapter(private val equipmentViewModel : ReservationDetailEquipmentViewModel?,
                                    private val facilityViewModel : ReservationDetailFacilityViewModel?)
    : ListAdapter<ReservationLogItem, ReservationDetailLogRVAdapter.ViewHolder>(
    AddressDiffCallback) {
    companion object {
        val AddressDiffCallback = object : DiffUtil.ItemCallback<ReservationLogItem>() {
            override fun areItemsTheSame(oldItem: ReservationLogItem, newItem: ReservationLogItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ReservationLogItem, newItem: ReservationLogItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface OnItemClickListener { fun onItemClick(position: Int, facilitySettingData : ReservationLogItem) }

    inner class ViewHolder(val binding: ItemReservationDetailBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemReservationDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { item ->
            CoroutineScope(Dispatchers.Main).launch {
                when (item.type){
                    ReservationType.EQUIPMENT -> {
                        item.equipmentLog?.let {
                            if (item.equipmentLog.reservationState == "사용중" || item.equipmentLog.reservationState == "예약중"){
                                val checkingPassTime = withContext(Dispatchers.IO){ calculateDurationWithCurrent(item.equipmentLog.endTime) }
                                if (checkingPassTime.isNegative) equipmentViewModel?.makeReservationLogFinished(item.equipmentLog.documentId) }
                            holder.binding.itemReservationDetailMonth.text = withContext(Dispatchers.IO){ getMonthString(item.equipmentLog.startTime) }
                            holder.binding.itemReservationDetailDay.text = withContext(Dispatchers.IO){ getMonthDayString(item.equipmentLog.startTime) }
                            holder.binding.itemReservationDetailStartTime.text = withContext(Dispatchers.IO) { getHourMinuteString(item.equipmentLog.startTime) }
                            holder.binding.itemReservationDetailEndTime.text = withContext(Dispatchers.IO) { getHourMinuteString(item.equipmentLog.endTime) }
                            holder.binding.itemReservationDetailUserName.text = item.equipmentLog.userName
                            holder.binding.itemReservationDetailState.text = item.equipmentLog.reservationState
                        }
                    }
                    ReservationType.FACILITY -> {
                        item.facilityLog?.let {
                            if (item.facilityLog.reservationState == "사용중" || item.facilityLog.reservationState == "예약중" ){
                                val checkingPassTime = withContext(Dispatchers.IO){ calculateDurationWithCurrent(item.facilityLog.endTime) }
                                if (checkingPassTime.isNegative) facilityViewModel?.makeReservationLogFinished(item.facilityLog.documentId) }
                            holder.binding.itemReservationDetailMonth.text = withContext(Dispatchers.IO){ getMonthString(item.facilityLog.startTime) }
                            holder.binding.itemReservationDetailDay.text = withContext(Dispatchers.IO){ getMonthDayString(item.facilityLog.startTime) }
                            holder.binding.itemReservationDetailStartTime.text = withContext(Dispatchers.IO) { getHourMinuteString(item.facilityLog.startTime) }
                            holder.binding.itemReservationDetailEndTime.text = withContext(Dispatchers.IO) { getHourMinuteString(item.facilityLog.endTime) }
                            holder.binding.itemReservationDetailUserName.text = item.facilityLog.userName
                            holder.binding.itemReservationDetailState.text = item.facilityLog.reservationState
                        }
                    }
                }
            }
        }
    }
}