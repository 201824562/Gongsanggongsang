package com.example.userapp.ui.main.reservation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.userapp.base.BaseFragment
import com.example.userapp.databinding.FragmentMainhomeReservationBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

//예약 base fragment
//use tab layout, viewpager2

class ReservationFragment :
    BaseFragment<FragmentMainhomeReservationBinding, ReservationViewModel>() {
    override lateinit var viewbinding: FragmentMainhomeReservationBinding
    override val viewmodel: ReservationViewModel by viewModels()

    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentMainhomeReservationBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {

        viewbinding.commonTab.addTab(viewbinding.commonTab.newTab().setText("사용/예약중"))
        viewbinding.commonTab.addTab(viewbinding.commonTab.newTab().setText("바로 사용"))
        viewbinding.commonTab.addTab(viewbinding.commonTab.newTab().setText("예약 사용"))

        initViewPager()

    }

    override fun initDataBinding(savedInstanceState: Bundle?) {

    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
    }

    private fun initViewPager() {
        viewbinding.run {
            val tabLayoutTextArray = arrayOf("사용/예약중", "바로 사용", "예약 사용")
            val reservationViewPagerAdapter = ReservationViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle)
            viewbinding.commonViewpager.adapter = reservationViewPagerAdapter
            viewbinding.commonTab.addOnTabSelectedListener(object :
                TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    viewbinding.commonViewpager.currentItem = tab!!.position
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })

            TabLayoutMediator(
                viewbinding.commonTab,
                viewbinding.commonViewpager
            ) { tab, position ->   //탭레이아웃과 뷰페이저(어댑터) 연결
                tab.text = tabLayoutTextArray[position]
            }.attach()
        }
    }
}
