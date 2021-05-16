package com.example.gongsanggongsang.Second

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gongsanggongsang.R
import com.example.gongsanggongsang.databinding.FireItemPracticeBinding
import com.example.gongsanggongsang.databinding.FirebaseExItemBinding
import com.google.firebase.firestore.FirebaseFirestore


class ReservationDryerFragment : Fragment() {
    val database = FirebaseFirestore.getInstance()
    private val data = arrayListOf<machine_state>()
    private lateinit var binding: FireItemPracticeBinding
    // This property is only valid between onCreateView and
    // onDestroyView.

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FireItemPracticeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var name = "default"
        var using = "default"
        var waiting = "default"

        database.collection("CATE2")
                .addSnapshotListener { value, e ->
                    if (e != null) {
                        return@addSnapshotListener
                    }
                    data.clear()
                    for (document in value!!) {
                        name = document.id
                        using = document.getString("using") ?: " "
                        waiting = document.getString("waiting") ?: " "
                        data.add(machine_state(name,using,waiting))
                    }
                    binding.recyclerView.adapter?.notifyDataSetChanged()
                }

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = CateAdapter(data,
                onClickUsingIcon = {
                    modify_using_status(it)
                },
                onClickNoUsingIcon = {
                    modify_using_status(it)
                },
                onClickWaitingIcon = {
                    modify_wait(it)
                }
        )
    }

    // 클릭시 사용 함수
    private fun modify_using_status(machineState: machine_state){
        var map= mutableMapOf<String,Any>()
        if(machineState.using == "using"){
            map["waiting"] = "0"
            database.collection("CATE2").document(machineState.name).update(map)
            map["using"] = "no_using"
        }else{
            map["using"] = "using"
        }
        database.collection("CATE2").document(machineState.name).update(map)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun modify_wait(machineState: machine_state) {
        if (machineState.waiting.toInt() >= 0) {
            var map = mutableMapOf<String, Any>()
            map["waiting"] = (machineState.waiting.toInt() + 1).toString()
            database.collection("CATE2").document(machineState.name).update(map)
        }
    }
}

data class machine_state(val name:String, var using:String, var waiting:String)

class CateAdapter(
        private val dataSet: ArrayList<machine_state>,
        val onClickUsingIcon: (machineState: machine_state) -> Unit,
        val onClickNoUsingIcon: (machineState: machine_state) -> Unit,
        val onClickWaitingIcon: (machineState: machine_state) -> Unit
) :
        RecyclerView.Adapter<CateAdapter.CateViewHolder>() {

    class CateViewHolder(val binding: FirebaseExItemBinding ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CateViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.firebase_ex_item, viewGroup, false)
        return CateViewHolder(FirebaseExItemBinding.bind(view))
    }

    override fun onBindViewHolder(viewHolder: CateViewHolder, position: Int) {
        // item뷰 지지고 볶기
        val ind = dataSet[position]
        viewHolder.binding.document.text = dataSet[position].name
        viewHolder.binding.usingText.text = dataSet[position].using
        viewHolder.binding.waitingText.text = dataSet[position].waiting
        viewHolder.binding.useBtn.setOnClickListener() {
            if (dataSet[position].using == "no_using") {
                onClickUsingIcon.invoke(dataSet[position])
            }
        }
        viewHolder.binding.noUseBtn.setOnClickListener() {
            if (dataSet[position].using == "using") {
                onClickNoUsingIcon.invoke(dataSet[position])
            }
        }
        viewHolder.binding.waitBtn.setOnClickListener() {
            if (dataSet[position].using == "using") {
                onClickWaitingIcon.invoke(dataSet[position])
            }
        }
    }
    override fun getItemCount() = dataSet.size

}
