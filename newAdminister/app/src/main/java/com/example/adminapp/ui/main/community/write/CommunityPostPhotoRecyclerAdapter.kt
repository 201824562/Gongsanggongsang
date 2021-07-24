package com.example.adminapp.ui.main.community.write

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.adminapp.databinding.FragmentCommunityAttachImageItemBinding



class CommunityPostPhotoRecyclerAdapter(val attachPhotoItems:ArrayList<Uri>): RecyclerView.Adapter<CommunityPostPhotoRecyclerAdapter.CommunityPostPhotoViewHolder>() {

    override fun getItemCount(): Int {
        return attachPhotoItems.size;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityPostPhotoViewHolder {
        val viewbinding = FragmentCommunityAttachImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  CommunityPostPhotoViewHolder(viewbinding, parent)
    }

    override fun onBindViewHolder(holder: CommunityPostPhotoViewHolder, position: Int) {
        holder.bind(attachPhotoItems[position])
    }


    class CommunityPostPhotoViewHolder(viewbinding: FragmentCommunityAttachImageItemBinding, itemview: ViewGroup) : RecyclerView.ViewHolder(viewbinding.root) {
        val binding = viewbinding
        fun bind(it : Uri) {
            Glide.with(itemView).load(it).
            apply(RequestOptions.overrideOf(300, 300))
                .apply(RequestOptions.centerCropTransform())
                .into(binding.attachImage)
        }

    }


}