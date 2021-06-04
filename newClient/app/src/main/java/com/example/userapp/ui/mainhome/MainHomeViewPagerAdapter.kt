package com.example.userapp.ui.mainhome

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.userapp.ui.mainhome.community.CommunityMainFragment
import com.example.userapp.ui.mainhome.home.HomeFragment
import com.example.userapp.ui.mainhome.reservation.ReservationFragment
import com.example.userapp.ui.mainhome.settings.SettingsFragment

class MainHomeViewPagerAdapter (activity: FragmentActivity) : FragmentStateAdapter(activity){

    override fun getItemCount(): Int = 4 //PagerViewadapter에서 관리할 View 개수를 반환한다.

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> HomeFragment()
            1 -> ReservationFragment()
            2 -> CommunityMainFragment()
            3 -> SettingsFragment()
            else -> error("no such position: $position")
        }
    }
}