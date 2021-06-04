package com.example.userapp.ui.mainhome.community

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.userapp.R
import com.example.userapp.data.entity.PostCommentDataClass
import com.example.userapp.databinding.FragmentCommunityPreviewItemBinding
import com.example.userapp.databinding.FragmentPostCommentItemBinding


class CommunityCommentRecyclerAdapter(val communityCommentItems:ArrayList<PostCommentDataClass>): RecyclerView.Adapter<CommunityCommentRecyclerAdapter.CommunityCommentViewHolder>() {

    override fun getItemCount(): Int {
        return communityCommentItems.size;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityCommentViewHolder {
        val viewbinding = FragmentPostCommentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  CommunityCommentViewHolder(viewbinding, parent)
    }

    override fun onBindViewHolder(holder: CommunityCommentViewHolder, position: Int) {
        holder.bind(communityCommentItems[position])
    }

    fun getItem(position: Int): PostCommentDataClass {
        return communityCommentItems[position]
    }

    class CommunityCommentViewHolder(viewbinding: FragmentPostCommentItemBinding, itemview: ViewGroup) : RecyclerView.ViewHolder(viewbinding.root) {
        val binding = viewbinding

        fun bind(it : PostCommentDataClass) {
            binding.communityCommentName.text = it.name
            binding.communityCommentDate.text = it.date
            binding.communityCommentPost.text = it.contents

        }

    }

}