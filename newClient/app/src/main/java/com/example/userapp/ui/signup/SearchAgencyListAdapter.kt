package com.example.userapp.ui.signup

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.userapp.base.BaseViewModel
import com.example.userapp.data.model.Agency
import com.example.userapp.databinding.ItemSignupSearchListBinding

class SearchAgencyListAdapter (val  listener : OnItemClickListener): ListAdapter<Agency, RecyclerView.ViewHolder>(AddressDiffCallback) {

    companion object {
        val AddressDiffCallback = object : DiffUtil.ItemCallback<Agency>() {
            override fun areItemsTheSame(oldItem: Agency, newItem: Agency): Boolean {
                return (oldItem.name == newItem.name) && (oldItem.location == newItem.location)
            }
            override fun areContentsTheSame(oldItem: Agency, newItem: Agency): Boolean {
                return oldItem == newItem
            }
        }
    }

    private var lastChecked : CheckBox ?= null
    private var lastCheckedPos : Int ?= null

    fun clearCheckedVariables(){
        lastChecked = null
        lastCheckedPos = null
    }
    fun setCheckedVariables(checkBox: CheckBox, lastCheckBoxPosition : Int){
        lastChecked = checkBox
        lastCheckedPos = lastCheckBoxPosition
    }

    interface OnItemClickListener { fun onItemClick(v: View, position: Int, agencyInfo : Agency?,
                                                    checkBox: CheckBox?, checkPos : Int?) }
    inner class ViewHolder(val binding: ItemSignupSearchListBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(ItemSignupSearchListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let{ item ->
            if (holder is ViewHolder) {
                if (item.clicked){
                    lastCheckedPos = position
                    lastChecked = holder.binding.itemCheckBox
                    holder.binding.itemCheckBox.isChecked = item.clicked }
                holder.binding.listItemAgencyName.text = item.name
                holder.binding.listItemAgencyLocation.text = item.location
                holder.binding.listItemAgencyLayout.setOnClickListener {
                    when (lastCheckedPos){
                        null -> {
                            lastChecked = holder.binding.itemCheckBox
                            lastCheckedPos = position
                            item.clicked = true
                            holder.binding.itemCheckBox.isChecked = true
                            listener.onItemClick(holder.itemView, position, item, lastChecked, lastCheckedPos) }
                        position -> {
                            lastChecked = null
                            lastCheckedPos = null
                            item.clicked = false
                            holder.binding.itemCheckBox.isChecked = false
                            listener.onItemClick(holder.itemView, position, null, null, null) }
                        else -> {
                            getItem(lastCheckedPos!!).clicked = false
                            lastChecked!!.isChecked = false
                            lastChecked = holder.binding.itemCheckBox
                            lastCheckedPos = position
                            item.clicked = true
                            holder.binding.itemCheckBox.isChecked = true
                            listener.onItemClick(holder.itemView, position, item, lastChecked, lastCheckedPos)
                        }
                    }
                }
            }
        }
    }

}
