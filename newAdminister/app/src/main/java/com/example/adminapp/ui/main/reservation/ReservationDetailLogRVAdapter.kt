package com.example.adminapp.ui.main.reservation

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.adminapp.data.model.ReservationEquipmentLog
import com.example.adminapp.data.model.ReservationFacilitySettingData
import com.example.adminapp.databinding.ItemReservationDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReservationDetailEquipmentLogRVAdapter() : ListAdapter<ReservationEquipmentLog, ReservationDetailEquipmentLogRVAdapter.ViewHolder>(AddressDiffCallback) {

    companion object {
        val AddressDiffCallback = object : DiffUtil.ItemCallback<ReservationEquipmentLog>() {
            override fun areItemsTheSame(oldItem: ReservationEquipmentLog, newItem: ReservationEquipmentLog): Boolean {
                return (oldItem.name== newItem.name)
            }
            override fun areContentsTheSame(oldItem: ReservationEquipmentLog, newItem: ReservationEquipmentLog): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface OnItemClickListener { fun onItemClick(position: Int, facilitySettingData : ReservationEquipmentLog) }

    inner class ViewHolder(val binding: ItemReservationDetailBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemReservationDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { item ->
            CoroutineScope(Dispatchers.Main).launch {
                holder.binding.itemReservationDetailMonth.text = withContext(Dispatchers.IO){ getMonthString(item.startTime) }
                holder.binding.itemReservationDetailDay.text = withContext(Dispatchers.IO){ getMonthDayString(item.startTime) }
                holder.binding.itemReservationDetailStartTime.text = withContext(Dispatchers.IO) { getHourMinuteString(item.startTime) }
                holder.binding.itemReservationDetailEndTime.text = withContext(Dispatchers.IO) { getHourMinuteString(item.endTime) }
                holder.binding.itemReservationDetailUserName.text = item.userName
                holder.binding.itemReservationDetailState.text = item.reservationState
            }
        }

    }

}