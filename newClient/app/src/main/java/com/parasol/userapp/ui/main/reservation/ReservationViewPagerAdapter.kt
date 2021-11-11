package com.parasol.userapp.ui.main.reservation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ReservationViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                ReservationCurrentFragment()
            }
            1 -> {
                ReservationEquipmentFragment()
            }
            2 -> {
                ReservationFacilityFragment()
            }
            else -> {
                ReservationCurrentFragment()
            }
        }
    }
}