package com.example.adminapp.ui.main.alarm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.adminapp.base.BaseFragment
import com.example.adminapp.base.BaseSessionFragment
import com.example.adminapp.databinding.FragmentMainhomeAlarmBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

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
                val tabTextList = arrayListOf("전체","공지/긴급","건의","가입승인","퇴실신청")
                tab.text = tabTextList[position] }.attach() }
    }
}

class AlarmViewPagerAdapter (activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AlarmAllFragment()
            1 -> AlarmNoticeFragment()
            2 -> AlarmSuggestFragment()
            3 -> AlarmAllowFragment()
            4 -> AlarmOutFragment()
            else -> error("no such position: $position")
        }
    }
}