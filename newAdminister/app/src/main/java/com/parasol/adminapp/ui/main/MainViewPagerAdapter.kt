package com.parasol.adminapp.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.parasol.adminapp.ui.main.alarm.AlarmFragment
import com.parasol.adminapp.ui.main.community.CommunityFragment
import com.parasol.adminapp.ui.main.home.HomeFragment
import com.parasol.adminapp.ui.main.reservation.ReservationFragment
import com.parasol.adminapp.ui.main.settings.SettingsFragment

class MainViewPagerAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle){

    override fun getItemCount(): Int = 5 //PagerViewadapter에서 관리할 View 개수를 반환한다.

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> HomeFragment()
            1 -> ReservationFragment()
            2 -> CommunityFragment()
            3 -> AlarmFragment()
            4 -> SettingsFragment()
            else -> error("no such position: $position")
        }
    }
}