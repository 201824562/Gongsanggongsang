package com.example.adminapp.ui.main.reservation

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
import coil.load
import com.example.adminapp.R
import com.example.adminapp.data.model.ReservationEquipmentData
import com.example.adminapp.databinding.ItemReservationBinding


class ReservationEquipmentRVAdapter(private val context : Context, private val viewmodel : ReservationViewModel,
                                    private val  listener : OnItemClickListener
)
    : ListAdapter<ReservationEquipmentData, ReservationEquipmentRVAdapter.ViewHolder>(
    AddressDiffCallback
) {

    companion object {
        val AddressDiffCallback = object : DiffUtil.ItemCallback<ReservationEquipmentData>() {
            override fun areItemsTheSame(oldItem: ReservationEquipmentData, newItem: ReservationEquipmentData): Boolean {
                return (oldItem.name == newItem.name)
            }
            override fun areContentsTheSame(oldItem: ReservationEquipmentData, newItem: ReservationEquipmentData): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface OnItemClickListener { fun onItemClick(position: Int, equipmentData : ReservationEquipmentData) }

    inner class ViewHolder(val binding: ItemReservationBinding): RecyclerView.ViewHolder(binding.root) { var timer : CountDownTimer? = null }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemReservationBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { item ->
            if (holder.timer != null) { holder.timer!!.cancel() }
            holder.binding.reserveItemIcon.load(item.icon)
            holder.binding.reserveEditItemName.text = item.name
            if (!item.usable){
                holder.binding.reservationState.setTextColor(ContextCompat.getColor(context, R.color.pinkish_orange))
                holder.binding.reservationState.text = "사용불가"
                holder.binding.reserveItemIconBackground.background = ContextCompat.getDrawable(context, R.drawable.view_oval_gray)
                holder.binding.reservationUsingStateInfo.visibility = View.INVISIBLE
            }else{
                holder.binding.reservationState.setTextColor(ContextCompat.getColor(context, R.color.black_60))
                if (!item.using){
                    holder.binding.reserveItemIconBackground.background = ContextCompat.getDrawable(context, R.drawable.view_oval_gray)
                    holder.binding.reservationState.text = "사용가능"
                    holder.binding.reservationUsingStateInfo.visibility = View.INVISIBLE  }
                else{
                    holder.binding.reserveItemIconBackground.background = ContextCompat.getDrawable(context, R.drawable.view_oval_light_orange)
                    holder.binding.reservationState.text = "사용중"
                    holder.binding.reservationUsingStateInfo.visibility = View.VISIBLE
                    holder.binding.reservationEndTime.text = getHourMinuteString(item.endTime)
                    holder.timer = object : CountDownTimer(calculateDurationWithCurrent(item.endTime).toMillis(), 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            val minute = (millisUntilFinished/60000)
                            val second = (millisUntilFinished%60000)/1000
                            holder.binding.reservationLeftTimeMinute.text = minute.toString()
                            holder.binding.reservationLeftTimeSecond.text = if (second<10) "0${second}" else second.toString()
                        }
                        override fun onFinish() {
                            viewmodel.finishReservationEquipmentData(item.name)
                            viewmodel.makeReservationLogFinished(item.documentId) } }.start()
                }
            }
            holder.binding.itemContentLayout.setOnClickListener { listener.onItemClick(position, item) }
        }
    }
}