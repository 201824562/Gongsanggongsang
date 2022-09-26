package com.parasol.userapp.ui.main.reservation

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.parasol.userapp.MainActivity
import com.parasol.userapp.R
import com.parasol.userapp.ui.base.BaseSessionFragment
import com.parasol.userapp.data.dto.UserModel
import com.parasol.userapp.data.entity.DayTimeSlot
import com.parasol.userapp.databinding.FragmentMainhomeReservationFacilitySelectBinding
import com.parasol.userapp.databinding.FragmentMainhomeReservationFacilitySelectItemBinding
import com.parasol.userapp.restartActivity
import com.parasol.userapp.utils.CautionMessageDialog
import com.parasol.userapp.utils.ConfirmReserveDialog
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class ReservationFacilitySelect : BaseSessionFragment<FragmentMainhomeReservationFacilitySelectBinding, ReservationViewModel>() {
    override lateinit var viewbinding: FragmentMainhomeReservationFacilitySelectBinding
    override val viewmodel: ReservationViewModel by viewModels()
    val database = FirebaseFirestore.getInstance()
    val args: ReservationFacilitySelectArgs by navArgs()
    private var ac: MainActivity? = null
    private var userInfo : UserModel? = null
    private lateinit var unableRVlinearLayoutManager : LinearLayoutManager
    var weekday : Int = 1

    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentMainhomeReservationFacilitySelectBinding.inflate(inflater, container, false)
        return viewbinding.root
    }


    override fun initViewStart(savedInstanceState: Bundle?) {
        unableRVlinearLayoutManager = LinearLayoutManager(context)
        unableRVlinearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        viewbinding.facilityTimesliceRecyclerView.layoutManager = unableRVlinearLayoutManager
        viewbinding.facilityTimesliceRecyclerView.adapter = FacilitySelectAdapter(
            emptyList(),
            1,
            onClickUsingIcon = {
                if(it.buttonSelected as Boolean){
                    if(viewmodel.delete_select_time_slot(it)){
                        it.buttonSelected = false
                    }else{
                        val cautionMessageDialog1 = CautionMessageDialog(requireContext(), "붙어있는 시간만 선택할 수 있어요.")
                        cautionMessageDialog1.clickListener = object : CautionMessageDialog.DialogButtonClickListener {
                            override fun dialogConfirmClickListener() {
                                cautionMessageDialog1.dismiss()
                            }
                        }
                        showDialog(cautionMessageDialog1, viewLifecycleOwner)
                    }
                }
                else{
                    if( viewmodel.add_select_time_slot(it,args.myArg.max_time/args.myArg.interval_time)){
                        it.buttonSelected = true
                    }else{
                        if(viewmodel.getDayTimeSlotListSize() == args.myArg.max_time/args.myArg.interval_time){
                            val cautionMessageDialog2 = CautionMessageDialog(requireContext(), "최대 예약가능한 시간을 초과했어요.")
                            cautionMessageDialog2.clickListener = object : CautionMessageDialog.DialogButtonClickListener {
                                override fun dialogConfirmClickListener() {
                                    cautionMessageDialog2.dismiss()
                                }
                            }
                            showDialog(cautionMessageDialog2, viewLifecycleOwner)
                        }else{
                            val cautionMessageDialog3 = CautionMessageDialog(requireContext(), "붙어있는 시간만 선택할 수 있어요.")
                            cautionMessageDialog3.clickListener = object : CautionMessageDialog.DialogButtonClickListener {
                                override fun dialogConfirmClickListener() {
                                    cautionMessageDialog3.dismiss()
                                }
                            }
                            showDialog(cautionMessageDialog3, viewLifecycleOwner)
                        }
                    }
                }
                viewbinding.nextBtn.isEnabled = viewmodel.getDayTimeSlotListSize() != 0L
            }
        )
        setTimeTabView()
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.onSuccessGettingUserInfo.observe(this, { userInfo = it })
        viewmodel.onSuccessGettingNullUserInfo.observe(this, { restartActivity() })
        viewmodel.FacilityDayInfoLiveData.observe(viewLifecycleOwner, {
            (viewbinding.facilityTimesliceRecyclerView.adapter as FacilitySelectAdapter).setData(it.day_time_slot_list)
        })
        (viewbinding.facilityTimesliceRecyclerView.adapter as FacilitySelectAdapter).setweekdayData(weekday)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewmodel.getUserInfo()
        viewbinding.documentNameTextview.text = args.myArg.document_name + " 예약"
        viewbinding.message2Textview.text = "최대 예약 가능한 시간:" + args.myArg.max_time.toString()+"분"

        val defaultCal :Calendar = Calendar.getInstance()
        var cal :Calendar = Calendar.getInstance()
        var cal2 :Calendar = Calendar.getInstance()
        val dateFmt :SimpleDateFormat = SimpleDateFormat("dd")

        if(cal2.get(Calendar.DAY_OF_WEEK) == 1) cal.add(Calendar.DATE, -1)
        else cal2.add(Calendar.DATE, 7)

        cal2.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY)
        viewbinding.daySunTextview.text = dateFmt.format(cal2.time)

        cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY)
        viewbinding.dayMonTextview.text = dateFmt.format(cal.time)
        Log.e("compare check",Calendar.getInstance().compareTo(cal).toString())
        if(defaultCal.compareTo(cal) == 1) {
            viewbinding.monView.isEnabled = false
            viewbinding.dayMonTextview.isEnabled = false
        }

        cal.set(Calendar.DAY_OF_WEEK,Calendar.TUESDAY)
        viewbinding.dayTueTextview.text = dateFmt.format(cal.time)
        Log.e("compare check",Calendar.getInstance().compareTo(cal).toString())
        if(defaultCal.compareTo(cal) == 1) {
            viewbinding.tueView.isEnabled = false
            viewbinding.dayTueTextview.isEnabled = false
        }

        cal.set(Calendar.DAY_OF_WEEK,Calendar.WEDNESDAY)
        viewbinding.dayWedTextview.text = dateFmt.format(cal.time)
        Log.e("compare check",Calendar.getInstance().compareTo(cal).toString())

        if(defaultCal.compareTo(cal) == 1) {
            viewbinding.wedView.isEnabled = false
            viewbinding.dayWedTextview.isEnabled = false
        }

        cal.set(Calendar.DAY_OF_WEEK,Calendar.THURSDAY)
        viewbinding.dayThuTextview.text = dateFmt.format(cal.time)
        if(defaultCal.compareTo(cal) == 1) {
            viewbinding.thuView.isEnabled = false
            viewbinding.dayThuTextview.isEnabled = false
        }

        cal.set(Calendar.DAY_OF_WEEK,Calendar.FRIDAY)
        viewbinding.dayFriTextview.text = dateFmt.format(cal.time)
        if(defaultCal.compareTo(cal) == 1) {
            viewbinding.friView.isEnabled = false
            viewbinding.dayFriTextview.isEnabled = false
        }

        cal.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY)
        viewbinding.daySatTextview.text = dateFmt.format(cal.time)
        if(defaultCal.compareTo(cal) == 1) {
            viewbinding.satView.isEnabled = false
            viewbinding.daySatTextview.isEnabled = false
        }

        viewbinding.monView.setOnClickListener(View.OnClickListener {
            viewmodel.getFacilityTimeSlotData(args.myArg.document_name,"monday",args.myArg.category_icon,args.myArg.interval_time)
            viewbinding.dayInfo.visibility = View.VISIBLE
            button_not_selected()
            viewbinding.monView.isSelected = true
            weekday = 2
            (viewbinding.facilityTimesliceRecyclerView.adapter as FacilitySelectAdapter).setweekdayData(weekday)
            viewmodel.clear_select_time_slot()
        })
        viewbinding.tueView.setOnClickListener(View.OnClickListener {
            viewmodel.getFacilityTimeSlotData(args.myArg.document_name,"tuesday",args.myArg.category_icon,args.myArg.interval_time)
            viewbinding.dayInfo.visibility = View.VISIBLE
            button_not_selected()
            viewbinding.tueView.isSelected = true
            weekday = 3
            (viewbinding.facilityTimesliceRecyclerView.adapter as FacilitySelectAdapter).setweekdayData(weekday)
            viewmodel.clear_select_time_slot()
        })
        viewbinding.wedView.setOnClickListener(View.OnClickListener {
            viewmodel.getFacilityTimeSlotData(args.myArg.document_name,"wednesday",args.myArg.category_icon,args.myArg.interval_time)
            viewbinding.dayInfo.visibility = View.VISIBLE
            button_not_selected()
            viewbinding.wedView.isSelected = true
            weekday = 4
            (viewbinding.facilityTimesliceRecyclerView.adapter as FacilitySelectAdapter).setweekdayData(weekday)
            viewmodel.clear_select_time_slot()
        })
        viewbinding.thuView.setOnClickListener(View.OnClickListener {
            viewmodel.getFacilityTimeSlotData(args.myArg.document_name,"thursday",args.myArg.category_icon,args.myArg.interval_time)
            viewbinding.dayInfo.visibility = View.VISIBLE
            button_not_selected()
            viewbinding.thuView.isSelected = true
            weekday = 5
            (viewbinding.facilityTimesliceRecyclerView.adapter as FacilitySelectAdapter).setweekdayData(weekday)
            viewmodel.clear_select_time_slot()
        })
        viewbinding.friView.setOnClickListener(View.OnClickListener {
            viewmodel.getFacilityTimeSlotData(args.myArg.document_name,"friday",args.myArg.category_icon,args.myArg.interval_time)
            viewbinding.dayInfo.visibility = View.VISIBLE
            button_not_selected()
            viewbinding.friView.isSelected = true
            weekday = 6
            (viewbinding.facilityTimesliceRecyclerView.adapter as FacilitySelectAdapter).setweekdayData(weekday)
            viewmodel.clear_select_time_slot()
        })
        viewbinding.satView.setOnClickListener(View.OnClickListener {
            viewmodel.getFacilityTimeSlotData(args.myArg.document_name,"saturday",args.myArg.category_icon,args.myArg.interval_time)
            viewbinding.dayInfo.visibility = View.VISIBLE
            button_not_selected()
            viewbinding.satView.isSelected = true
            weekday = 7
            (viewbinding.facilityTimesliceRecyclerView.adapter as FacilitySelectAdapter).setweekdayData(weekday)
            viewmodel.clear_select_time_slot()
        })
        viewbinding.sunView.setOnClickListener(View.OnClickListener {
            viewmodel.getFacilityTimeSlotData(args.myArg.document_name,"sunday",args.myArg.category_icon,args.myArg.interval_time)
            viewbinding.dayInfo.visibility = View.VISIBLE
            button_not_selected()
            viewbinding.sunView.isSelected = true
            weekday = 1
            (viewbinding.facilityTimesliceRecyclerView.adapter as FacilitySelectAdapter).setweekdayData(weekday)
            viewmodel.clear_select_time_slot()
        })

        viewbinding.nextBtn.setOnClickListener {
            val confirmUsingDialog = ConfirmReserveDialog(requireContext(), args.myArg, viewmodel.getReserveFacilityStartTime(), viewmodel.getReserveFacilityEndTime()) //사용하는
            confirmUsingDialog.clickListener = object : ConfirmReserveDialog.DialogButtonClickListener {
                override fun dialogAgainClickListener() {
                    confirmUsingDialog.dismiss()
                }

                override fun dialogReserveClickListener() {
                    var endTimeCal = Calendar.getInstance()
                    var startTimeCal = Calendar.getInstance()
                    var calPair = Pair(startTimeCal,endTimeCal)

                    userInfo?.let { info ->
                        calPair = viewmodel.add_reserve(info, args.myArg)
                    }
                    val sdf = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS", Locale.KOREA)
                    (activity as MainActivity).setUseCompleteAlarm(calPair.second,false,(args.myArg.document_name+sdf.format(calPair.second.getTime()).toString()).hashCode())
                    (activity as MainActivity).setBeforeUseAlarm(calPair.first,(args.myArg.document_name+sdf.format(calPair.second.getTime()).toString()).hashCode())
                    confirmUsingDialog.dismiss()
                }
            }
            showDialog(confirmUsingDialog, viewLifecycleOwner)
            button_not_selected()
        }
    }

    fun button_not_selected(){
        viewbinding.monView.isSelected = false
        viewbinding.tueView.isSelected = false
        viewbinding.wedView.isSelected = false
        viewbinding.thuView.isSelected = false
        viewbinding.friView.isSelected = false
        viewbinding.satView.isSelected = false
        viewbinding.sunView.isSelected = false
        viewbinding.nextBtn.isEnabled = false
    }

    private fun setTimeTextView(time : Int){
        when (time){
            0 -> {
                viewbinding.timeText0.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                viewbinding.timeText6.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_50))
                viewbinding.timeText12.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_50))
                viewbinding.timeText18.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_50)) }
            6 -> {
                viewbinding.timeText6.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                viewbinding.timeText0.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_50))
                viewbinding.timeText12.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_50))
                viewbinding.timeText18.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_50)) }
            12 -> {
                viewbinding.timeText12.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                viewbinding.timeText0.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_50))
                viewbinding.timeText6.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_50))
                viewbinding.timeText18.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_50)) }
            18 -> {
                viewbinding.timeText18.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                viewbinding.timeText0.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_50))
                viewbinding.timeText6.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_50))
                viewbinding.timeText12.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_50)) }
        }
    }
    private fun setTimeTabView(){
        viewbinding.run {
            unableReserveTimeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
                val timeTabValue = when (checkedId){
                    timeText0.id -> 0
                    timeText6.id -> 6
                    timeText12.id -> 12
                    timeText18.id -> 18
                    else -> 0 }
                setTimeTextView(timeTabValue)
                scrollRVToTabTime(timeTabValue)
            }
        }
    }
    private fun scrollRVToTabTime(time : Int){
        val position = when (args.myArg.interval_time){
            30L -> {
                if (time==0) 0
                else time*2
            }
            60L -> {
                if (time==0) 0
                else time
            }
            else -> 0
        }
        unableRVlinearLayoutManager.scrollToPositionWithOffset(position, 0)
    }
}

