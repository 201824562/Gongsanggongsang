package com.example.userapp.ui.main.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.userapp.R
import com.example.userapp.databinding.FragmentSettingsOutReserveBinding
import com.example.userapp.ui.main.reservation.ReservationFacilitySelectArgs
import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.userapp.MainActivity
import com.example.userapp.ui.base.BaseSessionFragment
import com.example.userapp.data.dto.UserModel
import com.example.userapp.data.entity.DayTimeSlot
import com.example.userapp.data.model.ReservationFacility
import com.example.userapp.databinding.FragmentSettingsOutReserveItemBinding
import com.example.userapp.restartActivity
import com.example.userapp.ui.base.sessionRestart
import com.example.userapp.utils.CautionMessageDialog
import com.example.userapp.utils.ConfirmReserveDialog
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.util.*

class SettingsOutReserveFragment : BaseSessionFragment<FragmentSettingsOutReserveBinding, SettingsViewModel>() {
    override lateinit var viewbinding: FragmentSettingsOutReserveBinding
    override val viewmodel: SettingsViewModel by viewModels()
    val database = FirebaseFirestore.getInstance()
    val args: ReservationFacilitySelectArgs by navArgs()
    private lateinit var unableRVlinearLayoutManager : LinearLayoutManager
    private var ac: MainActivity? = null
    private var userInfo : UserModel? = null
    var weekday : Int = 1
    private var defaultCal : Calendar = Calendar.getInstance()
    private var cal : Calendar = Calendar.getInstance()

    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentSettingsOutReserveBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        unableRVlinearLayoutManager = LinearLayoutManager(context)
        unableRVlinearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        viewbinding.facilityTimesliceRecyclerView.layoutManager = unableRVlinearLayoutManager
        viewbinding.facilityTimesliceRecyclerView.adapter = OutReserveAdapter(
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
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.onSuccessGettingUserInfo.observe(this, { userInfo = it })
        viewmodel.onSuccessGettingNullUserInfo.observe(this, { restartActivity() })
        viewmodel.FacilityDayInfoLiveData.observe(viewLifecycleOwner, {
            (viewbinding.facilityTimesliceRecyclerView.adapter as OutReserveAdapter).setData(it.day_time_slot_list)
        })
        (viewbinding.facilityTimesliceRecyclerView.adapter as OutReserveAdapter).setweekdayData(weekday)
        viewmodel.InitRefTimeLiveData.observe(this, {
            cal = it.clone() as Calendar
            defaultCal = it.clone() as Calendar
            setWeekBtn()
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewmodel.getUserInfo()
        viewmodel.getinitRefTimeData()

        setWeekBtn()

        viewbinding.monView.setOnClickListener(View.OnClickListener {
            viewmodel.getFacilityTimeSlotData(args.myArg.document_name,"monday",args.myArg.category_icon,args.myArg.interval_time)
            viewbinding.dayInfo.visibility = View.VISIBLE
            button_not_selected()
            viewbinding.monView.isSelected = true
            weekday = 2
            (viewbinding.facilityTimesliceRecyclerView.adapter as OutReserveAdapter).setweekdayData(weekday)
            viewmodel.clear_select_time_slot()
        })
        viewbinding.tueView.setOnClickListener(View.OnClickListener {
            viewmodel.getFacilityTimeSlotData(args.myArg.document_name,"tuesday",args.myArg.category_icon,args.myArg.interval_time)
            viewbinding.dayInfo.visibility = View.VISIBLE
            button_not_selected()
            viewbinding.tueView.isSelected = true
            weekday = 3
            (viewbinding.facilityTimesliceRecyclerView.adapter as OutReserveAdapter).setweekdayData(weekday)
            viewmodel.clear_select_time_slot()
        })
        viewbinding.wedView.setOnClickListener(View.OnClickListener {
            viewmodel.getFacilityTimeSlotData(args.myArg.document_name,"wednesday",args.myArg.category_icon,args.myArg.interval_time)
            viewbinding.dayInfo.visibility = View.VISIBLE
            button_not_selected()
            viewbinding.wedView.isSelected = true
            weekday = 4
            (viewbinding.facilityTimesliceRecyclerView.adapter as OutReserveAdapter).setweekdayData(weekday)
            viewmodel.clear_select_time_slot()
        })
        viewbinding.thuView.setOnClickListener(View.OnClickListener {
            viewmodel.getFacilityTimeSlotData(args.myArg.document_name,"thursday",args.myArg.category_icon,args.myArg.interval_time)
            viewbinding.dayInfo.visibility = View.VISIBLE
            button_not_selected()
            viewbinding.thuView.isSelected = true
            weekday = 5
            (viewbinding.facilityTimesliceRecyclerView.adapter as OutReserveAdapter).setweekdayData(weekday)
            viewmodel.clear_select_time_slot()
        })
        viewbinding.friView.setOnClickListener(View.OnClickListener {
            viewmodel.getFacilityTimeSlotData(args.myArg.document_name,"friday",args.myArg.category_icon,args.myArg.interval_time)
            viewbinding.dayInfo.visibility = View.VISIBLE
            button_not_selected()
            viewbinding.friView.isSelected = true
            weekday = 6
            (viewbinding.facilityTimesliceRecyclerView.adapter as OutReserveAdapter).setweekdayData(weekday)
            viewmodel.clear_select_time_slot()
        })
        viewbinding.satView.setOnClickListener(View.OnClickListener {
            viewmodel.getFacilityTimeSlotData(args.myArg.document_name,"saturday",args.myArg.category_icon,args.myArg.interval_time)
            viewbinding.dayInfo.visibility = View.VISIBLE
            button_not_selected()
            viewbinding.satView.isSelected = true
            weekday = 7
            (viewbinding.facilityTimesliceRecyclerView.adapter as OutReserveAdapter).setweekdayData(weekday)
            viewmodel.clear_select_time_slot()
        })
        viewbinding.sunView.setOnClickListener(View.OnClickListener {
            viewmodel.getFacilityTimeSlotData(args.myArg.document_name,"sunday",args.myArg.category_icon,args.myArg.interval_time)
            viewbinding.dayInfo.visibility = View.VISIBLE
            button_not_selected()
            viewbinding.sunView.isSelected = true
            weekday = 1
            (viewbinding.facilityTimesliceRecyclerView.adapter as OutReserveAdapter).setweekdayData(weekday)
            viewmodel.clear_select_time_slot()
        })

        viewbinding.nextBtn.setOnClickListener {//주용이한테 넘겨줄 코드
            var tmp = viewmodel.create_deliveryOutReserveData()
            findNavController().navigate(SettingsOutReserveFragmentDirections.actionSettingsOutReserveFragmentToSettingsOutWriteFragment(tmp))

//            val confirmUsingDialog = ConfirmReserveDialog(requireContext(), args.myArg, viewmodel.getReserveFacilityStartTime(), viewmodel.getReserveFacilityEndTime()) //사용하는
//            confirmUsingDialog.clickListener = object : ConfirmReserveDialog.DialogButtonClickListener {
//                override fun dialogAgainClickListener() {
//                    confirmUsingDialog.dismiss()
//                }
//
//                override fun dialogReserveClickListener() {
////                    var endTimeCal = Calendar.getInstance()
////                    var startTimeCal = Calendar.getInstance()
////                    var calPair = Pair(startTimeCal,endTimeCal)
//                    var tmp = viewmodel.create_deliveryOutReserveData()
//
//                    val sdf = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS", Locale.KOREA)
//                    //(activity as MainActivity).setUseCompleteAlarm(calPair.second,false,(args.myArg.document_name+sdf.format(calPair.second.getTime()).toString()).hashCode())
//                    confirmUsingDialog.dismiss()
//                }
//            }
//            showDialog(confirmUsingDialog, viewLifecycleOwner)
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
    fun setWeekBtn(){
        val dateFmt :SimpleDateFormat = SimpleDateFormat("dd")
        val nowCal : Calendar = Calendar.getInstance()
        nowCal.set(Calendar.HOUR,0)
        nowCal.set(Calendar.MINUTE,0)
        nowCal.set(Calendar.SECOND,0)

        weekEnableInit()

        if(cal.get(Calendar.DAY_OF_WEEK) != 1) cal.add(Calendar.DATE, 7)

        cal.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY)
        viewbinding.daySunTextview.text = dateFmt.format(cal.time)
        if(nowCal.compareTo(cal) == 1) {
            viewbinding.sunView.isEnabled = false
            viewbinding.daySunTextview.isEnabled = false
        }
        cal.add(Calendar.DATE, 1)

        if(cal.get(Calendar.DAY_OF_WEEK) == defaultCal.get(Calendar.DAY_OF_WEEK)) cal.add(Calendar.DATE,-7)
        viewbinding.dayMonTextview.text = dateFmt.format(cal.time)
        if(nowCal.compareTo(cal) == 1) {
            viewbinding.monView.isEnabled = false
            viewbinding.dayMonTextview.isEnabled = false
        }
        cal.add(Calendar.DATE, 1)

        if(cal.get(Calendar.DAY_OF_WEEK) == defaultCal.get(Calendar.DAY_OF_WEEK)) cal.add(Calendar.DATE,-7)
        viewbinding.dayTueTextview.text = dateFmt.format(cal.time)
        if(nowCal.compareTo(cal) == 1) {
            viewbinding.tueView.isEnabled = false
            viewbinding.dayTueTextview.isEnabled = false
        }
        cal.add(Calendar.DATE, 1)

        if(cal.get(Calendar.DAY_OF_WEEK) == defaultCal.get(Calendar.DAY_OF_WEEK)) cal.add(Calendar.DATE,-7)
        viewbinding.dayWedTextview.text = dateFmt.format(cal.time)
        if(nowCal.compareTo(cal) == 1) {
            viewbinding.wedView.isEnabled = false
            viewbinding.dayWedTextview.isEnabled = false
        }
        cal.add(Calendar.DATE, 1)

        if(cal.get(Calendar.DAY_OF_WEEK) == defaultCal.get(Calendar.DAY_OF_WEEK)) cal.add(Calendar.DATE,-7)
        viewbinding.dayThuTextview.text = dateFmt.format(cal.time)
        if(nowCal.compareTo(cal) == 1) {
            viewbinding.thuView.isEnabled = false
            viewbinding.dayThuTextview.isEnabled = false
        }
        cal.add(Calendar.DATE, 1)

        if(cal.get(Calendar.DAY_OF_WEEK) == defaultCal.get(Calendar.DAY_OF_WEEK)) cal.add(Calendar.DATE,-7)
        viewbinding.dayFriTextview.text = dateFmt.format(cal.time)
        if(nowCal.compareTo(cal) == 1) {
            viewbinding.friView.isEnabled = false
            viewbinding.dayFriTextview.isEnabled = false
        }
        cal.add(Calendar.DATE, 1)

        if(cal.get(Calendar.DAY_OF_WEEK) == defaultCal.get(Calendar.DAY_OF_WEEK)) cal.add(Calendar.DATE,-7)
        viewbinding.daySatTextview.text = dateFmt.format(cal.time)
        if(nowCal.compareTo(cal) == 1) {
            viewbinding.satView.isEnabled = false
            viewbinding.daySatTextview.isEnabled = false
        }
    }
    fun weekEnableInit() {
        viewbinding.sunView.isEnabled = true
        viewbinding.daySunTextview.isEnabled = true
        viewbinding.monView.isEnabled = true
        viewbinding.dayMonTextview.isEnabled = true
        viewbinding.tueView.isEnabled = true
        viewbinding.dayTueTextview.isEnabled = true
        viewbinding.wedView.isEnabled = true
        viewbinding.dayWedTextview.isEnabled = true
        viewbinding.thuView.isEnabled = true
        viewbinding.dayThuTextview.isEnabled = true
        viewbinding.friView.isEnabled = true
        viewbinding.dayFriTextview.isEnabled = true
        viewbinding.satView.isEnabled = true
        viewbinding.daySatTextview.isEnabled = true
    }
}

class OutReserveAdapter(
    private var dataSet: List<DayTimeSlot>,
    var weekday: Int,
    val onClickUsingIcon: ( DayTimeSlot) -> Unit
) :
    RecyclerView.Adapter<OutReserveAdapter.OutReserveViewHolder>() {

    class OutReserveViewHolder(val viewbinding: FragmentSettingsOutReserveItemBinding) :
        RecyclerView.ViewHolder(viewbinding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): OutReserveViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.fragment_settings_out_reserve_item, viewGroup, false)
        return OutReserveViewHolder(FragmentSettingsOutReserveItemBinding.bind(view))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: OutReserveViewHolder, position: Int) {
        val data = dataSet[position]
        Log.e("data", data.data.toString())
        Log.e("data position", position.toString())
        viewHolder.viewbinding.timeSlotBtn.text =
            String.format("%02d", data.data?.hour) + ":" + String.format("%02d", data.data?.min)

        Log.e("data", dataSet[position].toString())
        //버튼 상태 그리기
        viewHolder.viewbinding.timeSlotBtn.isSelected = data.user.toString() != "Nope"
        viewHolder.viewbinding.timeSlotBtn.isEnabled = data.user.toString() == "Nope"

        if (weekday == Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
            if ((data.data?.hour?.toInt())?.times(60)
                    ?.plus((data.data?.min?.toInt()!!))!! < Calendar.getInstance()
                    .get(Calendar.AM_PM) * 12 * 60 + Calendar.getInstance()
                    .get(Calendar.HOUR) * 60 + Calendar.getInstance().get(Calendar.MINUTE)
            ) {
                viewHolder.viewbinding.timeSlotBtn.isEnabled = false
            }
        }

        //사용하기 버튼
        viewHolder.viewbinding.timeSlotBtn.setOnClickListener() {
            onClickUsingIcon.invoke(data)
            //select 효과 처리
            if (!viewHolder.viewbinding.timeSlotBtn.isSelected) {
                if (data.buttonSelected) viewHolder.viewbinding.timeSlotBtn.isSelected = true

            } else {
                if (!data.buttonSelected) viewHolder.viewbinding.timeSlotBtn.isSelected = false
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

