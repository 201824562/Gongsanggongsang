package com.example.userapp.ui.main.community.preview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.userapp.data.model.PostDataInfo
import com.example.userapp.databinding.FragmentCommunityPreviewMarketItemBinding
import com.example.userapp.ui.main.community.CommunityViewModel


class CommunityPreviewMarketRecyclerAdapter(var postDataList : ArrayList<PostDataInfo>): RecyclerView.Adapter<CommunityPreviewMarketRecyclerAdapter.CommunityPreviewMarketViewHolder>() {
    val viewmodel : CommunityViewModel = CommunityViewModel()
    interface OnCommunityMarketItemClickListener{
        fun onPreviewItemClick(position: Int)
    }
    var listener: OnCommunityMarketItemClickListener? = null

    override fun getItemCount(): Int {
        return postDataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityPreviewMarketViewHolder {
        val viewbinding = FragmentCommunityPreviewMarketItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  CommunityPreviewMarketViewHolder(viewbinding, parent, listener)
    }

    override fun onBindViewHolder(holder: CommunityPreviewMarketViewHolder, position: Int) {
        holder.bind(postDataList[position])

    }

    fun getItem(position: Int): PostDataInfo {
        return postDataList[position]
    }

    inner class CommunityPreviewMarketViewHolder(viewbinding: FragmentCommunityPreviewMarketItemBinding, itemview: ViewGroup, listener: OnCommunityMarketItemClickListener?) : RecyclerView.ViewHolder(viewbinding.root) {
        val binding = viewbinding

        fun bind(postDataInfo: PostDataInfo) {
            if(postDataInfo.post_photo_uri?.size != 0){
                Glide.with(binding.communityMarketPreviewThumbnail).load(postDataInfo.post_photo_uri?.get(0))
            }
            binding.communityMarketPreviewTitle.text = postDataInfo.post_title
            binding.communityMarketPreviewPrice.text = postDataInfo.post_state
            binding.communityMarketPreviewContent.text = postDataInfo.post_contents
            binding.communityMarketPreviewTime.text = postDataInfo.post_date
            binding.communityMarketPreviewCommentNumber.text = postDataInfo.post_comments.size.toString()

        }
        init {
            itemView.setOnClickListener(){
                listener?.onPreviewItemClick(bindingAdapterPosition)
                return@setOnClickListener
            }
        }
    }
}