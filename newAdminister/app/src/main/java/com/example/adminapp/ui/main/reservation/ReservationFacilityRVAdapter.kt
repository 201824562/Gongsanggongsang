package com.example.adminapp.ui.main.reservation

import android.content.Context
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.adminapp.R
import com.example.adminapp.data.model.ReservationFacilityBundle
import com.example.adminapp.data.model.ReservationFacilityData
import com.example.adminapp.data.model.ReservationFacilityLog
import com.example.adminapp.data.model.ReservationFacilitySettingData
import com.example.adminapp.databinding.ItemReservationBinding



class ReservationFacilityLogRVAdapter(private val context : Context, private val viewmodel : ReservationViewModel,
                                   private val  listener : ReservationFacilityLogRVAdapter.OnItemClickListener)
    : ListAdapter<ReservationFacilityLog, ReservationFacilityLogRVAdapter.ViewHolder>(AddressDiffCallback) {

    companion object {
        val AddressDiffCallback = object : DiffUtil.ItemCallback<ReservationFacilityLog>() {
            override fun areItemsTheSame(oldItem: ReservationFacilityLog, newItem: ReservationFacilityLog): Boolean {
                return (oldItem== newItem)
            }
            override fun areContentsTheSame(oldItem: ReservationFacilityLog, newItem: ReservationFacilityLog): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface OnItemClickListener { fun onItemClick(position: Int, facilityLogData : ReservationFacilityLog) }

    inner class ViewHolder(val binding: ItemReservationBinding): RecyclerView.ViewHolder(binding.root) { var timer : CountDownTimer? = null }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemReservationBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { item ->
            if (holder.timer != null) { holder.timer!!.cancel() }
            holder.binding.reserveItemIcon.load(item.icon)
            holder.binding.reserveEditItemName.text = item.name
            holder.binding.reservationState.setTextColor(ContextCompat.getColor(context, R.color.black_60))
            holder.binding.reserveItemIconBackground.background = ContextCompat.getDrawable(context, R.drawable.view_oval_light_orange)
            holder.binding.reservationState.text = item.reservationState
            holder.binding.reservationUsingStateInfo.visibility = View.VISIBLE
            holder.binding.reservationEndTime.text = getHourMinuteString(item.endTime)
            holder.timer = object : CountDownTimer(calculateDuration(item.endTime).toMillis(), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val minute = (millisUntilFinished/60000)
                    val second = (millisUntilFinished%60000)/1000
                    holder.binding.reservationLeftTimeMinute.text = minute.toString()
                    holder.binding.reservationLeftTimeSecond.text = if (second<10) "0${second}" else second.toString()
                }
                override fun onFinish() { viewmodel.finishReservationFacilityLogData(item.documentId) } }.start()
            holder.binding.itemContentLayout.setOnClickListener { listener.onItemClick(position, item) }
        }
    }
}

class ReservationFacilityRVAdapter(private val context : Context, private val viewmodel : ReservationViewModel,
                                   private val  listener : ReservationFacilityRVAdapter.OnItemClickListener)
    : ListAdapter<ReservationFacilityBundle, ReservationFacilityRVAdapter.ViewHolder>(AddressDiffCallback) {
    companion object {
        val AddressDiffCallback = object : DiffUtil.ItemCallback<ReservationFacilityBundle>() {
            override fun areItemsTheSame(oldItem: ReservationFacilityBundle, newItem: ReservationFacilityBundle): Boolean {
                return (oldItem== newItem)
            }
            override fun areContentsTheSame(oldItem: ReservationFacilityBundle, newItem: ReservationFacilityBundle): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface OnItemClickListener { fun onItemClick(position: Int, facilityData : ReservationFacilityBundle) }

    inner class ViewHolder(val binding: ItemReservationBinding): RecyclerView.ViewHolder(binding.root) { var timer : CountDownTimer? = null }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemReservationBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { item ->
            if (holder.timer != null) { holder.timer!!.cancel() }
            holder.binding.reserveEditItemName.text = item.name
            if (item.using){
             item.logData?.let {
                 holder.binding.reserveItemIcon.load(item.logData!!.icon)
                 holder.binding.reservationState.setTextColor(ContextCompat.getColor(context, R.color.black_60))
                 holder.binding.reserveItemIconBackground.background = ContextCompat.getDrawable(context, R.drawable.view_oval_light_orange)
                 holder.binding.reservationState.text = "사용중"
                 holder.binding.reservationUsingStateInfo.visibility = View.VISIBLE
                 holder.binding.reservationEndTime.text = getHourMinuteString(item.logData!!.endTime)
                 holder.timer = object : CountDownTimer(calculateDuration(item.logData!!.endTime).toMillis(), 1000) {
                     override fun onTick(millisUntilFinished: Long) {
                         val minute = (millisUntilFinished/60000)
                         val second = (millisUntilFinished%60000)/1000
                         holder.binding.reservationLeftTimeMinute.text = minute.toString()
                         holder.binding.reservationLeftTimeSecond.text = if (second<10) "0${second}" else second.toString() }
                     override fun onFinish() { viewmodel.finishReservationEquipmentData(item.name) } }.start() }
            }else{
                item.settingData?.let {
                    holder.binding.reserveItemIcon.load(item.settingData!!.icon)
                    if (!item.settingData!!.usable){
                        holder.binding.reservationState.setTextColor(ContextCompat.getColor(context, R.color.pinkish_orange))
                        holder.binding.reservationState.text = "예약불가"
                        holder.binding.reserveItemIconBackground.background = ContextCompat.getDrawable(context, R.drawable.view_oval_gray)
                        holder.binding.reservationUsingStateInfo.visibility = View.INVISIBLE }
                    else {
                        holder.binding.reservationState.setTextColor(ContextCompat.getColor(context, R.color.black_60))
                        holder.binding.reserveItemIconBackground.background = ContextCompat.getDrawable(context, R.drawable.view_oval_gray)
                        holder.binding.reservationState.text = "사용가능"
                        holder.binding.reservationUsingStateInfo.visibility = View.INVISIBLE }
                }
            }
            holder.binding.itemContentLayout.setOnClickListener { listener.onItemClick(position, item) }
        }
    }

}