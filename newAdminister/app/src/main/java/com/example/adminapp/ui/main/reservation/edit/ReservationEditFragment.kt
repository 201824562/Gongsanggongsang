package com.example.adminapp.ui.main.reservation.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.adminapp.R
import com.example.adminapp.base.BaseFragment
import com.example.adminapp.base.BaseSessionFragment
import com.example.adminapp.databinding.FragmentReservationEditBinding
import com.example.adminapp.ui.main.MainViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ReservationEditFragment : BaseSessionFragment<FragmentReservationEditBinding, ReservationEditViewModel>() {

    companion object{
        const val TAB_INDEX_EQUIPMENT: Int = 0
        const val TAB_INDEX_FACILITY: Int = 1
    }

    override lateinit var viewbinding: FragmentReservationEditBinding
    override val viewmodel: ReservationEditViewModel by viewModels()

    private lateinit var reservationEditViewPagerAdapter : ReservationEditViewPagerAdapter

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentReservationEditBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        viewbinding.backBtn.setOnClickListener { findNavController().navigate(R.id.action_reservationEditFragment_pop) }
        initViewPager()
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {

    }

    override fun initViewFinal(savedInstanceState: Bundle?) {

    }
    private fun initViewPager() {
        viewbinding.run {
            reservationEditViewPagerAdapter = ReservationEditViewPagerAdapter(requireActivity())
            viewbinding.editViewpager.adapter = reservationEditViewPagerAdapter
            viewbinding.editTab.addOnTabSelectedListener(object :
                TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when(tab?.position) {
                        TAB_INDEX_EQUIPMENT -> viewbinding.editViewpager.setCurrentItem(TAB_INDEX_EQUIPMENT, true)
                        TAB_INDEX_FACILITY -> viewbinding.editViewpager.setCurrentItem(TAB_INDEX_FACILITY, true)
                        else -> throw IllegalStateException("에러가 발생했습니다. 다시 시도해주세요.")
                    }
                }
                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })

        }
    }

}