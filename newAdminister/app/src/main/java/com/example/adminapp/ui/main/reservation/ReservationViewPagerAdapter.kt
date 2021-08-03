package com.example.adminapp.ui.main.reservation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ReservationViewPagerAdapter (activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ReservationUsingFragment()
            1 -> ReservationEquipmentFragment()
            2 -> ReservationFacilityFragment()
            3 -> ReservationLogFragment()
            else -> error("no such position: $position")
        }
    }
}