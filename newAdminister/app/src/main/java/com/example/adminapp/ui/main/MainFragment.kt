package com.example.adminapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.navigation.ui.AppBarConfiguration
import androidx.viewpager2.widget.ViewPager2
import com.example.adminapp.R
import com.example.adminapp.base.BaseFragment
import com.example.adminapp.databinding.FragmentMainBinding
import com.example.adminapp.utils.setupKeyboardHide

class MainFragment : BaseFragment<FragmentMainBinding, MainViewModel>(){

    override lateinit var viewbinding: FragmentMainBinding
    override val viewmodel: MainViewModel by viewModels()

    private var mainViewPagerAdapter : MainViewPagerAdapter? = null
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewbinding = FragmentMainBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        requireActivity().onBackPressedDispatcher.addCallback(this) { viewmodel.onBackPressed() }
        initViewPager()
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.onBackPressedEventLiveData.observe(viewLifecycleOwner){ requireActivity().finish() }
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {}


    private fun initViewPager(){
        viewbinding.run{
            mainViewPagerAdapter = MainViewPagerAdapter(requireActivity())
            mainhomeViewpager.apply {
                offscreenPageLimit = 5
                adapter = mainViewPagerAdapter
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        mainhomeBottomNavi.selectedItemId = when(position){
                            0 -> R.id.btn_home
                            1 -> R.id.btn_reservation
                            2 -> R.id.btn_community
                            3 -> R.id.btn_alarm
                            4 -> R.id.btn_mypage
                            else -> error("no such position: $position") } } })
                isUserInputEnabled = false
            }

            mainhomeBottomNavi.setOnNavigationItemSelectedListener { itemclicklistener ->
                when(itemclicklistener.setChecked(true).itemId){
                    R.id.btn_home -> {
                        mainhomeViewpager.setCurrentItem(0, false)
                        return@setOnNavigationItemSelectedListener true
                    }
                    R.id.btn_reservation -> {
                        mainhomeViewpager.setCurrentItem(1, false)
                        return@setOnNavigationItemSelectedListener true
                    }
                    R.id.btn_community -> {
                        mainhomeViewpager.setCurrentItem(2, false)
                        return@setOnNavigationItemSelectedListener true
                    }
                    R.id.btn_alarm -> {
                        mainhomeViewpager.setCurrentItem(3, false)
                        return@setOnNavigationItemSelectedListener true
                    }
                    R.id.btn_mypage -> {
                        mainhomeViewpager.setCurrentItem(4, false)
                        return@setOnNavigationItemSelectedListener true }
                    else -> error("no such position!")
                }
                return@setOnNavigationItemSelectedListener false
            }
        }
    }

}