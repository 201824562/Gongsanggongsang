package com.example.gongsanggongsang


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.example.gongsanggongsang.Fifth.FifthFragment
import com.example.gongsanggongsang.First.FirstFragment
import com.example.gongsanggongsang.Fourth.FourthFragment
import com.example.gongsanggongsang.Second.SecondFragment
import com.example.gongsanggongsang.Third.ThirdFragment
import androidx.viewpager2.adapter.FragmentStateAdapter as FragmentStateAdapter

class ViewPagerAdapter(fm: FragmentManager, lc:Lifecycle) : FragmentStateAdapter(fm,lc){

    override fun getItemCount(): Int = 2 //PagerViewadapter에서 관리할 View 개수를 반환한다.

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

    //추가?


}