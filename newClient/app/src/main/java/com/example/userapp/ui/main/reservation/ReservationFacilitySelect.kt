package com.example.userapp.ui.main.reservation

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.userapp.R
import com.example.userapp.base.BaseFragment
import com.example.userapp.base.BaseSessionFragment
import com.example.userapp.data.entity.DayTimeSlot
import com.example.userapp.data.entity.ReservationTimeData
import com.example.userapp.data.model.ReservationFacilityDayInfo
import com.example.userapp.data.model.ReservationFacilityTimeSlot
import com.example.userapp.databinding.FragmentMainhomeReservationFacilitySelectBinding
import com.example.userapp.databinding.FragmentMainhomeReservationFacilitySelectItemBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.HashMap

class ReservationFacilitySelect : BaseSessionFragment<FragmentMainhomeReservationFacilitySelectBinding, ReservationViewModel>() {
    override lateinit var viewbinding: FragmentMainhomeReservationFacilitySelectBinding
    override val viewmodel: ReservationViewModel by viewModels()
    val database = FirebaseFirestore.getInstance()
    val args: ReservationFacilitySelectArgs by navArgs()

    override fun initViewbinding(
        inflater: LayoutInflater,
         container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentMainhomeReservationFacilitySelectBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        viewbinding.facilityTimesliceRecyclerView.layoutManager = LinearLayoutManager(context).also{
            it.orientation = LinearLayoutManager.HORIZONTAL
        }
        viewbinding.facilityTimesliceRecyclerView.adapter = FacilitySelectAdapter(
            emptyList(),
            onClickUsingIcon = {
                if(it.buttonSelected as Boolean){
                    if(viewmodel.delete_select_time_slot(it)){
                        it.buttonSelected = false
                    }else{
                        Toast.makeText(context, "붙어있는 시간만 선택할 수 있습니다.\n따라서, 취소가 불가능합니다.", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    if( viewmodel.add_select_time_slot(it,args.myArg.max_time/args.myArg.interval_time)){
                        it.buttonSelected = true
                    }else{
                        if(viewmodel.getDayTimeSlotListSize() == args.myArg.max_time/args.myArg.interval_time){
                            Toast.makeText(context, "최대 예약가능한 시간을 초과하였습니다.", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(context, "붙어있는 시간만 선택할 수 있습니다.\n따라서, 선택이 불가능합니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                viewbinding.nextBtn.isEnabled = viewmodel.getDayTimeSlotListSize() != 0L
                viewmodel
            }
        )
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.FacilityDayInfoLiveData.observe(viewLifecycleOwner, {
            (viewbinding.facilityTimesliceRecyclerView.adapter as FacilitySelectAdapter).setData(it.day_time_slot_list)
        })
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.documentNameTextview.text = args.myArg.document_name + " 예약"
        viewbinding.message2Textview.text = "최대 예약 가능한 시간:" + args.myArg.max_time.toString()+"분"

        var cal :Calendar = Calendar.getInstance()
        val dateFmt :SimpleDateFormat = SimpleDateFormat("dd")

        cal.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY)
        viewbinding.daySunTextview.text = dateFmt.format(cal.time)
        cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY)
        viewbinding.dayMonTextview.text = dateFmt.format(cal.time)
        cal.set(Calendar.DAY_OF_WEEK,Calendar.TUESDAY)
        viewbinding.dayTueTextview.text = dateFmt.format(cal.time)
        cal.set(Calendar.DAY_OF_WEEK,Calendar.WEDNESDAY)
        viewbinding.dayWedTextview.text = dateFmt.format(cal.time)
        cal.set(Calendar.DAY_OF_WEEK,Calendar.THURSDAY)
        viewbinding.dayThuTextview.text = dateFmt.format(cal.time)
        cal.set(Calendar.DAY_OF_WEEK,Calendar.FRIDAY)
        viewbinding.dayFriTextview.text = dateFmt.format(cal.time)
        cal.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY)
        viewbinding.daySatTextview.text = dateFmt.format(cal.time)


        viewbinding.monView.setOnClickListener(View.OnClickListener {
            viewmodel.getFacilityTimeSlotData(args.myArg.document_name,"monday")
            viewbinding.dayInfo.visibility = View.VISIBLE
            button_not_selected()
            viewbinding.monView.isSelected = true
            viewmodel.clear_select_time_slot()
        })
        viewbinding.tueView.setOnClickListener(View.OnClickListener {
            viewmodel.getFacilityTimeSlotData(args.myArg.document_name,"tuesday")
            viewbinding.dayInfo.visibility = View.VISIBLE
            button_not_selected()
            viewbinding.tueView.isSelected = true
            viewmodel.clear_select_time_slot()
        })
        viewbinding.wedView.setOnClickListener(View.OnClickListener {
            viewmodel.getFacilityTimeSlotData(args.myArg.document_name,"wednesday")
            viewbinding.dayInfo.visibility = View.VISIBLE
            button_not_selected()
            viewbinding.wedView.isSelected = true
            viewmodel.clear_select_time_slot()
        })
        viewbinding.thuView.setOnClickListener(View.OnClickListener {
            viewmodel.getFacilityTimeSlotData(args.myArg.document_name,"thursday")
            viewbinding.dayInfo.visibility = View.VISIBLE
            button_not_selected()
            viewbinding.thuView.isSelected = true
            viewmodel.clear_select_time_slot()
        })
        viewbinding.friView.setOnClickListener(View.OnClickListener {
            viewmodel.getFacilityTimeSlotData(args.myArg.document_name,"friday")
            viewbinding.dayInfo.visibility = View.VISIBLE
            button_not_selected()
            viewbinding.friView.isSelected = true
            viewmodel.clear_select_time_slot()
        })
        viewbinding.satView.setOnClickListener(View.OnClickListener {
            viewmodel.getFacilityTimeSlotData(args.myArg.document_name,"saturday")
            viewbinding.dayInfo.visibility = View.VISIBLE
            button_not_selected()
            viewbinding.satView.isSelected = true
            viewmodel.clear_select_time_slot()
        })
        viewbinding.sunView.setOnClickListener(View.OnClickListener {
            viewmodel.getFacilityTimeSlotData(args.myArg.document_name,"sunday")
            viewbinding.dayInfo.visibility = View.VISIBLE
            button_not_selected()
            viewbinding.sunView.isSelected = true
            viewmodel.clear_select_time_slot()
        })
        viewbinding.nextBtn.setOnClickListener {
            viewmodel.add_reserve()
            Toast.makeText(context, "예약이 완료되었습니다!!", Toast.LENGTH_SHORT).show()
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
}

class FacilitySelectAdapter(
    private var dataSet: List<DayTimeSlot>,
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
        viewHolder.viewbinding.timeSlotBtn.text = data.data?.hour.toString() + ":" + data.data?.min.toString()

        Log.e("data", dataSet[position].toString())
    //버튼 상태 그리기
        viewHolder.viewbinding.timeSlotBtn.isSelected = data.user.toString() != "Nope"
        viewHolder.viewbinding.timeSlotBtn.isEnabled = data.user.toString() == "Nope"
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

    override fun getItemCount() = dataSet.size
}