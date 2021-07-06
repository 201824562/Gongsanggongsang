package com.example.userapp.ui.main.reservation

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.userapp.R
import com.example.userapp.base.BaseFragment
import com.example.userapp.data.model.ReservationUseEquipment
import com.example.userapp.databinding.FragmentMainhomeReservationCurrentBinding
import com.example.userapp.databinding.FragmentMainhomeReservationCurrentUsingItemBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

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
                it.coroutine.cancel()
                viewmodel.end_use(it)
            }
        )
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.UseEquipmentLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            (viewbinding.equipmentUsingRecyclerView.adapter as EquipmentUsingAdapter).setData(it)
        })
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewmodel.getUseEquipmentData()
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

    override fun onBindViewHolder(viewHolder: EquipmentUsingViewHolder, position: Int) {
        val data = dataSet[position]

        data.remain_time = (ChronoUnit.MILLIS.between(
            LocalDateTime.now(),
            LocalDateTime.parse(data.end_time)
        ))
        println("remaintime")
        println(data.remain_time)
        println("\n")


        viewHolder.viewbinding.document.text = data.document_name
        //viewHolder.viewbinding.remainTimeTextview.text = (data.remain_time/60000).toString() + "분 남음"
        viewHolder.viewbinding.noUseBtn.setOnClickListener() {
            onClickNoUsingIcon.invoke(data)
        }

        //스래드로 현재 유저가 사용중인 장비의 남은 시간을 알려줌
        data.coroutine.async() {
            for (i in (data.remain_time) downTo (1000)){
                viewHolder.viewbinding.remainTimeTextview.setText((i / 60000).toString() + "분 남음")
                delay(1000)
                Log.d("position",position.toString())
                Log.d("i",i.toString())
            }
        }
//        object : CountDownTimer(data.remain_time, 1000) {
//
//            override fun onTick(millisUntilFinished: Long) {
//                viewHolder.viewbinding.remainTimeTextview.setText((millisUntilFinished / 60000).toString() + "분 남음")
//            }
//
//            override fun onFinish() {
////                viewHolder.viewbinding.remainTimeTextview.setText("done!")
//            }
//        }.start()
    }

    //데이터셋 변화 >> 뷰로 적용시켜주는 함수
    fun setData(newData: List<ReservationUseEquipment>) {
        dataSet = newData
        notifyDataSetChanged()
    }

    override fun getItemCount() = dataSet.size
}