package com.example.adminapp.ui.main.reservation.log

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.adminapp.R
import com.example.adminapp.data.model.ReservationEquipmentLog
import com.example.adminapp.data.model.ReservationLogItem
import com.example.adminapp.data.model.ReservationType
import com.example.adminapp.databinding.ItemReservationLogBinding
import com.example.adminapp.ui.main.reservation.getHourMinuteString
import com.example.adminapp.ui.main.reservation.getMonthDayString
import com.example.adminapp.ui.main.reservation.getMonthString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReservationLogRVAdapter() : ListAdapter<ReservationLogItem, ReservationLogRVAdapter.ViewHolder>(AddressDiffCallback) {

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

    interface OnItemClickListener { fun onItemClick(position: Int, facilityLogData : ReservationLogItem) }

    inner class ViewHolder(val binding: ItemReservationLogBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemReservationLogBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { item ->
            CoroutineScope(Dispatchers.Main).launch {
                when (item.type){
                    ReservationType.EQUIPMENT -> {
                        item.equipmentLog?.let {
                            holder.binding.itemReservationLogReservationType.text = "바로 사용"
                            holder.binding.itemReservationLogName.text = item.equipmentLog.name
                            holder.binding.itemReservationLogMonth.text = withContext(Dispatchers.IO){ getMonthString(item.equipmentLog.startTime) }
                            holder.binding.itemReservationLogDay.text = withContext(Dispatchers.IO){ getMonthDayString(item.equipmentLog.startTime) }
                            holder.binding.itemReservationLogStartTime.text = withContext(Dispatchers.IO) { getHourMinuteString(item.equipmentLog.startTime) }
                            holder.binding.itemReservationLogEndTime.text = withContext(Dispatchers.IO) { getHourMinuteString(item.equipmentLog.endTime) }
                            holder.binding.itemReservationLogUserName.text = item.equipmentLog.userName
                            when (item.equipmentLog.reservationState) {
                                "사용중" -> holder.binding.itemReservationLogState.apply {
                                    text = item.equipmentLog.reservationState
                                    setTextColor(ContextCompat.getColor(context, R.color.pinkish_orange))
                                    setBackgroundResource(R.drawable.view_5dp_stroke_rectangle_pinkish_orange) }
                                "예약중" -> holder.binding.itemReservationLogState.apply {
                                    text = item.equipmentLog.reservationState
                                    setTextColor(ContextCompat.getColor(context, R.color.applemint))
                                    setBackgroundResource(R.drawable.view_5dp_stroke_rectangle_applemint) }
                                else -> holder.binding.itemReservationLogState.apply {
                                    text = item.equipmentLog.reservationState
                                    setTextColor(ContextCompat.getColor(context, R.color.black_60))
                                    setBackgroundResource(R.drawable.view_5dp_stroke_rectangle_black20) }
                            }
                        }
                    }
                    ReservationType.FACILITY -> {
                        item.facilityLog?.let {
                            holder.binding.itemReservationLogReservationType.text = "예약 사용"
                            holder.binding.itemReservationLogName.text = item.facilityLog.name
                            holder.binding.itemReservationLogMonth.text = withContext(Dispatchers.IO){ getMonthString(item.facilityLog.startTime) }
                            holder.binding.itemReservationLogDay.text = withContext(Dispatchers.IO){ getMonthDayString(item.facilityLog.startTime) }
                            holder.binding.itemReservationLogStartTime.text = withContext(Dispatchers.IO) { getHourMinuteString(item.facilityLog.startTime) }
                            holder.binding.itemReservationLogEndTime.text = withContext(Dispatchers.IO) { getHourMinuteString(item.facilityLog.endTime) }
                            holder.binding.itemReservationLogUserName.text = item.facilityLog.userName
                            when (item.facilityLog.reservationState) {
                                "사용중" -> holder.binding.itemReservationLogState.apply {
                                    text = item.facilityLog.reservationState
                                    setTextColor(ContextCompat.getColor(context, R.color.pinkish_orange))
                                    setBackgroundResource(R.drawable.view_5dp_stroke_rectangle_pinkish_orange) }
                                "예약중" -> holder.binding.itemReservationLogState.apply {
                                    text = item.facilityLog.reservationState
                                    setTextColor(ContextCompat.getColor(context, R.color.applemint))
                                    setBackgroundResource(R.drawable.view_5dp_stroke_rectangle_applemint) }
                                else -> holder.binding.itemReservationLogState.apply {
                                    text = item.facilityLog.reservationState
                                    setTextColor(ContextCompat.getColor(context, R.color.black_60))
                                    setBackgroundResource(R.drawable.view_5dp_stroke_rectangle_black20) }
                            }
                        }
                    }
                }
            }
        }

    }

}