package com.example.adminapp.ui.main.reservation.add

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.adminapp.R
import com.example.adminapp.data.model.CategoryData
import com.example.adminapp.data.model.CategoryItem
import com.example.adminapp.data.model.RecyclerDataType
import com.example.adminapp.databinding.ItemReservationAddCategoryBinding

class ReservationAddRVAdapter (val  listener : OnItemClickListener): ListAdapter<CategoryItem, ReservationAddRVAdapter.ViewHolder>(
    AddressDiffCallback
) {

    companion object {
        val AddressDiffCallback = object : DiffUtil.ItemCallback<CategoryItem>() {
            override fun areItemsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
                return (oldItem.data?.iconID== newItem.data?.iconID) && (oldItem.data?.name == newItem.data?.name)
            }
            override fun areContentsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface OnItemClickListener { fun onItemClick( position: Int, categoryData : CategoryData?, askDelete : Boolean) }

    private var lastSelected : ConstraintLayout? = null
    private var lastSelectedPos : Int ?= null

    fun clearSelectedVariables(){
        lastSelected = null
        lastSelectedPos = null
    }

    inner class ViewHolder(val binding: ItemReservationAddCategoryBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemReservationAddCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { item ->
            if (item.type == RecyclerDataType.NONE) {
                setAddView(holder.binding)
                holder.binding.itemAddView.setOnClickListener { listener.onItemClick(position, null, false) }
            }
            else {
                setDataView(holder.binding, item.data!!)
                holder.binding.itemAddView.setOnClickListener {
                    when (item.data.clicked){
                        true -> {
                            lastSelected = null
                            lastSelectedPos = null
                            item.data.clicked = false
                            it.isSelected = false
                            listener.onItemClick(position,null, false)
                        }
                        false ->{
                            if (lastSelected != null){
                                getItem(lastSelectedPos!!).data!!.clicked = false
                                lastSelected!!.isSelected = false}
                            lastSelected = holder.binding.itemAddView
                            lastSelectedPos = position
                            item.data.clicked = true
                            it.isSelected = true
                            listener.onItemClick(position, item.data, false)
                        }
                    }
                }
                holder.binding.itemAddView.setOnLongClickListener {
                    listener.onItemClick(position, item.data, true)
                    return@setOnLongClickListener true
                }
            }

        }

    }

    private fun setAddView(binding: ItemReservationAddCategoryBinding) {
        binding.run { itemAddImage.load(R.drawable.ic_add) }
    }

    private fun setDataView(binding: ItemReservationAddCategoryBinding, data : CategoryData) {
        binding.run {
            itemAddImage.load(data.iconID)
            itemAddName.text = data.name
        }
    }


}
