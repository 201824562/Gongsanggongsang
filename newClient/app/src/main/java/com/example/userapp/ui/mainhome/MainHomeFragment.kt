package com.example.userapp.ui.mainhome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.userapp.R
import com.example.userapp.base.BaseFragment
import com.example.userapp.databinding.FragmentMainhomeBinding

class MainFragment : BaseFragment<FragmentMainhomeBinding,MainHomeViewModel>(){

    override lateinit var viewbinding: FragmentMainhomeBinding
    override val viewmodel: MainHomeViewModel by viewModels()
    private var mainHomeViewPagerAdapter : MainHomeViewPagerAdapter? = null

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewbinding = FragmentMainhomeBinding.inflate(inflater, container, false)
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

            mainHomeViewPagerAdapter = MainHomeViewPagerAdapter(requireActivity())
            mainhomeViewpager.apply {
                offscreenPageLimit = 4
                adapter = mainHomeViewPagerAdapter
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        mainhomeBottomNavi.selectedItemId = when(position){
                            0 -> R.id.btn_home
                            1 -> R.id.btn_reservation
                            2 -> R.id.btn_community
                            3 -> R.id.btn_mypage
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
                    R.id.btn_mypage -> {
                        mainhomeViewpager.setCurrentItem(3, false)
                        return@setOnNavigationItemSelectedListener true }
                    else -> error("no such position!")
                }
                return@setOnNavigationItemSelectedListener false
            }
        }
    }

}