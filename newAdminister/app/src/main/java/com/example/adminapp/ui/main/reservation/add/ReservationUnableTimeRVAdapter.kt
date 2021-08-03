package com.example.adminapp.ui.main.reservation.add

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.adminapp.data.model.ReservationUnableTimeItem
import com.example.adminapp.databinding.ItemReservationAddUnabletimesBinding

class ReservationUnableTimeRVAdapter (val  listener : OnItemClickListener): ListAdapter<ReservationUnableTimeItem, ReservationUnableTimeRVAdapter.ViewHolder>(
    AddressDiffCallback
) {


    companion object {
        val AddressDiffCallback = object : DiffUtil.ItemCallback<ReservationUnableTimeItem>() {
            override fun areItemsTheSame(oldData: ReservationUnableTimeItem, newData: ReservationUnableTimeItem): Boolean {
                return oldData == newData
            }
            override fun areContentsTheSame(oldData: ReservationUnableTimeItem, newData: ReservationUnableTimeItem): Boolean {
                return oldData == newData
            }
        }
    }

    interface OnItemClickListener { fun onItemClick(currentItemList : List<ReservationUnableTimeItem>) }

    inner class ViewHolder(val binding: ItemReservationAddUnabletimesBinding): RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemReservationAddUnabletimesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let{ item ->
            holder.binding.itemAddUnableView.isSelected = item.unable
            holder.binding.itemAddUnableText.text = item.data.makeTimeDataToString()
            holder.binding.itemAddUnableView.setOnClickListener {
                when(item.unable){
                    true -> {
                        item.unable = false
                        it.isSelected = false
                    }
                    false -> {
                        item.unable = true
                        it.isSelected = true
                    }
                }
                listener.onItemClick(currentList)
            }
        }
    }

}
