package com.example.gongsanggongsang.Second

import android.app.ActionBar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gongsanggongsang.R
import com.example.gongsanggongsang.databinding.FragmentCommunalEquipmentBinding
import com.example.gongsanggongsang.databinding.FragmentCommunalEquipmentItemBinding
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firestore.v1.DocumentTransform
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import java.time.LocalDateTime

class CommunalEquipmentFragment : Fragment() {
    val database = FirebaseFirestore.getInstance()
    private val viewModel: CommunalViewModel by viewModels()
    private lateinit var binding: FragmentCommunalEquipmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCommunalEquipmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.get_equipmentdata()

        binding.equipmentRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.equipmentRecyclerView.adapter = EquipmentAdapter(
            emptyList(),
            onClickUsingIcon = {
                //bottom sheet dialog
                val communalEquipmentDialogFragment = CommunalEquipmentDialogFragment()
                communalEquipmentDialogFragment.show(requireActivity().supportFragmentManager,"aaaaa")

                viewModel.add_use(it)
//                binding.equipmentRecyclerView.adapter?.notifyDataSetChanged()
            }
        )
        //관찰 -> ui 업데이트
        viewModel.LiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            (binding.equipmentRecyclerView.adapter as EquipmentAdapter).setData(it)
        })
    }
}

data class EquipmentDataClass(
    val document_name: String,
    var using: String,
    var usage_time: Int
)

class EquipmentAdapter(
    private var dataSet: List<EquipmentDataClass>,
    val onClickUsingIcon: (EquipmentDataClass: EquipmentDataClass) -> Unit
) :
    RecyclerView.Adapter<EquipmentAdapter.EquipmentViewHolder>() {

    class EquipmentViewHolder(val binding: FragmentCommunalEquipmentItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): EquipmentViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.fragment_communal_equipment_item, viewGroup, false)
        return EquipmentViewHolder(FragmentCommunalEquipmentItemBinding.bind(view))
    }

    override fun onBindViewHolder(viewHolder: EquipmentViewHolder, position: Int) {
        // item뷰 지지고 볶기
        val ind = dataSet[position]
        viewHolder.binding.document.text = dataSet[position].document_name
        viewHolder.binding.usingStatus.text = dataSet[position].using
        //사용중일때 사용하기 버튼 없애기
        if (dataSet[position].using != "no_using") {
            viewHolder.binding.usingStatus.text = "using"
            viewHolder.binding.useBtn.setVisibility(View.GONE)
        } else {
            viewHolder.binding.usingStatus.text = "no_using"
            viewHolder.binding.useBtn.setVisibility(View.VISIBLE)
        }
        //사용하기 버튼
        viewHolder.binding.useBtn.setOnClickListener() {
            onClickUsingIcon.invoke(dataSet[position])
        }
    }

    //라이브데이터 값이 변경되었을 때 필요한 메소 - 데이터갱신
    fun setData(newData: List<EquipmentDataClass>){
        dataSet = newData
        notifyDataSetChanged()
    }
    override fun getItemCount() = dataSet.size
}