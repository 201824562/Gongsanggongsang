package com.example.adminapp.ui.main.reservation.edit

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

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