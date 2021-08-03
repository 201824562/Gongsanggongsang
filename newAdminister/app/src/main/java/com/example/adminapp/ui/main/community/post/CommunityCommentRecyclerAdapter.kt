package com.example.adminapp.ui.main.community.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adminapp.data.repository.PostCommentDataClass
import com.example.adminapp.databinding.FragmentCommunityCommentItemBinding


class CommunityCommentRecyclerAdapter(val communityCommentItems:ArrayList<PostCommentDataClass>): RecyclerView.Adapter<CommunityCommentRecyclerAdapter.CommunityCommentViewHolder>() {

    override fun getItemCount(): Int {
        return communityCommentItems.size;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityCommentViewHolder {
        val viewbinding = FragmentCommunityCommentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  CommunityCommentViewHolder(viewbinding, parent)
    }

    override fun onBindViewHolder(holder: CommunityCommentViewHolder, position: Int) {
        holder.bind(communityCommentItems[position])
    }

    fun getItem(position: Int): PostCommentDataClass {
        return communityCommentItems[position]
    }

    class CommunityCommentViewHolder(viewbinding: FragmentCommunityCommentItemBinding, itemview: ViewGroup) : RecyclerView.ViewHolder(viewbinding.root) {
        val binding = viewbinding

        fun bind(it : PostCommentDataClass) {
            binding.communityCommentName.text = it.name
            binding.communityCommentDate.text = it.date
            binding.communityCommentPost.text = it.contents

        }

    }

}