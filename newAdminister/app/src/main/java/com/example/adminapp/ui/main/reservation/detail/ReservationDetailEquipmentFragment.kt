package com.example.adminapp.ui.main.reservation.detail

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.adminapp.R
import com.example.adminapp.base.BaseSessionFragment
import com.example.adminapp.data.model.*
import com.example.adminapp.databinding.FragmentReservationDetailEquipmentBinding
import com.example.adminapp.ui.main.reservation.calculateDurationWithCurrent
import com.example.adminapp.ui.main.reservation.getHourMinuteString
import com.example.adminapp.utils.WrapedDialogBasicTwoButton

class ReservationDetailEquipmentFragment() : BaseSessionFragment<FragmentReservationDetailEquipmentBinding, ReservationDetailEquipmentViewModel>() {

    companion object{
        const val USING_STATE : String = "사용중"
        const val CAN_USE_STATE : String = "사용가능"
        const val CANT_USING_STATE : String = "사용불가"
        const val NO_USING_STRING : String = "-"
        const val NO_USING_BLANK_STRING : String = ""
    }

    override lateinit var viewbinding : FragmentReservationDetailEquipmentBinding
    override val viewmodel : ReservationDetailEquipmentViewModel by viewModels()
    private val args : ReservationDetailEquipmentFragmentArgs by navArgs()
    private lateinit var equipmentData : ReservationEquipmentData
    private lateinit var equipmentSettingData : ReservationEquipmentSettingData
    private lateinit var reservationDetailLogRVAdapter : ReservationDetailLogRVAdapter
    private var timer : CountDownTimer ?= null

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewbinding = FragmentReservationDetailEquipmentBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        viewbinding.backBtn.setOnClickListener {  findNavController().navigate(R.id.action_reservationDetailEquipmentFragment_pop)  }
        when(args.equipmentData){
            null -> makeErrorEvent()
            else -> equipmentData = args.equipmentData!!
        }
        when (args.equipmentSettingData){
            null -> makeErrorEvent()
            else -> equipmentSettingData = args.equipmentSettingData!! }
        setRecyclerView()
        setItemData(equipmentData)
        setItemSettingData(equipmentSettingData)
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.run {
            getReservationEquipmentData(equipmentData.name).observe(viewLifecycleOwner){
                if (!it.boolean){ findNavController().navigate(R.id.action_reservationDetailEquipmentFragment_pop) }
                else{
                    equipmentData = it.equipmentData!!
                    setItemData(equipmentData)
                    viewmodel.getReservationEquipmentSettingData(equipmentData.name) }
            }
            onSuccessGettingReserveEquipmentSettingData.observe(viewLifecycleOwner) {
                equipmentSettingData = it
                setItemSettingData(equipmentSettingData)
            }

            getReservationEquipmentLogData("바로 사용", equipmentData.name).observe(viewLifecycleOwner){
                if (it.isEmpty()) showEmptyView()
                else showRV( it.map {logData ->  ReservationLogItem(ReservationType.EQUIPMENT, logData, null)})
            }
        }
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.run {
            reservationDetailSettingBtn.setOnClickListener {
                findNavController().navigate(ReservationDetailEquipmentFragmentDirections
                    .actionReservationDetailEquipmentFragmentToReservationEditDetailFragment(
                        ReservationItem(ReservationType.EQUIPMENT, ReservationData(equipmentSettingData.icon,
                            equipmentSettingData.name, maxTime = equipmentSettingData.maxTime), listOf())
                    ))
            }
            reserveDetailItemStopBtn.setOnClickListener {
                if (!equipmentData.usable) viewmodel.startReservationEquipment(equipmentData.name)
                else if (equipmentData.using) showStopWarningDialog()
                else viewmodel.stopReservationEquipment(equipmentData)
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        try { timer?.cancel() }
        catch (e: Exception) { }
    }

    private fun makeErrorEvent(){
        showToast("알수 없는 에러가 발생했습니다. 다시 시도해주세요.")
        findNavController().navigate(R.id.action_reservationDetailEquipmentFragment_pop)
    }

    private fun showStopWarningDialog(){
        //TODO : 알람 처리 해주기.
        val dialog = WrapedDialogBasicTwoButton(requireContext(), "현재 사용중인 이용자가 있습니다.\n"+
                "정말 강제 종료하시겠습니까?\n", "취소", "종료하기").apply {
            clickListener = object : WrapedDialogBasicTwoButton.DialogButtonClickListener{
                override fun dialogCloseClickListener() { dismiss() }
                override fun dialogCustomClickListener() {
                    viewmodel.stopReservationEquipment(equipmentData)
                    dismiss()
                }
            }
        }
        showDialog(dialog, viewLifecycleOwner)
    }

    private fun setRecyclerView() {
        reservationDetailLogRVAdapter = ReservationDetailLogRVAdapter()
        viewbinding.reservationDetailRv.adapter = reservationDetailLogRVAdapter
    }

    private fun showEmptyView(){
        viewbinding.apply {
            reservationDetailEmptyView.visibility = View.VISIBLE
            reservationDetailRv.visibility = View.GONE
        }
    }
    private fun showRV(list : List<ReservationLogItem>){
        viewbinding.run{
            reservationDetailEmptyView.visibility  = View.GONE
            reservationDetailRv.visibility = View.VISIBLE
            reservationDetailLogRVAdapter.submitList(list)
        }
    }

    private fun setItemSettingData(item : ReservationEquipmentSettingData){ viewbinding.reserveDetailItemMaxTime.text = item.maxTime.toString() }

    private fun setItemData(item : ReservationEquipmentData){
        viewbinding.run {
            timer?.cancel()
            toolbarText.text = item.name
            reserveDetailItemIcon.load(item.icon)
            reserveDetailItemName.text = item.name
            reservationDetailRvBtn.isSelected = true
            reservationDetailRvBtn.setOnClickListener {
                it.isSelected = !it.isSelected
                when (it.isSelected){
                    true -> reservationDetailRv.visibility = View.VISIBLE
                    false -> reservationDetailRv.visibility = View.GONE
                }
            }
            if (!item.usable){
                reserveDetailItemUsableState.text = CANT_USING_STATE
                reserveDetailItemState.apply {
                    text = CANT_USING_STATE
                    setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.pinkish_orange)) }
                reserveDetailItemStopBtn.text = "사용모드 켜기"
                reserveDetailItemStopBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.applemint))
                reserveDetailItemIconBackground.background = ContextCompat.getDrawable(requireContext(), R.drawable.view_oval_gray)
                reserveDetailItemUserName.text = NO_USING_STRING
                reserveDetailItemUsedTime1.text = NO_USING_STRING
                reserveDetailItemLeftTime2.text = NO_USING_STRING
                reserveDetailItemLeftTime2.text = NO_USING_BLANK_STRING
            }
            else {
                reserveDetailItemUsableState.text = CAN_USE_STATE
                reserveDetailItemStopBtn.text = "사용모드 끄기"
                reserveDetailItemStopBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.pinkish_orange))
                if (!item.using){
                    reserveDetailItemIconBackground.background = ContextCompat.getDrawable(requireContext(), R.drawable.view_oval_gray)
                    reserveDetailItemUsedTimeText1.visibility = View.INVISIBLE
                    reserveDetailItemLeftTimeText1.visibility = View.INVISIBLE
                    reserveDetailItemLeftTimeText2.visibility = View.INVISIBLE
                    reserveDetailItemState.apply {
                        text = CAN_USE_STATE
                        setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.black_50)) }
                    reserveDetailItemUserName.text = NO_USING_STRING
                    reserveDetailItemUsedTime1.text = NO_USING_STRING
                    reserveDetailItemLeftTime1.text = NO_USING_STRING
                    reserveDetailItemLeftTime2.text = NO_USING_BLANK_STRING
                }
                else{
                    reserveDetailItemIconBackground.background = ContextCompat.getDrawable(requireContext(), R.drawable.view_oval_light_orange)
                    reserveDetailItemUsedTimeText1.visibility = View.VISIBLE
                    reserveDetailItemLeftTimeText1.visibility = View.VISIBLE
                    reserveDetailItemLeftTimeText2.visibility = View.VISIBLE
                    reserveDetailItemState.apply {
                        text = USING_STATE
                        setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_orange))
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.pinkish_orange)) }
                    reserveDetailItemUserName.text = item.user
                    timer = object : CountDownTimer(calculateDurationWithCurrent(item.endTime).toMillis(), 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            val minute = (millisUntilFinished/60000)
                            val second = (millisUntilFinished%60000)/1000
                            reserveDetailItemUsedTime1.text = calculateDurationWithCurrent(item.startTime).abs().toMinutes().toString()
                            reserveDetailItemLeftTime1.text = minute.toString()
                            reserveDetailItemLeftTime2.text = if (second<10) "0${second}" else second.toString()
                        }
                        override fun onFinish() { } }.start()
                    reserveDetailItemStartTime.text = getHourMinuteString(item.startTime)
                    reserveDetailItemEndTime.text = getHourMinuteString(item.endTime)
                }
            }
        }
    }



}
