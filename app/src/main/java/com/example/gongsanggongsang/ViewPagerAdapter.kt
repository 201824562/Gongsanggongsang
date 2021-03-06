package com.example.gongsanggongsang

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import com.google.android.material.appbar.AppBarLayout
import androidx.viewpager2.adapter.FragmentStateAdapter as FragmentStateAdapter

class ViewPagerAdapter(fm: FragmentActivity) : FragmentStateAdapter(fm){

    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment{
        return when(position){
            0 -> FirstFragment()
            1 -> SecondFragment()
            2 -> ThirdFragment()
            3 -> FourthFragment()
            4 -> FifthFragment()
            else -> FirstFragment()
        }
    }
}