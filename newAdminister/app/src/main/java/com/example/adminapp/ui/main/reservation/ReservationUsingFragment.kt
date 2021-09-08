package com.example.adminapp.ui.main.reservation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.adminapp.ui.base.BaseSessionFragment
import com.example.adminapp.data.model.*
import com.example.adminapp.databinding.FragmentReservationChildUsingBinding
import com.example.adminapp.ui.main.MainFragmentDirections

class ReservationUsingFragment : BaseSessionFragment<FragmentReservationChildUsingBinding, ReservationViewModel>() {

    override lateinit var viewbinding: FragmentReservationChildUsingBinding
    override val viewmodel: ReservationViewModel by viewModels()
    private lateinit var reservationEquipmentRVAdapter: ReservationEquipmentRVAdapter
    private lateinit var reservationFacilityRVAdapter: ReservationFacilityLogRVAdapter
    private var equipmentDataBundle : ReservationEquipmentData? = null
    private var facilityDataBundle: ReservationFacilityBundle? = null

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewbinding = FragmentReservationChildUsingBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) { setRecyclerView() }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.onSuccessGettingReserveEquipmentSettingData.observe(viewLifecycleOwner){
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToReservationDetailEquipmentFragment(equipmentDataBundle, it))
        }
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewmodel.getReservationUsingEquipmentDataList().observe(viewLifecycleOwner){
            if (it.isEmpty()){ showEquipmentEmptyView() }
            else showEquipmentRV(it)
        }
        viewmodel.getReservationUsingFacilityLogList().observe(viewLifecycleOwner){
            if (it.isEmpty()){ showFacilityEmptyView() }
            else showFacilityRV(it)
        }
    }

    private fun setRecyclerView() {
        reservationEquipmentRVAdapter = ReservationEquipmentRVAdapter(requireContext(), viewmodel, object : ReservationEquipmentRVAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, equipmentData: ReservationEquipmentData) {
                equipmentDataBundle = equipmentData
                viewmodel.getReservationEquipmentSettingData(equipmentData.name)
            } })
        viewbinding.reservationRv1.adapter = reservationEquipmentRVAdapter

        reservationFacilityRVAdapter = ReservationFacilityLogRVAdapter(requireContext(), viewmodel, object : ReservationFacilityLogRVAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, facilityLogData: ReservationFacilityLog) {
                facilityDataBundle = ReservationFacilityBundle(true, facilityLogData.name, facilityLogData, null)
                findNavController().navigate(MainFragmentDirections.actionMainFragmentToReservationDetailFacilityFragment(facilityBundle = facilityDataBundle))
            }
        })
        viewbinding.reservationRv2.adapter = reservationFacilityRVAdapter
    }

    private fun showEquipmentEmptyView(){
        viewbinding.apply {
            reservationUsingEmptyView1.visibility = View.VISIBLE
            reservationRv1.visibility = View.GONE
        }
    }
    private fun showEquipmentRV(list : List<ReservationEquipmentData>){
        viewbinding.run{
            reservationUsingEmptyView1.visibility  = View.GONE
            reservationRv1.visibility = View.VISIBLE
            reservationEquipmentRVAdapter.submitList(list)
        }
    }
    private fun showFacilityEmptyView(){
        viewbinding.apply {
            reservationUsingEmptyView2.visibility = View.VISIBLE
            reservationRv2.visibility = View.GONE
        }
    }
    private fun showFacilityRV(list : List<ReservationFacilityLog>){
        viewbinding.run{
            reservationUsingEmptyView2.visibility  = View.GONE
            reservationRv2.visibility = View.VISIBLE
            reservationFacilityRVAdapter.submitList(list)
        }
    }

}

