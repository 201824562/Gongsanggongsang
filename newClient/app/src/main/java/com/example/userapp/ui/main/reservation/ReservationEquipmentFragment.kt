package com.example.userapp.ui.main.reservation

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.userapp.MainActivity
import com.example.userapp.R
import com.example.userapp.base.BaseSessionFragment
import com.example.userapp.data.dto.UserModel
import com.example.userapp.data.model.ReservationEquipment
import com.example.userapp.databinding.FragmentMainhomeReservationEquipmentBinding
import com.example.userapp.databinding.FragmentMainhomeReservationEquipmentItemBinding
import com.example.userapp.restartActivity
import com.example.userapp.ui.main.reservation.CategoryResources.Companion.makeIconStringToDrawableID
import com.example.userapp.utils.ConfirmUsingDialog
import com.example.userapp.utils.InputUsingTimeDialog
import com.google.firebase.firestore.FirebaseFirestore
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*


class ReservationEquipmentFragment :
    BaseSessionFragment<FragmentMainhomeReservationEquipmentBinding, ReservationViewModel>() {
    override lateinit var viewbinding: FragmentMainhomeReservationEquipmentBinding
    override val viewmodel: ReservationViewModel by viewModels()
    val database = FirebaseFirestore.getInstance()
    private var userInfo : UserModel? = null
    //알람부분

    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentMainhomeReservationEquipmentBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        viewbinding.equipmentRecyclerView.layoutManager = LinearLayoutManager(context)
        viewbinding.equipmentRecyclerView.adapter = EquipmentAdapter(
            emptyList(),
            onClickUsingIcon = {
                val inputUsingTimeDialog = InputUsingTimeDialog(requireContext(),it).apply {
                    clickListener = object : InputUsingTimeDialog.DialogButtonClickListener {
                        override fun dialogCloseClickListener() {
                            dismiss()
                        }

                        @RequiresApi(Build.VERSION_CODES.O)
                        override fun usingClickListener(usingtime :Int) {
                            val confirmUsingDialog = ConfirmUsingDialog(requireContext(), usingtime, it) //사용하는
                            confirmUsingDialog.clickListener = object : ConfirmUsingDialog.DialogButtonClickListener {
                                override fun dialogAgainClickListener() {
                                    confirmUsingDialog.dismiss()
                                }

                                override fun dialogUsingClickListener() {
                                    if (usingtime > 0) { //사용시간이 0보다 큰 경우만 사용
                                        val sdf = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS", Locale.KOREA)
                                        val endTimeCal = Calendar.getInstance()
                                        var endTimeStr: String = "noStr"
                                        userInfo?.let { info ->
                                            endTimeStr = viewmodel.add_use(info,it,usingtime)
                                        }

                                        //여기에 알람매니저 구현
                                        try {
                                            val date: Date = sdf.parse(endTimeStr)
                                            endTimeCal.setTime(date)
                                        } catch (e: ParseException) {
                                            e.printStackTrace()
                                        }
                                        Log.e("equipment endTime",endTimeStr)
                                        (activity as MainActivity).setUseCompleteAlarm(endTimeCal,false,it.icon)
                                    }
                                    confirmUsingDialog.dismiss()
                                    dismiss()
                                }
                            }
                            showDialog(confirmUsingDialog, viewLifecycleOwner)
                        }
                    }
                }
                showDialog(inputUsingTimeDialog, viewLifecycleOwner)
            }
        )
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.onSuccessGettingUserInfo.observe(this, { userInfo = it })
        viewmodel.onSuccessGettingNullUserInfo.observe(this, { restartActivity() })
        viewmodel.EquipmentLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            (viewbinding.equipmentRecyclerView.adapter as EquipmentAdapter).setData(it)
        })
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewmodel.getUserInfo()
        viewmodel.getEquipmentData()
    }
}

class EquipmentAdapter(
    private var dataSet: List<ReservationEquipment>,
    val onClickUsingIcon: (ReservationEquipment: ReservationEquipment) -> Unit
) :
    RecyclerView.Adapter<EquipmentAdapter.EquipmentViewHolder>() {

    class EquipmentViewHolder(val viewbinding: FragmentMainhomeReservationEquipmentItemBinding) :
        RecyclerView.ViewHolder(viewbinding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): EquipmentViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.fragment_mainhome_reservation_equipment_item, viewGroup, false)
        return EquipmentViewHolder(FragmentMainhomeReservationEquipmentItemBinding.bind(view))
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(viewHolder: EquipmentViewHolder, position: Int) {
        val data = dataSet[position]
        Log.e("icon : ", data.icon.toString())
        viewHolder.viewbinding.document.text = data.document_name
        viewHolder.viewbinding.icon.load(data.icon)
        viewHolder.viewbinding.usingStatus.text = data.using
        //사용중일때 사용하기 버튼 없애기
        if (data.using != "no_using") {
            viewHolder.viewbinding.usingStatus.text = "사용중"
            viewHolder.viewbinding.useBtn.visibility = View.GONE
            viewHolder.viewbinding.endtimeTextview.text = LocalDateTime.parse(data.endTime).format(DateTimeFormatter.ofPattern("HH:mm")) + " 종료"
            viewHolder.viewbinding.remainTimeTextview.text = ChronoUnit.MINUTES.between(
                LocalDateTime.now(),
                LocalDateTime.parse(data.endTime)
            ).toString() + "분 남음"
            viewHolder.viewbinding.endtimeTextview.visibility = View.VISIBLE
            viewHolder.viewbinding.remainTimeTextview.visibility = View.VISIBLE

        } else {
            viewHolder.viewbinding.usingStatus.text = "사용가능"
            viewHolder.viewbinding.useBtn.visibility = View.VISIBLE
            viewHolder.viewbinding.endtimeTextview.visibility = View.GONE
            viewHolder.viewbinding.remainTimeTextview.visibility = View.GONE
        }
        //사용하기 버튼
        viewHolder.viewbinding.useBtn.setOnClickListener() {
            onClickUsingIcon.invoke(data)
        }
    }

    //라이브데이터 값이 변경되었을 때 필요한 메소 - 데이터갱신
    fun setData(newData: List<ReservationEquipment>) {
        dataSet = newData
        notifyDataSetChanged()
    }

    override fun getItemCount() = dataSet.size
}