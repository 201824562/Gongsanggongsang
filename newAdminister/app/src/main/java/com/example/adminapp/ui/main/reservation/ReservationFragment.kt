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
import com.example.adminapp.base.BaseSessionFragment
import com.example.adminapp.databinding.FragmentMainReservationBinding
import com.example.adminapp.ui.main.reservation.log.ReservationLogFragment
import com.google.android.material.tabs.TabLayout

class ReservationFragment : BaseSessionFragment<FragmentMainReservationBinding, ReservationViewModel>() {

    companion object{
        const val TAB_INDEX_USING: Int = 0
        const val TAB_INDEX_EQUIPMENT: Int = 1
        const val TAB_INDEX_FACILITY: Int = 2
        const val TAB_INDEX_LOG: Int = 3
    }

    override lateinit var viewbinding: FragmentMainReservationBinding
    override val viewmodel: ReservationViewModel by viewModels()
    private lateinit var reservationViewPagerAdapter : ReservationViewPagerAdapter

    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentMainReservationBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        initViewPager()
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
    }

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
            reservationTab.addOnTabSelectedListener(object :
                TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when(tab?.position) {
                        TAB_INDEX_USING -> viewbinding.reservationViewpager.setCurrentItem(TAB_INDEX_USING)
                        TAB_INDEX_EQUIPMENT -> viewbinding.reservationViewpager.setCurrentItem(TAB_INDEX_EQUIPMENT)
                        TAB_INDEX_FACILITY -> viewbinding.reservationViewpager.setCurrentItem(TAB_INDEX_FACILITY)
                        TAB_INDEX_LOG -> viewbinding.reservationViewpager.setCurrentItem(TAB_INDEX_LOG)
                        else -> throw IllegalStateException("에러가 발생했습니다. 다시 시도해주세요.")
                    }
                }
                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })

        }
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