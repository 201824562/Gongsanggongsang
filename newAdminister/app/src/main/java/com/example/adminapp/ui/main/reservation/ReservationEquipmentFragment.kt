package com.example.adminapp.ui.main.reservation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.adminapp.R
import com.example.adminapp.base.BaseSessionFragment
import com.example.adminapp.data.model.*
import com.example.adminapp.databinding.FragmentReservationChildBinding
import com.example.adminapp.databinding.ItemReservationBinding
import com.example.adminapp.ui.main.MainFragmentDirections
import com.example.adminapp.ui.main.reservation.edit.ReservationEditFragmentDirections


class ReservationEquipmentFragment : BaseSessionFragment<FragmentReservationChildBinding, ReservationViewModel>() {

    override lateinit var viewbinding: FragmentReservationChildBinding
    override val viewmodel: ReservationViewModel by viewModels()
    private lateinit var reservationEquipmentRVAdapter: ReservationEquipmentRVAdapter
    private var equipmentDataBundle : ReservationEquipmentData? = null

    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentReservationChildBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        viewbinding.reservationChildText.text = "사용하기"
        setRecyclerView() }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.onSuccessGettingReserveEquipmentSettingData.observe(viewLifecycleOwner){
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToReservationDetailEquipmentFragment(equipmentDataBundle, it))
        }
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewmodel.getReservationEquipmentDataList().observe(viewLifecycleOwner){
            reservationEquipmentRVAdapter.submitList(it)
        }
    }

    private fun setRecyclerView() {
        reservationEquipmentRVAdapter = ReservationEquipmentRVAdapter(requireContext(), object : ReservationEquipmentRVAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, equipmentData: ReservationEquipmentData) {
                equipmentDataBundle = equipmentData
                viewmodel.getReservationEquipmentSettingData(equipmentData.name)
            }
        })
        viewbinding.reservationRv.adapter = reservationEquipmentRVAdapter
    }
}

class ReservationEquipmentRVAdapter(private val context : Context, private val  listener : OnItemClickListener)
    : ListAdapter<ReservationEquipmentData, ReservationEquipmentRVAdapter.ViewHolder>(AddressDiffCallback) {

    companion object {
        val AddressDiffCallback = object : DiffUtil.ItemCallback<ReservationEquipmentData>() {
            override fun areItemsTheSame(oldItem: ReservationEquipmentData, newItem: ReservationEquipmentData): Boolean {
                return (oldItem == newItem)
            }
            override fun areContentsTheSame(oldItem: ReservationEquipmentData, newItem: ReservationEquipmentData): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface OnItemClickListener { fun onItemClick(position: Int, equipmentData : ReservationEquipmentData) }

    inner class ViewHolder(val binding: ItemReservationBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemReservationBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { item ->
            holder.binding.reserveItemIcon.load(item.icon)
            holder.binding.reserveEditItemName.text = item.name
            if (!item.usable){
                holder.binding.reservationState.setTextColor(ContextCompat.getColor(context, R.color.pinkish_orange))
                holder.binding.reservationState.text = "강제종료모드"
                holder.binding.reserveItemIconBackground.background = ContextCompat.getDrawable(context, R.drawable.view_oval_gray)
                holder.binding.reservationUsingStateInfo.visibility = View.INVISIBLE
            }else{
                holder.binding.reservationState.setTextColor(ContextCompat.getColor(context, R.color.black_60))
                if (!item.using){
                    holder.binding.reserveItemIconBackground.background = ContextCompat.getDrawable(context, R.drawable.view_oval_gray)
                    holder.binding.reservationState.text = "대기중"
                    holder.binding.reservationUsingStateInfo.visibility = View.INVISIBLE  }
                else{
                    holder.binding.reserveItemIconBackground.background = ContextCompat.getDrawable(context, R.drawable.view_oval_light_orange)
                    holder.binding.reservationState.text = "사용중"
                    holder.binding.reservationUsingStateInfo.visibility = View.VISIBLE
                    holder.binding.reservationEndTime.text = item.endTime //TODO : 계산필요
                    holder.binding.reservationLeftTime.text = item.endTime //TODO : 계산필요
                }
            }
            holder.binding.itemEditSetting.setOnClickListener { listener.onItemClick(position, item) }
        }

    }

}