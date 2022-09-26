package com.parasol.adminapp.ui.main.alarm

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.parasol.adminapp.data.model.AlarmItem
import com.parasol.adminapp.data.model.AlarmType
import com.parasol.adminapp.databinding.ItemAlarmBinding
import com.parasol.adminapp.ui.main.reservation.getTimePassedString

class AlarmRVAdapter(private val  listener : OnItemClickListener)
    : ListAdapter<AlarmItem, AlarmRVAdapter.ViewHolder>(
    AddressDiffCallback
) {

    companion object {
        val AddressDiffCallback = object : DiffUtil.ItemCallback<AlarmItem>() {
            override fun areItemsTheSame(oldItem: AlarmItem, newItem: AlarmItem): Boolean {
                return (oldItem == newItem)
            }
            override fun areContentsTheSame(oldItem: AlarmItem, newItem: AlarmItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface OnItemClickListener { fun onItemClick(position: Int, alarmData : AlarmItem) }

    inner class ViewHolder(val binding: ItemAlarmBinding): RecyclerView.ViewHolder(binding.root) { }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemAlarmBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).let { item ->
            if (item.type == AlarmType.makeEnumDataToString(AlarmType.RESERVATION)) {
                //TODO : 추가디자인?
                holder.binding.alarmMoveBtn.visibility = View.GONE
            }
            holder.binding.alarmTypeName.text = item.type
            holder.binding.alarmPassedTime.text = getTimePassedString(item.time)
            holder.binding.alarmState.text = item.message
            holder.binding.itemContentLayout.setOnClickListener { listener.onItemClick(position, item) }
        }
    }
}