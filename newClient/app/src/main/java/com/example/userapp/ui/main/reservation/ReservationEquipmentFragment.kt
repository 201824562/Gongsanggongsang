package com.example.userapp.ui.main.reservation

import android.app.ProgressDialog.show
import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.userapp.R
import com.example.userapp.base.BaseFragment
import com.example.userapp.data.model.ReservationEquipment
import com.example.userapp.databinding.FragmentMainhomeReservationEquipmentBinding
import com.example.userapp.databinding.FragmentMainhomeReservationEquipmentItemBinding
import com.example.userapp.utils.ConfirmUsingDialog
import com.example.userapp.utils.FinishUsingDialog
import com.example.userapp.utils.InputUsingTimeDialog
import com.google.firebase.firestore.FirebaseFirestore


class ReservationEquipmentFragment :
    BaseFragment<FragmentMainhomeReservationEquipmentBinding, ReservationViewModel>() {
    override lateinit var viewbinding: FragmentMainhomeReservationEquipmentBinding
    override val viewmodel: ReservationViewModel by viewModels()
    val database = FirebaseFirestore.getInstance()

    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding =
            FragmentMainhomeReservationEquipmentBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        viewbinding.equipmentRecyclerView.layoutManager = LinearLayoutManager(context)
        viewbinding.equipmentRecyclerView.adapter = EquipmentAdapter(
            emptyList(),
            onClickUsingIcon = {

                val inputUsingTimeDialog = InputUsingTimeDialog(requireContext()).apply {
                    clickListener = object : InputUsingTimeDialog.DialogButtonClickListener {
                        override fun dialogCloseClickListener() {
                            dismiss()
                        }

                        override fun usingClickListener(usingtime :Int) {
                            val confirmUsingDialog = ConfirmUsingDialog(requireContext(), usingtime) //사용하는
                            confirmUsingDialog.clickListener = object : ConfirmUsingDialog.DialogButtonClickListener {
                                override fun dialogAgainClickListener() {
                                    confirmUsingDialog.dismiss()
                                }

                                override fun dialogUsingClickListener() {
                                    if (usingtime > 0) { //사용시간이 0보다 큰 경우만 사용
                                        viewmodel.add_use(it,usingtime)
                                    }
                                    confirmUsingDialog.dismiss()
                                    dismiss()
                                }
                            }
                            confirmUsingDialog.show()
                        }
                    }
                }
                inputUsingTimeDialog.show()
            }
        )
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.EquipmentLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            (viewbinding.equipmentRecyclerView.adapter as EquipmentAdapter).setData(it)
        })
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
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

    override fun onBindViewHolder(viewHolder: EquipmentViewHolder, position: Int) {
        val data = dataSet[position]
        viewHolder.viewbinding.document.text = data.document_name
        viewHolder.viewbinding.usingStatus.text = data.using
        //사용중일때 사용하기 버튼 없애기
        if (data.using != "no_using") {
            viewHolder.viewbinding.usingStatus.text = "using"
            viewHolder.viewbinding.useBtn.visibility = View.GONE
        } else {
            viewHolder.viewbinding.usingStatus.text = "no_using"
            viewHolder.viewbinding.useBtn.visibility = View.VISIBLE
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