package com.example.adminapp.ui.main.reservation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.adminapp.base.BaseSessionFragment
import com.example.adminapp.data.model.ReservationData
import com.example.adminapp.data.model.ReservationFacilitySettingData
import com.example.adminapp.data.model.ReservationItem
import com.example.adminapp.data.model.ReservationType
import com.example.adminapp.databinding.FragmentReservationChildBinding
import com.example.adminapp.databinding.ItemReservationEditSettingBinding
import com.example.adminapp.ui.main.reservation.edit.ReservationEditFragmentDirections

//TODO : 후후후순위.
class ReservationFacilityFragment : BaseSessionFragment<FragmentReservationChildBinding, ReservationViewModel>() {

    override lateinit var viewbinding: FragmentReservationChildBinding
    override val viewmodel: ReservationViewModel by viewModels()
    private lateinit var reservationFacilitySettingRVAdapter: ReservationFacilitySettingRVAdapter

    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentReservationChildBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        viewbinding.reservationChildText.text = "예약하기"
    /*setRecyclerView()*/ }

    override fun initDataBinding(savedInstanceState: Bundle?) { }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        /*viewmodel.getReservationFacilitySettingDataList().observe(viewLifecycleOwner){
            reservationFacilitySettingRVAdapter.submitList(it)
        }*/
    }

/*    private fun setRecyclerView() {
        reservationFacilitySettingRVAdapter = ReservationFacilitySettingRVAdapter(object : ReservationFacilitySettingRVAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, facilitySettingData: ReservationFacilitySettingData) {
                val reservationItem = ReservationItem(
                    ReservationType.FACILITY, ReservationData(facilitySettingData.icon, facilitySettingData.name,
                    facilitySettingData.intervalTime, facilitySettingData.maxTime), facilitySettingData.unableTimeList)
                findNavController().navigate(
                    ReservationEditFragmentDirections
                        .actionReservationEditFragmentToReservationEditDetailFragment(reservationItem))
            }
        })
        viewbinding.reservationEditRv.adapter = reservationFacilitySettingRVAdapter
    }*/
}

class ReservationFacilitySettingRVAdapter(private val  listener : OnItemClickListener)
    : ListAdapter<ReservationFacilitySettingData, ReservationFacilitySettingRVAdapter.ViewHolder>(AddressDiffCallback) {

    companion object {
        val AddressDiffCallback = object : DiffUtil.ItemCallback<ReservationFacilitySettingData>() {
            override fun areItemsTheSame(oldItem: ReservationFacilitySettingData, newItem: ReservationFacilitySettingData): Boolean {
                return (oldItem.name== newItem.name)
            }
            override fun areContentsTheSame(oldItem: ReservationFacilitySettingData, newItem: ReservationFacilitySettingData): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface OnItemClickListener { fun onItemClick(position: Int, facilitySettingData : ReservationFacilitySettingData) }

    inner class ViewHolder(val binding: ItemReservationEditSettingBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemReservationEditSettingBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { item ->
            holder.binding.reserveEditItemIcon.load(item.icon)
            holder.binding.reserveEditItemName.text = item.name
            holder.binding.reserveEditItemIntervalTime.text = item.getIntervalTimeView()
            holder.binding.reserveEditItemMaxTime.text = item.getMaxTimeView()
            holder.binding.itemEditSetting.setOnClickListener { listener.onItemClick(position, item) }

        }

    }

}