package com.parasol.adminapp.ui.main.reservation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.parasol.adminapp.R
import com.parasol.adminapp.ui.base.BaseSessionFragment
import com.parasol.adminapp.data.model.ReservationData
import com.parasol.adminapp.data.model.ReservationFacilityBundle
import com.parasol.adminapp.data.model.ReservationItem
import com.parasol.adminapp.data.model.ReservationType
import com.parasol.adminapp.databinding.FragmentReservationDetailFacilityBinding


class ReservationDetailFacilityFragment() : BaseSessionFragment<FragmentReservationDetailFacilityBinding, ReservationDetailFacilityViewModel>() {

    companion object{
        const val TAB_INDEX_BASIC: Int = 0
        const val TAB_INDEX_LOG: Int = 1
    }

    override lateinit var viewbinding: FragmentReservationDetailFacilityBinding
    override val viewmodel: ReservationDetailFacilityViewModel by viewModels()
    private val args : ReservationDetailFacilityFragmentArgs by navArgs()
    private lateinit var facilityBundleData : ReservationFacilityBundle
    private var reservationDetailFacilityViewPagerAdapter : ReservationDetailFacilityViewPagerAdapter? = null

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewbinding = FragmentReservationDetailFacilityBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        viewbinding.backBtn.setOnClickListener {  findNavController().navigate(R.id.action_reservationDetailFacilityFragment_pop)  }
        when(args.facilityBundle){
            null -> makeErrorEvent()
            else -> facilityBundleData = args.facilityBundle!! }
        initViewPager()
    }

    override fun initDataBinding(savedInstanceState: Bundle?) { viewbinding.toolbarText.text = facilityBundleData.name
        viewmodel.getReservationFacilitySettingData(facilityBundleData.name)
            .observe(viewLifecycleOwner) {
                if (it.boolean) facilityBundleData.settingData = it.facilitySettingData
                else findNavController().navigate(R.id.action_reservationDetailFacilityFragment_pop)
            }
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.run {
            reservationDetailSettingBtn.setOnClickListener {
                facilityBundleData.settingData?.let {
                    findNavController().navigate(ReservationDetailFacilityFragmentDirections
                        .actionReservationDetailFacilityFragmentToReservationEditDetailFragment(
                            ReservationItem(
                                ReservationType.FACILITY, ReservationData(it.icon,
                                    it.name, it.intervalTime, it.maxTime), it.unableTimeList)))
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        reservationDetailFacilityViewPagerAdapter?.deleteFragments()
        reservationDetailFacilityViewPagerAdapter = null
    }

    private fun makeErrorEvent(){
        showToast("알수 없는 에러가 발생했습니다. 다시 시도해주세요.")
        findNavController().navigate(R.id.action_reservationDetailEquipmentFragment_pop)
    }

    private fun initViewPager() {
        viewbinding.run {
            reservationDetailFacilityViewPagerAdapter = ReservationDetailFacilityViewPagerAdapter(requireActivity(), facilityBundleData)
            reservationLogViewpager.apply {
                adapter = reservationDetailFacilityViewPagerAdapter
                isUserInputEnabled = false }
            reservationDetailBasicBtn.isSelected = true
            reservationDetailBasicBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_87))
            reservationDetailBasicBtn.setOnClickListener {
                makeButtonsUnselected()
                it.isSelected = true
                reservationDetailBasicBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_87))
                reservationLogViewpager.setCurrentItem(TAB_INDEX_BASIC, false)
                reservationDetailBasicView.visibility = View.VISIBLE
            }
            reservationDetailLogBtn.setOnClickListener {
                makeButtonsUnselected()
                it.isSelected = true
                reservationDetailLogBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_87))
                reservationLogViewpager.setCurrentItem(TAB_INDEX_LOG, false)
                reservationDetailLogView.visibility = View.VISIBLE
            }
        }
    }

    private fun makeButtonsUnselected(){
        viewbinding.run {
            reservationDetailBasicBtn.apply {
                isSelected = false
                setTextColor(ContextCompat.getColor(context, R.color.black_20)) }
            reservationDetailBasicView.visibility = View.INVISIBLE
            reservationDetailLogBtn.apply {
                isSelected = false
                setTextColor(ContextCompat.getColor(context, R.color.black_20)) }
            reservationDetailLogView.visibility = View.INVISIBLE
        }
    }

}

class ReservationDetailFacilityViewPagerAdapter (activity: FragmentActivity, private val facilityInfo : ReservationFacilityBundle) : FragmentStateAdapter(activity) {

    private var firstFragment : Fragment = ReservationDetailFacilityBasicFragment()
    private var secondFragment : Fragment = ReservationDetailFacilityLogFragment()

    fun deleteFragments() {
        firstFragment.onDestroyView()
        secondFragment.onDestroyView() }

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val bundle = Bundle()
        bundle.putParcelable("facilityItemInfo", facilityInfo)
        return when (position) {
            0 -> { firstFragment.arguments = bundle
                firstFragment }
            1 -> {
                secondFragment.arguments = bundle
                secondFragment }
            else -> error("no such position: $position")
        }
    }
}