package com.example.userapp.ui.signup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.userapp.base.BaseViewModel
import com.example.userapp.data.model.Agency
import com.example.userapp.databinding.ItemSignupSearchListBinding

class SearchAgencyListAdapter (val  listener:OnItemClickListener): PagedListAdapter<Agency, RecyclerView.ViewHolder>(AddressDiffCallback) {

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

    interface OnItemClickListener { fun onItemClick(v: View, position: Int) }
    inner class ViewHolder(val binding: ItemSignupSearchListBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(ItemSignupSearchListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let{ agencyItem ->
            if (holder is ViewHolder) {
                holder.binding.listItemAgencyName.text = agencyItem.name
                holder.binding.listItemAgencyLocation.text = agencyItem.location
                holder.binding.itemCheckBox.setOnClickListener { listener.onItemClick(holder.itemView, position)   }
                holder.binding.listItemAddressLayout.setOnClickListener { listener.onItemClick(holder.itemView, position)  }
            }
        }
    }

}
