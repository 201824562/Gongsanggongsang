package com.example.userapp.ui.main.community

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.userapp.databinding.FragmentCommunityAttachImageItemBinding



class CommunityAttachPhotoRecyclerAdapter(val attachPhotoItems:ArrayList<String>): RecyclerView.Adapter<CommunityAttachPhotoRecyclerAdapter.CommunityAttachPhotoViewHolder>() {

    override fun getItemCount(): Int {
        return attachPhotoItems.size;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityAttachPhotoViewHolder {
        val viewbinding = FragmentCommunityAttachImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  CommunityAttachPhotoViewHolder(viewbinding, parent)
    }

    override fun onBindViewHolder(holder: CommunityAttachPhotoViewHolder, position: Int) {
        holder.bind(attachPhotoItems[position])
    }


    class CommunityAttachPhotoViewHolder(viewbinding: FragmentCommunityAttachImageItemBinding, itemview: ViewGroup) : RecyclerView.ViewHolder(viewbinding.root) {
        val binding = viewbinding
        fun bind(it : String) {
            Glide.with(itemView).load(it).
            apply(RequestOptions.overrideOf(300, 300))
                .apply(RequestOptions.centerCropTransform())
                .into(binding.attachImage)
        }

    }


}