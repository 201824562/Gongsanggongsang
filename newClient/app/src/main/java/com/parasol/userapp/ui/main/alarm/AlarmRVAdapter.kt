package com.parasol.userapp.ui.main.alarm

import android.content.Context
import android.os.Build
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.parasol.userapp.data.model.AlarmItem
import com.parasol.userapp.data.model.AlarmType
import com.parasol.userapp.databinding.ItemAlarmBinding

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