package com.example.adminapp.ui.main.reservation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.adminapp.R
import com.example.adminapp.ui.base.BaseSessionFragment
import com.example.adminapp.databinding.FragmentMainReservationBinding
import com.example.adminapp.ui.main.reservation.log.ReservationLogFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ReservationFragment : BaseSessionFragment<FragmentMainReservationBinding, ReservationViewModel>() {


    override lateinit var viewbinding: FragmentMainReservationBinding
    override val viewmodel: ReservationViewModel by viewModels()
    private lateinit var reservationViewPagerAdapter : ReservationViewPagerAdapter

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewbinding = FragmentMainReservationBinding.inflate(inflater, container, false)
        return viewbinding.root }

    override fun initViewStart(savedInstanceState: Bundle?) { initViewPager() }

    override fun initDataBinding(savedInstanceState: Bundle?) { }

    //TODO : 인디케이터 색이 안 바뀜.
    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.run {
            reservationSettingBtn.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_reservationEditFragment)
            }
            reservationAddBtn.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_reservationSelectAddFragment)
            }
        }
    }

    private fun initViewPager() {
        viewbinding.run {
            reservationViewPagerAdapter  = ReservationViewPagerAdapter(requireActivity())
            reservationViewpager.adapter = reservationViewPagerAdapter
            reservationTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) { viewbinding.reservationViewpager.currentItem = tab!!.position }
                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) { viewbinding.reservationViewpager.currentItem = tab!!.position } })
            TabLayoutMediator(reservationTab, reservationViewpager){ tab, position ->
                val tabTextList = arrayListOf("사용중", "바로 사용", "예약 사용", "사용 기록")
                tab.text = tabTextList[position] }.attach() }
    }

}

class ReservationViewPagerAdapter (activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ReservationUsingFragment()
            1 -> ReservationEquipmentFragment()
            2 -> ReservationFacilityFragment()
            3 -> ReservationLogFragment()
            else -> error("no such position: $position")
        }
    }
}