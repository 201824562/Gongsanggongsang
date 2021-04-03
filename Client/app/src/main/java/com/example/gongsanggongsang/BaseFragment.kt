package com.example.gongsanggongsang

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.fragment_base.*

class BaseFragment : Fragment() {
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_base, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewPager()
    }

    private fun initViewPager() {

        //main_pager.adapter = ViewPagerAdapter(this) //뷰페이저와 뷰페이저어댑터 연결
        main_pager.offscreenPageLimit = 5                //뷰계층구조에 보관된 페이지, view/fragment 수를 제어할 수 있다.
        viewPagerAdapter = ViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle)
        main_pager.adapter = viewPagerAdapter
        main_pager.registerOnPageChangeCallback(PageChangeCallback())
        main_bottom_navigation.setOnNavigationItemSelectedListener { navigationSelected(it) } //바텀네비게이션뷰와 셀렉팅리스너 연결


        /*
        TabLayoutMediator(tab_layout, nav_host_fragment){ tab, position->   //탭레이아웃과 뷰페이저(어댑터) 연결
            tab.text = tabLayoutTextArray[position]
            tab.setIcon(tabLayoutIconList[position])
        }.attach() */

        main_pager.isUserInputEnabled = false

    }

    private fun navigationSelected(item: MenuItem): Boolean {
        val checked = item.setChecked(true)
        when (checked.itemId) {
            R.id.first -> {
                main_pager.currentItem = 0
                return true
            }
            R.id.second-> {
                main_pager.currentItem = 1
                return true
            }
            R.id.third-> {
                main_pager.currentItem = 2
                return true
            }
            R.id.fourth-> {
                main_pager.currentItem = 3
                return true
            }
            R.id.fifth-> {
                main_pager.currentItem = 4
                return true
            }
        }
        return false
    }

    private inner class PageChangeCallback: ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            main_bottom_navigation.selectedItemId = when (position) {
                0 -> R.id.first
                1 -> R.id.second
                2 -> R.id.third
                3 -> R.id.fourth
                4 -> R.id.fifth
                else -> error("no such position: $position")
            }
        }
    }




}