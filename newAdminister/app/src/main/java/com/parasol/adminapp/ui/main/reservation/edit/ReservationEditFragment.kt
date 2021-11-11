package com.parasol.adminapp.ui.main.reservation.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.parasol.adminapp.R
import com.parasol.adminapp.ui.base.BaseSessionFragment
import com.parasol.adminapp.databinding.FragmentReservationEditBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ReservationEditFragment : BaseSessionFragment<FragmentReservationEditBinding, ReservationEditViewModel>() {

    companion object{
        const val TAB_INDEX_EQUIPMENT: Int = 0
        const val TAB_INDEX_FACILITY: Int = 1 }

    override lateinit var viewbinding: FragmentReservationEditBinding
    override val viewmodel: ReservationEditViewModel by viewModels()
    private var reservationEditViewPagerAdapter : ReservationEditViewPagerAdapter? = null

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentReservationEditBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {   initViewPager()
        viewbinding.backBtn.setOnClickListener { findNavController().navigate(R.id.action_reservationEditFragment_pop) } }

    override fun initDataBinding(savedInstanceState: Bundle?) {
/*        viewmodel.selectedTabPositionData.observe(viewLifecycleOwner){
            when(it){
                0 -> viewbinding.editViewpager.setCurrentItem(TAB_INDEX_EQUIPMENT, true)
                1 -> viewbinding.editViewpager.setCurrentItem(TAB_INDEX_FACILITY, true)
                else -> throw IllegalStateException("에러가 발생했습니다. 다시 시도해주세요.")
            }
        }*/
    }

    override fun initViewFinal(savedInstanceState: Bundle?) { }

    override fun onDestroy() {
        super.onDestroy()
        reservationEditViewPagerAdapter?.deleteFragments()
        reservationEditViewPagerAdapter = null
    }

    private fun initViewPager() {
        viewbinding.run {
            reservationEditViewPagerAdapter = ReservationEditViewPagerAdapter(requireActivity())
            viewbinding.editViewpager.apply {
                adapter = reservationEditViewPagerAdapter
            }
            editTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) { viewbinding.editViewpager.currentItem = tab!!.position }
                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) { viewbinding.editViewpager.currentItem = tab!!.position } })
            //if(viewmodel.selectedTabPosition!=null) viewbinding.editTab.getTabAt(viewmodel.selectedTabPosition!!)?.select()
            TabLayoutMediator(editTab, editViewpager){ tab, position ->
                val tabTextList = arrayListOf("바로 사용", "예약 사용")
                tab.text = tabTextList[position] }.attach()

        }
    }

}