package com.example.adminapp.ui.main.reservation.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.adminapp.ui.base.BaseSessionFragment
import com.example.adminapp.data.model.*
import com.example.adminapp.databinding.FragmentReservationEditChildBinding
import com.example.adminapp.databinding.ItemReservationEditSettingBinding

class ReservationEditFacilityFragment : BaseSessionFragment<FragmentReservationEditChildBinding, ReservationEditViewModel>() {

    override lateinit var viewbinding: FragmentReservationEditChildBinding
    override val viewmodel: ReservationEditViewModel by viewModels()
    private lateinit var reservationEditFacilitySettingRVAdapter: ReservationEditFacilitySettingRVAdapter
    private var observer : Observer<List<ReservationFacilitySettingData>>? = null

    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewbinding = FragmentReservationEditChildBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) { setRecyclerView() }

    override fun initDataBinding(savedInstanceState: Bundle?) { }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        observer = Observer { if (it.isEmpty()) showEmptyView() else showRV(it) }
        observer?.let {observer -> viewmodel.getReservationFacilitySettingDataList().observeForever(observer) }
    }

    private fun setRecyclerView() {
        reservationEditFacilitySettingRVAdapter = ReservationEditFacilitySettingRVAdapter(object : ReservationEditFacilitySettingRVAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, facilitySettingData: ReservationFacilitySettingData) {
                val reservationItem = ReservationItem(ReservationType.FACILITY, ReservationData(facilitySettingData.icon, facilitySettingData.name,
                    facilitySettingData.intervalTime, facilitySettingData.maxTime), facilitySettingData.unableTimeList)
                findNavController().navigate(
                    ReservationEditFragmentDirections
                    .actionReservationEditFragmentToReservationEditDetailFragment(reservationItem))
            }
        })
        viewbinding.reservationEditRv.adapter = reservationEditFacilitySettingRVAdapter
    }

    private fun showEmptyView(){
        viewbinding.apply {
            reservationEditChildEmptyView.visibility = View.VISIBLE
            reservationEditRv.visibility = View.GONE
        }
    }
    private fun showRV(list : List<ReservationFacilitySettingData>){
        viewbinding.run{
            reservationEditChildEmptyView.visibility  = View.GONE
            reservationEditRv.visibility = View.VISIBLE
            reservationEditFacilitySettingRVAdapter.submitList(list)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        observer?.let {observer -> viewmodel.getReservationFacilitySettingDataList().removeObserver(observer) }
    }

}

class ReservationEditFacilitySettingRVAdapter(private val  listener : OnItemClickListener)
    : ListAdapter<ReservationFacilitySettingData, ReservationEditFacilitySettingRVAdapter.ViewHolder>(AddressDiffCallback) {

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
            holder.binding.reserveEditBtn.setOnClickListener { listener.onItemClick(position, item) }
            holder.binding.itemEditSetting.setOnClickListener { listener.onItemClick(position, item) }

        }

    }

}