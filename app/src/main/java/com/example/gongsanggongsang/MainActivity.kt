package com.example.gongsanggongsang

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViewPager()
    }
    private fun initViewPager(){
        val tabLayoutTextArray = arrayOf("홈", "게시판", "예약", "냉장고", "내정보")
        val tabLayoutIconList = arrayListOf(R.drawable.basic_icon, R.drawable.basic_icon, R.drawable.basic_icon, R.drawable.basic_icon, R.drawable.basic_icon)
        nav_host_fragment.adapter = ViewPagerAdapter(this)
        TabLayoutMediator(tab_layout, nav_host_fragment){tab, position->
            tab.text = tabLayoutTextArray[position]
            tab.setIcon(tabLayoutIconList[position])
        }.attach()
    }


}