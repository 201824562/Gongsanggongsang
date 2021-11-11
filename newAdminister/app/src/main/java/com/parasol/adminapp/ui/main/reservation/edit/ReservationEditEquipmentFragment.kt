package com.parasol.adminapp.ui.main.reservation.edit

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
import com.parasol.adminapp.ui.base.BaseSessionFragment
import com.parasol.adminapp.data.model.*
import com.parasol.adminapp.databinding.FragmentReservationEditChildBinding
import com.parasol.adminapp.databinding.ItemReservationEditSettingBinding

class ReservationEditEquipmentFragment : BaseSessionFragment<FragmentReservationEditChildBinding, ReservationEditViewModel>() {

    override lateinit var viewbinding: FragmentReservationEditChildBinding
    override val viewmodel: ReservationEditViewModel by viewModels()
    private lateinit var reservationEditEquipmentSettingRVAdapter: ReservationEditEquipmentSettingRVAdapter
    private var observer : Observer<List<ReservationEquipmentSettingData>>?= null


    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewbinding = FragmentReservationEditChildBinding.inflate(inflater, container, false)
        return viewbinding.root
    }


    override fun initViewStart(savedInstanceState: Bundle?) { setRecyclerView() }

    override fun initDataBinding(savedInstanceState: Bundle?) { }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        observer = Observer { if (it.isEmpty()) showEmptyView() else showRV(it) }
        observer?.let {observer -> viewmodel.getReservationEquipmentSettingDataList().observeForever(observer) }
    }

    private fun setRecyclerView() {
        reservationEditEquipmentSettingRVAdapter = ReservationEditEquipmentSettingRVAdapter(object : ReservationEditEquipmentSettingRVAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, equipmentSettingData: ReservationEquipmentSettingData) {
                val reservationItem = ReservationItem(ReservationType.EQUIPMENT, ReservationData(equipmentSettingData.icon, equipmentSettingData.name,
                maxTime = equipmentSettingData.maxTime), listOf())
                findNavController().navigate(ReservationEditFragmentDirections
                    .actionReservationEditFragmentToReservationEditDetailFragment(reservationItem))
            }
        })
        viewbinding.reservationEditRv.adapter = reservationEditEquipmentSettingRVAdapter
    }

    private fun showEmptyView(){
        viewbinding.apply {
            reservationEditChildEmptyView.visibility = View.VISIBLE
            reservationEditRv.visibility = View.GONE
        }
    }
    private fun showRV(list : List<ReservationEquipmentSettingData>){
        viewbinding.run{
            reservationEditChildEmptyView.visibility  = View.GONE
            reservationEditRv.visibility = View.VISIBLE
            reservationEditEquipmentSettingRVAdapter.submitList(list)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        observer?.let {observer -> viewmodel.getReservationEquipmentSettingDataList().removeObserver(observer) }
    }

}

class ReservationEditEquipmentSettingRVAdapter(private val  listener : OnItemClickListener)
    : ListAdapter<ReservationEquipmentSettingData, ReservationEditEquipmentSettingRVAdapter.ViewHolder>(AddressDiffCallback) {

    companion object {
        val AddressDiffCallback = object : DiffUtil.ItemCallback<ReservationEquipmentSettingData>() {
            override fun areItemsTheSame(oldItem: ReservationEquipmentSettingData, newItem: ReservationEquipmentSettingData): Boolean {
                return (oldItem.name== newItem.name)
            }
            override fun areContentsTheSame(oldItem: ReservationEquipmentSettingData, newItem: ReservationEquipmentSettingData): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface OnItemClickListener { fun onItemClick(position: Int, equipmentSettingData : ReservationEquipmentSettingData) }

    inner class ViewHolder(val binding: ItemReservationEditSettingBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemReservationEditSettingBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { item ->
            holder.binding.reserveEditItemIcon.load(item.icon)
            holder.binding.reserveEditItemName.text = item.name
            holder.binding.reserveEditItemIntervalTimeLayout.visibility = View.GONE
            holder.binding.reserveEditItemMaxTime.text = item.getMaxTimeView()
            holder.binding.reserveEditBtn.setOnClickListener { listener.onItemClick(position, item) }
            holder.binding.itemEditSetting.setOnClickListener { listener.onItemClick(position, item) }

        }

    }

}