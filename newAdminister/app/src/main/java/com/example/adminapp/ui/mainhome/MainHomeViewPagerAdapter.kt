package com.example.adminapp.ui.mainhome

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.adminapp.ui.mainhome.community.CommunityFragment
import com.example.adminapp.ui.mainhome.home.HomeFragment
import com.example.adminapp.ui.mainhome.reservation.ReservationFragment
import com.example.adminapp.ui.mainhome.settings.SettingsFragment

class MainHomeViewPagerAdapter (activity: FragmentActivity) : FragmentStateAdapter(activity){

    override fun getItemCount(): Int = 4 //PagerViewadapter에서 관리할 View 개수를 반환한다.

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> HomeFragment()
            1 -> ReservationFragment()
            2 -> CommunityFragment()
            3 -> SettingsFragment()
            else -> error("no such position: $position")
        }
    }
}