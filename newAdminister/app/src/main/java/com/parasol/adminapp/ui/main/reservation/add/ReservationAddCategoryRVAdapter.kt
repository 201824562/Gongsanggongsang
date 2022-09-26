package com.parasol.adminapp.ui.main.reservation.add

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.parasol.adminapp.data.model.CategoryResourceItem
import com.parasol.adminapp.databinding.ItemReservationAddImageBinding

class ReservationAddCategoryRVAdapter (val  listener : OnItemClickListener): ListAdapter<CategoryResourceItem, ReservationAddCategoryRVAdapter.ViewHolder>(
    AddressDiffCallback
) {


    companion object {
        val AddressDiffCallback = object : DiffUtil.ItemCallback<CategoryResourceItem>() {
            override fun areItemsTheSame(oldItem: CategoryResourceItem, newItem: CategoryResourceItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: CategoryResourceItem, newItem: CategoryResourceItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface OnItemClickListener { fun onItemClick(resourceID : Int?) }

    private var lastSelected : LinearLayout? = null
    private var lastSelectedPos : Int ?= null

    inner class ViewHolder(val binding: ItemReservationAddImageBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemReservationAddImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let{ item ->
            holder.binding.itemAddImage.load(item.drawableID)
            holder.binding.itemAddView.setOnClickListener {
                when (item.clicked){
                    true -> {
                        lastSelected = null
                        lastSelectedPos = null
                        item.clicked = false
                        it.isSelected = false
                        listener.onItemClick(null)
                    }
                    false ->{
                        if (lastSelected != null){
                            getItem(lastSelectedPos!!).clicked = false
                            lastSelected!!.isSelected = false}
                        lastSelected = holder.binding.itemAddView
                        lastSelectedPos = position
                        item.clicked = true
                        it.isSelected = true
                        listener.onItemClick(item.drawableID)
                    }
                }
            }
        }
    }

}