class FacilitySelectAdapter(
    private var dataSet: List<DayTimeSlot>,
    var weekday: Int,
    val onClickUsingIcon: ( DayTimeSlot) -> Unit
) :
    RecyclerView.Adapter<FacilitySelectAdapter.FacilitySelectViewHolder>() {

    class FacilitySelectViewHolder(val viewbinding: FragmentMainhomeReservationFacilitySelectItemBinding) :
        RecyclerView.ViewHolder(viewbinding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): FacilitySelectViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.fragment_mainhome_reservation_facility_select_item, viewGroup, false)
        return FacilitySelectViewHolder(FragmentMainhomeReservationFacilitySelectItemBinding.bind(view))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: FacilitySelectViewHolder, position: Int) {
        val data = dataSet[position]
        Log.e("data", data.data.toString())
        Log.e("data position", position.toString())
        viewHolder.viewbinding.timeSlotBtn.text = String.format("%02d", data.data?.hour) + ":" + String.format("%02d", data.data?.min)

        Log.e("data", dataSet[position].toString())
        //버튼 상태 그리기
        viewHolder.viewbinding.timeSlotBtn.isSelected = data.user.toString() != "Nope"
        viewHolder.viewbinding.timeSlotBtn.isEnabled = data.user.toString() == "Nope"

        if(weekday == Calendar.getInstance().get(Calendar.DAY_OF_WEEK)){
            if((data.data?.hour?.toInt())?.times(60)?.plus((data.data?.min?.toInt()!!))!! < Calendar.getInstance().get(Calendar.AM_PM)*12*60 + Calendar.getInstance().get(Calendar.HOUR)*60 + Calendar.getInstance().get(Calendar.MINUTE)){
                viewHolder.viewbinding.timeSlotBtn.isEnabled = false
            }
        }

        //사용하기 버튼
        viewHolder.viewbinding.timeSlotBtn.setOnClickListener() {
            onClickUsingIcon.invoke(data)
            //select 효과 처리
            if (!viewHolder.viewbinding.timeSlotBtn.isSelected){
                if(data.buttonSelected) viewHolder.viewbinding.timeSlotBtn.isSelected = true

            }else{
                if(!data.buttonSelected) viewHolder.viewbinding.timeSlotBtn.isSelected = false
            }
        }
    }

    //라이브데이터 값이 변경되었을 때 필요한 메소 - 데이터갱신
    fun setData(newData: List<DayTimeSlot>) {
        dataSet = newData
        notifyDataSetChanged()
    }

    fun setweekdayData(newWeekday: Int) {
        weekday = newWeekday
        notifyDataSetChanged()
    }

    override fun getItemCount() = dataSet.size
}
