package com.example.userapp.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.userapp.ui.main.alarm.AlarmFragment
import com.example.userapp.ui.main.home.HomeFragment
import com.example.userapp.ui.main.settings.SettingsFragment

class MainViewPagerAdapter (activity: FragmentActivity) : FragmentStateAdapter(activity){

    override fun getItemCount(): Int = 3 //PagerViewadapter에서 관리할 View 개수를 반환한다.

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> HomeFragment()
            1 -> AlarmFragment()
            2 -> SettingsFragment()
            else -> error("no such position: $position")
        }
    }
}