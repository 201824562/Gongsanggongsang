package com.example.adminapp.ui.main.reservation.log

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.adminapp.R
import com.example.adminapp.base.BaseSessionFragment
import com.example.adminapp.databinding.FragmentReservationChildLogBinding
import com.example.adminapp.ui.main.reservation.ReservationViewModel

class ReservationLogFragment : BaseSessionFragment<FragmentReservationChildLogBinding, ReservationViewModel>() {

    companion object{
        const val TAB_INDEX_ALL: Int = 0
        const val TAB_INDEX_EQUIPMENT: Int = 1
        const val TAB_INDEX_FACILITY: Int = 2
    }

    override lateinit var viewbinding: FragmentReservationChildLogBinding
    override val viewmodel: ReservationViewModel by viewModels()
    private lateinit var reservationLogViewPagerAdapter : ReservationLogViewPagerAdapter

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewbinding = FragmentReservationChildLogBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) { initViewPager() }

    override fun initDataBinding(savedInstanceState: Bundle?) { }

    override fun initViewFinal(savedInstanceState: Bundle?) { }

    private fun makeDataListener(){

    }

    private fun initViewPager() {
        viewbinding.run {
            reservationLogViewPagerAdapter  = ReservationLogViewPagerAdapter(requireActivity())
            reservationLogViewpager.adapter = reservationLogViewPagerAdapter
            reservationLogAllBtn.isSelected = true
            reservationLogAllBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.applemint))
            reservationLogAllBtn.setOnClickListener {
                makeButtonsUnselected()
                it.isSelected = true
                reservationLogAllBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.applemint))
                reservationLogViewpager.setCurrentItem(TAB_INDEX_ALL, false)
            }
            reservationLogEquipmentBtn.setOnClickListener {
                makeButtonsUnselected()
                it.isSelected = true
                reservationLogEquipmentBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.applemint))
                reservationLogViewpager.setCurrentItem(TAB_INDEX_EQUIPMENT, false)
            }
            reservationLogFacilityBtn.setOnClickListener {
                makeButtonsUnselected()
                it.isSelected = true
                reservationLogFacilityBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.applemint))
                reservationLogViewpager.setCurrentItem(TAB_INDEX_FACILITY, false)
            }
        }
    }

    private fun makeButtonsUnselected(){
        viewbinding.run {
            reservationLogAllBtn.apply {
                isSelected = false
                setTextColor(ContextCompat.getColor(context, R.color.black_20)) }
            reservationLogEquipmentBtn.apply {
                isSelected = false
                setTextColor(ContextCompat.getColor(context, R.color.black_20)) }
            reservationLogFacilityBtn.apply {
                isSelected = false
                setTextColor(ContextCompat.getColor(context, R.color.black_20)) }
        }
    }

}

class ReservationLogViewPagerAdapter (activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ReservationLogAllFragment()
            1 -> ReservationLogEquipmentFragment()
            2 -> ReservationLogFacilityFragment()
            else -> error("no such position: $position")
        }
    }
}