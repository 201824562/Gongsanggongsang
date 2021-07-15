package com.example.userapp.ui.signup

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.net.toUri
import androidx.paging.PagedListAdapter
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


    interface OnItemClickListener { fun onItemClick(v: View, position: Int) }
    inner class ViewHolder(val binding: ItemSignupSearchListBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(ItemSignupSearchListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let{ agencyItem ->
            if (holder is ViewHolder) {
                holder.binding.itemCheckBox.isChecked = agencyItem.clicked
                holder.binding.listItemAgencyName.text = agencyItem.name
                holder.binding.listItemAgencyLocation.text = agencyItem.location
                holder.binding.listItemAgencyLayout.setOnClickListener {
                    if (holder.binding.itemCheckBox.isChecked){
                        agencyItem.clicked = false
                        lastChecked = null
                        lastCheckedPos = null
                        holder.binding.itemCheckBox.isChecked = false
                    }
                    else {
                        if (lastChecked != null){
                            lastCheckedPos?.let { pos -> getItem(pos).clicked = false}
                            lastChecked!!.isChecked = false
                            lastChecked = null
                            lastCheckedPos = null }
                        agencyItem.clicked = true
                        lastChecked = holder.binding.itemCheckBox
                        lastCheckedPos = position
                        holder.binding.itemCheckBox.isChecked = true
                    }
                    listener.onItemClick(holder.itemView, position)
                }
            }
        }
    }

}
