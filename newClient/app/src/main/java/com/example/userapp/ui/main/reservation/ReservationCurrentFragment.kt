package com.example.userapp.ui.main.reservation

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.userapp.R
import com.example.userapp.base.BaseFragment
import com.example.userapp.data.model.ReservationReserveFacility
import com.example.userapp.data.model.ReservationUseEquipment
import com.example.userapp.databinding.FragmentMainhomeReservationCurrentBinding
import com.example.userapp.databinding.FragmentMainhomeReservationCurrentReserveItemBinding
import com.example.userapp.databinding.FragmentMainhomeReservationCurrentUsingItemBinding
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.concurrent.timer

class ReservationCurrentFragment :
    BaseFragment<FragmentMainhomeReservationCurrentBinding, ReservationViewModel>() {
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

    override fun initViewStart(savedInstanceState: Bundle?) {
        viewbinding.equipmentUsingRecyclerView.layoutManager = LinearLayoutManager(context)
        viewbinding.equipmentUsingRecyclerView.adapter = EquipmentUsingAdapter(
            emptyList(),
            onClickNoUsingIcon = {
                viewmodel.end_use(it)
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
        viewmodel.UseEquipmentLiveData.observe(viewLifecycleOwner, {
            (viewbinding.equipmentUsingRecyclerView.adapter as EquipmentUsingAdapter).setData(it)
        })
        viewmodel.FacilityReserveLiveData.observe(viewLifecycleOwner, {
            (viewbinding.facilityReserveRecyclerView.adapter as FacilityReserveAdapter).setData(it)
        })
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewmodel.getUseEquipmentData()
        //viewmodel.getReserveFacilityData()
    }
}


class EquipmentUsingAdapter(
    private var  dataSet: List<ReservationUseEquipment>,
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

    override fun onBindViewHolder(viewHolder: EquipmentUsingViewHolder, position: Int) {
        val data = dataSet[position]

        data.remain_time = (ChronoUnit.MILLIS.between(
            LocalDateTime.now(),
            LocalDateTime.parse(data.end_time)
        ))
        println("remaintime : " + data.remain_time + "\n")

        viewHolder.viewbinding.document.text = data.document_name
        viewHolder.viewbinding.noUseBtn.setOnClickListener() {
            onClickNoUsingIcon.invoke(data)
        }
        //남은시간이 음수일경우 사용종료와 같은 동작을 수행
        if(data.remain_time < 0){
            onClickNoUsingIcon.invoke(data)
        }

        Log.d("viewholderPosition",position.toString())
        //dataset에 코루틴이 존재하여 돌아갈 경우 취소하고. 다른 코루틴을 생성한다 << dataset의 코루틴이 중복되는것을 막아줌

        //남은시간을 처리할 코루틴
        data.coroutine.async {
            while(data.remain_time > 0){
                viewHolder.viewbinding.remainTimeTextview.setText((data.remain_time / 60000).toString() + "분 남음")
                data.remain_time -= 1000
                delay(1000)
                Log.e("checking position",position.toString())
                Log.e("checking time","${data.remain_time}")
            }
            onClickNoUsingIcon.invoke(data)
        }
    }

    //데이터셋 변화 >> 뷰로 적용시켜주는 함수
    fun setData(newData: List<ReservationUseEquipment>) {
        dataSet = newData
        //notifyDataSetChanged()
    }

    override fun getItemCount() = dataSet.size
}

class FacilityReserveAdapter(
    private var  dataSet: List<ReservationReserveFacility>,
    val onClickNoUsingIcon: (ReservationReserveFacility: ReservationReserveFacility) -> Unit,
) :
    RecyclerView.Adapter<FacilityReserveAdapter.FacilityReserveViewHolder>() {

    class FacilityReserveViewHolder(val viewbinding: FragmentMainhomeReservationCurrentReserveItemBinding) :
        RecyclerView.ViewHolder(viewbinding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): FacilityReserveViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.fragment_mainhome_reservation_current_reserve_item, viewGroup, false)
        return FacilityReserveViewHolder(FragmentMainhomeReservationCurrentReserveItemBinding.bind(view))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: FacilityReserveViewHolder, position: Int) {
        val data = dataSet[position]

        viewHolder.viewbinding.documentNameTextview.text = data.document_name
        viewHolder.viewbinding.reserveTimeTextview.text = "00월 00일" +
                data.timeSlotList.first().chunked(2)[0] + ":" + data.timeSlotList.first().chunked(2)[1] +
                "~" + data.timeSlotList.last().chunked(2)[0] + ":" + data.timeSlotList.last().chunked(2)[1]

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

class FacilityReserveAdapter(
    private var  dataSet: List<ReservationReserveFacility>,
    val onClickNoUsingIcon: (ReservationReserveFacility: ReservationReserveFacility) -> Unit,
) :
    RecyclerView.Adapter<FacilityReserveAdapter.FacilityReserveViewHolder>() {

    class FacilityReserveViewHolder(val viewbinding: FragmentMainhomeReservationCurrentReserveItemBinding) :
        RecyclerView.ViewHolder(viewbinding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): FacilityReserveViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.fragment_mainhome_reservation_current_reserve_item, viewGroup, false)
        return FacilityReserveViewHolder(FragmentMainhomeReservationCurrentReserveItemBinding.bind(view))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: FacilityReserveViewHolder, position: Int) {
        val data = dataSet[position]

        viewHolder.viewbinding.documentNameTextview.text = data.document_name
        viewHolder.viewbinding.reserveTimeTextview.text = "00월 00일" +
                data.timeSlotList.first().chunked(2)[0] + ":" + data.timeSlotList.first().chunked(2)[1] +
                "~" + data.timeSlotList.last().chunked(2)[0] + ":" + data.timeSlotList.last().chunked(2)[1]

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
