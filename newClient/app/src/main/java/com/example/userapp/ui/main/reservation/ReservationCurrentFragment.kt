package com.example.userapp.ui.main.reservation

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.userapp.MainActivity
import com.example.userapp.R
import com.example.userapp.base.BaseSessionFragment
import com.example.userapp.data.model.ReservationReserveFacility
import com.example.userapp.data.model.ReservationUseEquipment
import com.example.userapp.databinding.FragmentMainhomeReservationCurrentBinding
import com.example.userapp.databinding.FragmentMainhomeReservationCurrentReserveItemBinding
import com.example.userapp.databinding.FragmentMainhomeReservationCurrentUsingFacilityItemBinding
import com.example.userapp.databinding.FragmentMainhomeReservationCurrentUsingItemBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ReservationCurrentFragment :
    BaseSessionFragment<FragmentMainhomeReservationCurrentBinding, ReservationViewModel>() {
    override lateinit var viewbinding: FragmentMainhomeReservationCurrentBinding
    override val viewmodel: ReservationViewModel by viewModels()
    val database = FirebaseFirestore.getInstance()

    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentMainhomeReservationCurrentBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initViewStart(savedInstanceState: Bundle?) {
        viewbinding.equipmentUsingRecyclerView.layoutManager = LinearLayoutManager(context)
        viewbinding.equipmentUsingRecyclerView.adapter = EquipmentUsingAdapter(
            emptyList(),
            onClickNoUsingIcon = {
                viewmodel.end_use(it)
                (activity as MainActivity).setUseCompleteAlarm(Calendar.getInstance(),true,it.icon)
            }
        )
        viewbinding.facilityUsingRecyclerView.layoutManager = LinearLayoutManager(context)
        viewbinding.facilityUsingRecyclerView.adapter = FacilityUsingAdapter(
            emptyList(),
            onClickNoUsingIcon = {
                viewmodel.end_use(it)
                (activity as MainActivity).setUseCompleteAlarm(Calendar.getInstance(),true,it.icon)
            }
        )
        viewbinding.facilityReserveRecyclerView.layoutManager = LinearLayoutManager(context)
        viewbinding.facilityReserveRecyclerView.adapter = FacilityReserveAdapter(
            emptyList(),
            onClickNoUsingIcon = {
                viewmodel.cancel_reserve(it)
            }
        )

    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        var useEquipmentEmpty = false
        var useFacilityEmpty = false
        viewmodel.UseEquipmentLiveData.observe(viewLifecycleOwner, {
            (viewbinding.equipmentUsingRecyclerView.adapter as EquipmentUsingAdapter).setData(it)
            if(it.isEmpty()){
                useEquipmentEmpty = true
                viewbinding.equipmentUsingRecyclerView.visibility = View.GONE
            }else{
                useEquipmentEmpty = false
                viewbinding.equipmentUsingRecyclerView.visibility = View.VISIBLE
            }

            if(useEquipmentEmpty && useFacilityEmpty){
                viewbinding.noUsingTextmessageTextview.visibility = View.VISIBLE
            }else{
                viewbinding.noUsingTextmessageTextview.visibility = View.GONE
            }
        })
        viewmodel.UseFacilityLiveData.observe(viewLifecycleOwner, {
            (viewbinding.facilityUsingRecyclerView.adapter as FacilityUsingAdapter).setData(it)
            if(it.isEmpty()){
                useFacilityEmpty = true
                viewbinding.facilityUsingRecyclerView.visibility = View.GONE
            }else{
                useEquipmentEmpty = false
                viewbinding.facilityUsingRecyclerView.visibility = View.VISIBLE
            }

            if(useEquipmentEmpty && useFacilityEmpty){
                viewbinding.noUsingTextmessageTextview.visibility = View.VISIBLE
            }else{
                viewbinding.noUsingTextmessageTextview.visibility = View.GONE
            }
        })
        viewmodel.FacilityReserveLiveData.observe(viewLifecycleOwner, {
            (viewbinding.facilityReserveRecyclerView.adapter as FacilityReserveAdapter).setData(it)
            if(it.isEmpty()){
                viewbinding.facilityReserveRecyclerView.visibility = View.GONE
                viewbinding.noReserveTextmessageTextview.visibility = View.VISIBLE
                //예약중인 내역이 없습니다 (박스)
            }else{
                viewbinding.facilityReserveRecyclerView.visibility = View.VISIBLE
                viewbinding.noReserveTextmessageTextview.visibility = View.GONE
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewmodel.getUseEquipmentData()
        viewmodel.getUseFacilityData()
        viewmodel.getReserveFacilityData()
    }
}

class EquipmentUsingAdapter(
    private var dataSet: List<ReservationUseEquipment>,
    val onClickNoUsingIcon: (ReservationUseEquipment: ReservationUseEquipment) -> Unit,
) :
    RecyclerView.Adapter<EquipmentUsingAdapter.EquipmentUsingViewHolder>() {

    class EquipmentUsingViewHolder(val viewbinding: FragmentMainhomeReservationCurrentUsingItemBinding) :
        RecyclerView.ViewHolder(viewbinding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): EquipmentUsingViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.fragment_mainhome_reservation_current_using_item, viewGroup, false)
        return EquipmentUsingViewHolder(FragmentMainhomeReservationCurrentUsingItemBinding.bind(view))
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(viewHolder: EquipmentUsingViewHolder, position: Int) {
        val data = dataSet[position]


        viewHolder.viewbinding.document.text = data.document_name
        viewHolder.viewbinding.icon.load(data.icon)

        viewHolder.viewbinding.endtimeTextview.text = LocalDateTime.parse(data.endTime).format(
            DateTimeFormatter.ofPattern("HH:mm")
        ) + " 종료"
        viewHolder.viewbinding.remainTimeTextview.text = data.remain_time.toString() + "분 남음"

        viewHolder.viewbinding.noUseBtn.setOnClickListener() {
            onClickNoUsingIcon.invoke(data)
        }
        //남은시간이 음수일경우 사용종료와 같은 동작을 수행
        if (data.remain_time < 0) {
            onClickNoUsingIcon.invoke(data)
        }

        Log.d("viewholderPosition", position.toString())
    }

    //데이터셋 변화 >> 뷰로 적용시켜주는 함수
    fun setData(newData: List<ReservationUseEquipment>) {
        dataSet = newData
        notifyDataSetChanged()
    }

    override fun getItemCount() = dataSet.size
}

class FacilityUsingAdapter(
    private var dataSet: List<ReservationUseEquipment>,
    val onClickNoUsingIcon: (ReservationUseEquipment: ReservationUseEquipment) -> Unit,
) :
    RecyclerView.Adapter<FacilityUsingAdapter.FacilityUsingViewHolder>() {

    class FacilityUsingViewHolder(val viewbinding: FragmentMainhomeReservationCurrentUsingFacilityItemBinding) :
        RecyclerView.ViewHolder(viewbinding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): FacilityUsingViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.fragment_mainhome_reservation_current_using_facility_item, viewGroup, false)
        return FacilityUsingViewHolder(FragmentMainhomeReservationCurrentUsingFacilityItemBinding.bind(view))
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(viewHolder: FacilityUsingViewHolder, position: Int) {
        val data = dataSet[position]


        viewHolder.viewbinding.document.text = data.document_name
        viewHolder.viewbinding.icon.load(data.icon)

        viewHolder.viewbinding.endtimeTextview.text = LocalDateTime.parse(data.endTime).format(
            DateTimeFormatter.ofPattern("HH:mm")
        ) + " 종료"
        viewHolder.viewbinding.remainTimeTextview.text = data.remain_time.toString() + "분 남음"

        viewHolder.viewbinding.noUseBtn.setOnClickListener() {
            onClickNoUsingIcon.invoke(data)
        }
        //남은시간이 음수일경우 사용종료와 같은 동작을 수행
        if (data.remain_time < 0) {
            onClickNoUsingIcon.invoke(data)
        }
    }

    //데이터셋 변화 >> 뷰로 적용시켜주는 함수
    fun setData(newData: List<ReservationUseEquipment>) {
        dataSet = newData
        notifyDataSetChanged()
    }

    override fun getItemCount() = dataSet.size
}

class FacilityReserveAdapter(
    private var dataSet: List<ReservationReserveFacility>,
    val onClickNoUsingIcon: (ReservationReserveFacility: ReservationReserveFacility) -> Unit,
) :
    RecyclerView.Adapter<FacilityReserveAdapter.FacilityReserveViewHolder>() {

    class FacilityReserveViewHolder(val viewbinding: FragmentMainhomeReservationCurrentReserveItemBinding) :
        RecyclerView.ViewHolder(viewbinding.root)

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): FacilityReserveViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.fragment_mainhome_reservation_current_reserve_item, viewGroup, false)
        return FacilityReserveViewHolder(
            FragmentMainhomeReservationCurrentReserveItemBinding.bind(
                view
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(viewHolder: FacilityReserveViewHolder, position: Int) {
        val data = dataSet[position]

        viewHolder.viewbinding.documentNameTextview.text = data.document_name
        viewHolder.viewbinding.icon.load(data.icon)
        //예약내역에 날짜 띄우는거 하다가 말았음

        viewHolder.viewbinding.periodTextview.text = LocalDateTime.parse(data.startTime)
            .format(DateTimeFormatter.ofPattern("MM'월' dd'일' HH:mm'~'")) + LocalDateTime.parse(data.endTime)
            .format(DateTimeFormatter.ofPattern("HH:mm"))
        viewHolder.viewbinding.cancelReserveBtn.setOnClickListener() {
            onClickNoUsingIcon.invoke(data)
        }
    }

    //데이터셋 변화 >> 뷰로 적용시켜주는 함수
    fun setData(newData: List<ReservationReserveFacility>) {
        dataSet = newData
        notifyDataSetChanged()
    }

    override fun getItemCount() = dataSet.size
}
