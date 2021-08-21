package com.example.adminapp.ui.main.reservation.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.adminapp.data.model.ReservationLogItem
import com.example.adminapp.data.model.ReservationType
import com.example.adminapp.databinding.ItemReservationDetailBinding
import com.example.adminapp.ui.main.reservation.getHourMinuteString
import com.example.adminapp.ui.main.reservation.getMonthDayString
import com.example.adminapp.ui.main.reservation.getMonthString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReservationDetailLogRVAdapter() : ListAdapter<ReservationLogItem, ReservationDetailLogRVAdapter.ViewHolder>(
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