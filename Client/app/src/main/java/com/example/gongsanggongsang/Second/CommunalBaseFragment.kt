package com.example.gongsanggongsang.Second

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.gongsanggongsang.R
import com.example.gongsanggongsang.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.*
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_communal_base.*

class CommunalBaseFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_communal_base, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        common_tab.addTab(common_tab.newTab().setText("사용/예약중"))
        common_tab.addTab(common_tab.newTab().setText("바로 사용"))
        common_tab.addTab(common_tab.newTab().setText("예약 사용"))

        initViewPager()
    }
    private fun initViewPager() {
        val tabLayoutTextArray = arrayOf("사용/예약중","바로 사용","예약 사용")
        val communalAdapter = CommunalAdapter(requireActivity().supportFragmentManager, lifecycle)
        common_viewpager.adapter = communalAdapter
        common_tab.addOnTabSelectedListener(object :
            OnTabSelectedListener {
            override fun onTabSelected(tab: Tab?) {
                common_viewpager.setCurrentItem(tab!!.position)
            }
            override fun onTabUnselected(tab: Tab?) {
            }
            override fun onTabReselected(tab: Tab?) {
            }
        })

        TabLayoutMediator(common_tab, common_viewpager){ tab, position->   //탭레이아웃과 뷰페이저(어댑터) 연결
            tab.text = tabLayoutTextArray[position]
        }.attach()
    }
}


