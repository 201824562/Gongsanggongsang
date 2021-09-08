package com.example.adminapp.ui.main.reservation.detail

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.adminapp.R
import com.example.adminapp.ui.base.BaseSessionFragment
import com.example.adminapp.data.model.*
import com.example.adminapp.databinding.FragmentReservationDetailFacilityBasicBinding
import com.example.adminapp.ui.main.reservation.calculateDurationWithCurrent
import com.example.adminapp.ui.main.reservation.getHourMinuteString
import com.example.adminapp.utils.WrapedDialogBasicTwoButton

class ReservationDetailFacilityBasicFragment : BaseSessionFragment<FragmentReservationDetailFacilityBasicBinding, ReservationDetailFacilityViewModel>() {

    companion object{
        const val USING_STATE : String = "사용중"
        const val CAN_USE_STATE : String = "사용가능"
        const val CAN_RESERVE_STATE : String = "예약가능"
        const val CANT_RESERVE_STATE : String = "예약불가"
        const val NO_USING_STRING : String = "-"
        const val NO_USING_BLANK_STRING : String = ""
    }

    override lateinit var viewbinding: FragmentReservationDetailFacilityBasicBinding
    override val viewmodel: ReservationDetailFacilityViewModel by viewModels()
    private lateinit var reservationDetailLogRVAdapter : ReservationDetailLogRVAdapter
    private var observerHashMap : MutableMap<LiveData<*>, Observer<*>>?= null
    private lateinit var facilityBundleData : ReservationFacilityBundle
    private var timer : CountDownTimer ?= null

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewbinding = FragmentReservationDetailFacilityBasicBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        when {
            arguments == null -> makeErrorEvent()
            requireArguments().getParcelable<ReservationFacilityBundle>("facilityItemInfo" )== null -> makeErrorEvent()
            else -> facilityBundleData = requireArguments().getParcelable("facilityItemInfo")!!
        }
        setRecyclerView()
        setItemSettingData(facilityBundleData)
        setItemData(facilityBundleData)
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.run {
            if (observerHashMap == null) observerHashMap = mutableMapOf()
            val observer1 = Observer<ReceiverFacilitySettingData> {
                if (it.boolean) {
                    facilityBundleData.settingData = it.facilitySettingData
                    setItemSettingData(facilityBundleData) }
                /*else { findNavController().navigate(R.id.action_reservationDetailFacilityFragment_pop) }*/
                }
            getReservationFacilitySettingData(facilityBundleData.name).observeForever(observer1)
            observerHashMap = mutableMapOf(getReservationFacilitySettingData(facilityBundleData.name) to observer1)

            val observer2 = Observer<ReceiverFacilityLog> {
                if (!it.boolean){
                    facilityBundleData.using = false
                    facilityBundleData.logData = it.facilityLogData
                    setItemData(facilityBundleData) }
                else{
                    facilityBundleData.using = true
                    facilityBundleData.logData = it.facilityLogData
                    setItemData(facilityBundleData) }
                getReservationFacilitySettingData(facilityBundleData.name) }
            getReservationFacilityLogData(facilityBundleData.name).observeForever(observer2)
            observerHashMap?.put(getReservationFacilityLogData(facilityBundleData.name), observer2)

            val observer3 = Observer<List<ReservationFacilityLog>> {
                if (it.isEmpty()) showEmptyView()
                else showRV( it.map {logData ->  ReservationLogItem(ReservationType.FACILITY, null, logData)}) }
            getReservationFacilityLogDataList("예약 사용", facilityBundleData.name).observeForever(observer3)
            observerHashMap?.put(getReservationFacilityLogDataList("예약 사용", facilityBundleData.name), observer3)
        }
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.run {
            reserveDetailItemStopBtn.setOnClickListener {
                if (facilityBundleData.settingData != null){
                    if (facilityBundleData.settingData!!.usable) viewmodel.stopReservationFacility(facilityBundleData.name)
                    else viewmodel.startReservationFacility(facilityBundleData.name)
                }else {
                    if (facilityBundleData.logData!!.usable) viewmodel.stopReservationFacility(facilityBundleData.name)
                    else viewmodel.startReservationFacility(facilityBundleData.name)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        try { timer?.cancel()
            observerHashMap?.let {
                for (i in it){ (i.key).removeObserver(i.value as Observer<in Any>) } }
            //viewmodel.initDetailFacilityLiveData()
        } catch (e: Exception) { }
    }

    private fun makeErrorEvent(){
        showToast("알수 없는 에러가 발생했습니다. 다시 시도해주세요.")
        findNavController().navigate(R.id.action_reservationDetailFacilityFragment_pop)
    }

    private fun showStopWarningDialog(){
        //TODO : 알람 처리 해주기.
        val dialog = WrapedDialogBasicTwoButton(requireContext(), "현재 사용중인 이용자가 있습니다.\n"+
                "정말 사용모드를 종료하시겠습니까?\n", "취소", "종료하기").apply {
            clickListener = object : WrapedDialogBasicTwoButton.DialogButtonClickListener{
                override fun dialogCloseClickListener() { dismiss() }
                override fun dialogCustomClickListener() {
                    //viewmodel.stopReservationFacility(equipmentData)
                    dismiss()
                }
            }
        }
        showDialog(dialog, viewLifecycleOwner)
    }

    private fun setRecyclerView() {
        reservationDetailLogRVAdapter = ReservationDetailLogRVAdapter(null, viewmodel)
        viewbinding.reservationDetailRv.adapter = reservationDetailLogRVAdapter
    }

    private fun showEmptyView(){
        viewbinding.apply {
            reservationDetailEmptyView.visibility = View.VISIBLE
            reservationDetailRv.visibility = View.GONE
        }
    }
    private fun showRV(list : List<ReservationLogItem>) {
        viewbinding.run {
            reservationDetailEmptyView.visibility = View.GONE
            reservationDetailRv.visibility = View.VISIBLE
            reservationDetailLogRVAdapter.submitList(list)
        }
    }

    private fun setItemSettingData(bundleItem : ReservationFacilityBundle){
        viewbinding.run {
            if (bundleItem.using) {
                reserveDetailItemIconBackground.background = ContextCompat.getDrawable(requireContext(), R.drawable.view_oval_light_orange)
                reserveDetailItemState.apply {
                        text = USING_STATE
                        setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_orange))
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.pinkish_orange)) } }
            else {
                reserveDetailItemIconBackground.background = ContextCompat.getDrawable(requireContext(), R.drawable.view_oval_gray)
                reserveDetailItemState.apply {
                    text = CAN_USE_STATE
                    setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.black_50)) }
            }
            if (bundleItem.settingData != null){
                bundleItem.settingData?.let { item ->
                    reserveDetailItemIcon.load(item.icon)
                    reserveDetailItemMaxTime.text = item.maxTime.toString()
                    setButtonMode(item.usable) }
            }
            else {
                bundleItem.logData?.let { item ->
                    reserveDetailItemIcon.load(item.icon)
                    reserveDetailItemMaxTime.text = item.maxTime.toString()
                    setButtonMode(item.usable) }
            }
        }
    }
    private fun setButtonMode(usable : Boolean){
        viewbinding.run {
            if (usable) {
                reserveDetailItemUsableState.text = CAN_RESERVE_STATE
                reserveDetailItemStopBtn.text = "예약모드 끄기"
                reserveDetailItemStopBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.pinkish_orange)) }
            else {
                reserveDetailItemUsableState.text = CANT_RESERVE_STATE
                reserveDetailItemStopBtn.text = "예약모드 켜기"
                reserveDetailItemStopBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.applemint)) }
        }
    }

    private fun setItemData(bundleItem : ReservationFacilityBundle){
        viewbinding.run {
            timer?.cancel()
            reserveDetailItemName.text = bundleItem.name
            reservationDetailRvBtn.isSelected = true
            reservationDetailRvBtn.setOnClickListener {
                it.isSelected = !it.isSelected
                when (it.isSelected){
                    true -> reservationDetailRv.visibility = View.VISIBLE
                    false -> reservationDetailRv.visibility = View.GONE }
            }
            if (bundleItem.using){
                bundleItem.logData?.let { item ->
                    reserveDetailItemStopBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.pinkish_orange))
                    reserveDetailItemUsedTimeText1.visibility = View.VISIBLE
                    reserveDetailItemLeftTimeText1.visibility = View.VISIBLE
                    reserveDetailItemLeftTimeText2.visibility = View.VISIBLE
                    reserveDetailItemUserName.text = item.userName
                    timer = object : CountDownTimer(calculateDurationWithCurrent(item.endTime).toMillis(), 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            val minute = (millisUntilFinished/60000)
                            val second = (millisUntilFinished%60000)/1000
                            reserveDetailItemUsedTime1.text = calculateDurationWithCurrent(item.startTime).abs().toMinutes().toString()
                            reserveDetailItemLeftTime1.text = minute.toString()
                            reserveDetailItemLeftTime2.text = if (second<10) "0${second}" else second.toString()
                        }
                        override fun onFinish() { viewmodel.finishReservationFacilityLogData(item.documentId) } }.start()
                    reserveDetailItemStartTime.text = getHourMinuteString(item.startTime)
                    reserveDetailItemEndTime.text = getHourMinuteString(item.endTime) }
            }else{
                reserveDetailItemIconBackground.background = ContextCompat.getDrawable(requireContext(), R.drawable.view_oval_gray)
                reserveDetailItemUsedTimeText1.visibility = View.INVISIBLE
                reserveDetailItemLeftTimeText1.visibility = View.INVISIBLE
                reserveDetailItemLeftTimeText2.visibility = View.INVISIBLE
                reserveDetailItemUserName.text = NO_USING_STRING
                reserveDetailItemUsedTime1.text = NO_USING_STRING
                reserveDetailItemLeftTime1.text = NO_USING_STRING
                reserveDetailItemLeftTime2.text = NO_USING_BLANK_STRING
                reserveDetailItemStartTime.text = NO_USING_BLANK_STRING
                reserveDetailItemEndTime.text = NO_USING_BLANK_STRING
            }
        }
    }



}
