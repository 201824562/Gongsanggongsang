package com.parasol.adminapp.ui.main.community.write

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.parasol.adminapp.databinding.FragmentCommunityPhotoItemBinding


class CommunityPhotoRecyclerAdapter(val attachPhotoItems:ArrayList<String>): RecyclerView.Adapter<CommunityPhotoRecyclerAdapter.CommunityAttachPhotoViewHolder>() {

    override fun getItemCount(): Int {
        return attachPhotoItems.size;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityAttachPhotoViewHolder {
        val viewbinding = FragmentCommunityPhotoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  CommunityAttachPhotoViewHolder(viewbinding, parent)
    }

    override fun onBindViewHolder(holder: CommunityAttachPhotoViewHolder, position: Int) {
        holder.bind(attachPhotoItems[position])
    }

    class CommunityAttachPhotoViewHolder(viewbinding: FragmentCommunityPhotoItemBinding, itemview: ViewGroup) : RecyclerView.ViewHolder(viewbinding.root) {
        val binding = viewbinding
        fun bind(it : String) {
            Glide.with(itemView).load(it).into(binding.attachImage2)
        }

    }


}