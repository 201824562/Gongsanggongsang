package com.example.adminapp.ui.main.reservation.edit

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

//TODO : 탭 레이아웃 상태 유지가 안됨.
class ReservationEditViewPagerAdapter (activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ReservationEditEquipmentFragment()
            1 -> ReservationEditFacilityFragment()
            else -> error("no such position: $position")
        }
    }
}