package com.example.userapp.ui.mainhome.reservation

import android.os.Bundle
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
    val onClickNoUsingIcon: (ReservationUseEquipment: ReservationUseEquipment) -> Unit
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

        viewHolder.viewbinding.document.text = data.document_name
        viewHolder.viewbinding.timeGap.text = ChronoUnit.MINUTES.between(
            LocalDateTime.now(),
            LocalDateTime.parse(data.end_time)
        ).toString() + "분 남음"
        viewHolder.viewbinding.noUseBtn.setOnClickListener() {
            onClickNoUsingIcon.invoke(data)
        }
    }

    fun setData(newData: List<ReservationUseEquipment>) {
        dataSet = newData
        notifyDataSetChanged()
    }

    override fun getItemCount() = dataSet.size
}