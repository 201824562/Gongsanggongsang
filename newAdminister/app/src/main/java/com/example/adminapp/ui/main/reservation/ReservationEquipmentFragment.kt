package com.example.adminapp.ui.main.reservation

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.adminapp.databinding.FragmentReservationChildTypesBinding
import com.example.adminapp.databinding.ItemReservationBinding
import com.example.adminapp.ui.main.MainFragmentDirections
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ReservationEquipmentFragment : BaseSessionFragment<FragmentReservationChildTypesBinding, ReservationViewModel>() {

    override lateinit var viewbinding: FragmentReservationChildTypesBinding
    override val viewmodel: ReservationViewModel by viewModels()
    private lateinit var reservationEquipmentRVAdapter: ReservationEquipmentRVAdapter
    private var equipmentDataBundle : ReservationEquipmentData? = null

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewbinding = FragmentReservationChildTypesBinding.inflate(inflater, container, false)
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
            if (it.isEmpty()){ showEmptyView() }
            else showRV(it)
        }
    }

    private fun setRecyclerView() {
        reservationEquipmentRVAdapter = ReservationEquipmentRVAdapter(requireContext(), viewmodel, object : ReservationEquipmentRVAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, equipmentData: ReservationEquipmentData) {
                equipmentDataBundle = equipmentData
                viewmodel.getReservationEquipmentSettingData(equipmentData.name)
            }
        })
        viewbinding.reservationRv.adapter = reservationEquipmentRVAdapter
    }

    private fun showEmptyView(){
        viewbinding.apply {
            reservationChildEmptyView.visibility = View.VISIBLE
            reservationRv.visibility = View.GONE
        }
    }
    private fun showRV(list : List<ReservationEquipmentData>){
        viewbinding.run{
            reservationChildEmptyView.visibility  = View.GONE
            reservationRv.visibility = View.VISIBLE
            reservationEquipmentRVAdapter.submitList(list)
        }
    }
}


