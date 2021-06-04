package com.example.gongsanggongsang.Second

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class CommunalAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle){
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> {return CommunalCurrentFragment()}
            1 -> {return CommunalEquipmentFragment()}
            2 -> {return CommunalFacilityFragment()}
            else -> {return CommunalCurrentFragment()}
        }
    }
}
