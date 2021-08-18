package com.example.adminapp.ui.main.reservation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.adminapp.R
import com.example.adminapp.base.BaseSessionFragment
import com.example.adminapp.data.model.ReservationData
import com.example.adminapp.data.model.ReservationFacilityBundle
import com.example.adminapp.data.model.ReservationItem
import com.example.adminapp.data.model.ReservationType
import com.example.adminapp.databinding.FragmentReservationDetailFacilityBinding


class ReservationDetailFacilityFragment() : BaseSessionFragment<FragmentReservationDetailFacilityBinding, ReservationDetailFacilityViewModel>() {

    companion object{
        const val TAB_INDEX_BASIC: Int = 0
        const val TAB_INDEX_LOG: Int = 1
    }

    override lateinit var viewbinding: FragmentReservationDetailFacilityBinding
    override val viewmodel: ReservationDetailFacilityViewModel by viewModels()
    private val args : ReservationDetailFacilityFragmentArgs by navArgs()
    private lateinit var facilityBundleData : ReservationFacilityBundle
    private lateinit var reservationDetailFacilityViewPagerAdapter : ReservationDetailFacilityViewPagerAdapter

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewbinding = FragmentReservationDetailFacilityBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        viewbinding.backBtn.setOnClickListener {  findNavController().navigate(R.id.action_reservationDetailEquipmentFragment_pop)  }
        when(args.facilityBundle){
            null -> makeErrorEvent()
            else -> facilityBundleData = args.facilityBundle!! }
        initViewPager()
    }

    override fun initDataBinding(savedInstanceState: Bundle?) { }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.run {
            reservationDetailSettingBtn.setOnClickListener {
                /*findNavController().navigate(ReservationDetailEquipmentFragmentDirections
                    .actionReservationDetailEquipmentFragmentToReservationEditDetailFragment(
                        ReservationItem(
                            ReservationType.EQUIPMENT, ReservationData(equipmentSettingData.icon,
                            equipmentSettingData.name, maxTime = equipmentSettingData.maxTime), listOf())
                    ))*/
            }
        }
    }

    private fun makeErrorEvent(){
        showToast("알수 없는 에러가 발생했습니다. 다시 시도해주세요.")
        findNavController().navigate(R.id.action_reservationDetailEquipmentFragment_pop)
    }

    private fun initViewPager() {
        viewbinding.run {
            reservationDetailFacilityViewPagerAdapter = ReservationDetailFacilityViewPagerAdapter(requireActivity())
            reservationLogViewpager.adapter = reservationDetailFacilityViewPagerAdapter
            reservationDetailBasicBtn.isSelected = true
            reservationDetailBasicBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_87))
            reservationDetailBasicBtn.setOnClickListener {
                makeButtonsUnselected()
                it.isSelected = true
                reservationDetailBasicBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_87))
                reservationLogViewpager.setCurrentItem(TAB_INDEX_BASIC, false)
            }
            reservationDetailLogBtn.setOnClickListener {
                makeButtonsUnselected()
                it.isSelected = true
                reservationDetailLogBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_87))
                reservationLogViewpager.setCurrentItem(TAB_INDEX_LOG, false)
            }
        }
    }

    private fun makeButtonsUnselected(){
        viewbinding.run {
            reservationDetailBasicBtn.apply {
                isSelected = false
                setTextColor(ContextCompat.getColor(context, R.color.black_20)) }
            reservationDetailLogBtn.apply {
                isSelected = false
                setTextColor(ContextCompat.getColor(context, R.color.black_20)) }
        }
    }

}

class ReservationDetailFacilityViewPagerAdapter (activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ReservationDetailFacilityBasicFragment()
            1 -> ReservationDetailFacilityLogFragment()
            else -> error("no such position: $position")
        }
    }
}