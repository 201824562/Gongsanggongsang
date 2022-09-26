package com.parasol.userapp.ui.main.alarm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.parasol.userapp.ui.base.BaseSessionFragment
import com.parasol.userapp.databinding.FragmentMainhomeAlarmBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

//TODO : 삭제 구현 -> 아이템에 삭제버튼 달기.(viewmodel)
class AlarmFragment: BaseSessionFragment<FragmentMainhomeAlarmBinding, AlarmViewModel>(){

    override lateinit var viewbinding: FragmentMainhomeAlarmBinding
    override val viewmodel: AlarmViewModel by viewModels()
    private lateinit var alarmViewPagerAdapter : AlarmViewPagerAdapter

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewbinding = FragmentMainhomeAlarmBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {  initViewPager() }

    override fun initDataBinding(savedInstanceState: Bundle?) {

    }

    override fun initViewFinal(savedInstanceState: Bundle?) {

    }

    private fun initViewPager() {
        viewbinding.run {
            alarmViewPagerAdapter  = AlarmViewPagerAdapter(requireActivity())
            alarmViewpager.adapter = alarmViewPagerAdapter
            alarmTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) { viewbinding.alarmViewpager.setCurrentItem(tab!!.position, false) }
                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) { viewbinding.alarmViewpager.setCurrentItem(tab!!.position, false) } })
            TabLayoutMediator(alarmTab, alarmViewpager){ tab, position ->
                val tabTextList = arrayListOf("전체", "공지/긴급","커뮤니티", "공용")
                tab.text = tabTextList[position] }.attach() }
    }
}

class AlarmViewPagerAdapter (activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AlarmAllFragment()
            1 -> AlarmNoticeFragment()
            2 -> AlarmCommunityFragment()
            3 -> AlarmReservationFragment()
            else -> error("no such position: $position")
        }
    }
}