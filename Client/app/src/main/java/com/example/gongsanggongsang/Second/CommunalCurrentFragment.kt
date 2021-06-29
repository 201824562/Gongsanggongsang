package com.example.gongsanggongsang.Second

import android.app.ActionBar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gongsanggongsang.R
import com.example.gongsanggongsang.databinding.FragmentCommunalCurrentBinding
import com.example.gongsanggongsang.databinding.FragmentCommunalEquipmentUsingItemBinding
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firestore.v1.DocumentTransform
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class CommunalCurrentFragment : Fragment() {
    val database = FirebaseFirestore.getInstance()
    val viewModel: CommunalViewModel by viewModels()
    private lateinit var binding: FragmentCommunalCurrentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCommunalCurrentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.get_UsingEquipmentdata()

        binding.equipmentUsingRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.equipmentUsingRecyclerView.adapter = EquipmentUsingAdapter(
            emptyList(),
            onClickNoUsingIcon = {
                viewModel.end_use(it)
            }
        )

        viewModel.usingLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            (binding.equipmentUsingRecyclerView.adapter as EquipmentUsingAdapter).setData(it)
        })
    }
}

data class UsingDataClass(
    val document_name: String,
    var name: String,
    var start_time: String,
    var end_time: String
)

class EquipmentUsingAdapter(
    private var dataSet: List<UsingDataClass>,
    val onClickNoUsingIcon: (UsingDataClass: UsingDataClass) -> Unit
) :
    RecyclerView.Adapter<EquipmentUsingAdapter.EquipmentUsingViewHolder>() {

    class EquipmentUsingViewHolder(val binding: FragmentCommunalEquipmentUsingItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): EquipmentUsingViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.fragment_communal_equipment_using_item, viewGroup, false)
        return EquipmentUsingViewHolder(FragmentCommunalEquipmentUsingItemBinding.bind(view))
    }

    override fun onBindViewHolder(viewHolder: EquipmentUsingViewHolder, position: Int) {
        // item뷰 지지고 볶기
        val ind = dataSet[position]
        //문자열로 적고 파싱하는걸로 다시해보자!

        viewHolder.binding.document.text = dataSet[position].document_name
        viewHolder.binding.timeGap.text = ChronoUnit.MINUTES.between(LocalDateTime.now(),LocalDateTime.parse(ind.end_time)).toString() + "분 남음"
        viewHolder.binding.noUseBtn.setOnClickListener() {
                onClickNoUsingIcon.invoke(dataSet[position])
        }
    }

    fun setData(newData: List<UsingDataClass>){
        dataSet = newData
        notifyDataSetChanged()
    }

    override fun getItemCount() = dataSet.size
}