package com.example.userapp.ui.main.community.preview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.userapp.data.model.PostDataInfo
import com.example.userapp.databinding.FragmentCommunityPreviewItemBinding
import com.example.userapp.ui.main.community.CommunityViewModel


class CommunityPreviewRecyclerAdapter(var postDataList : ArrayList<PostDataInfo>): RecyclerView.Adapter<CommunityPreviewRecyclerAdapter.CommunityPreviewViewHolder>() {
    val viewmodel : CommunityViewModel = CommunityViewModel()
    interface OnCommunityMarketItemClickListener{
        fun onPreviewItemClick(position: Int)
    }
    var listener: OnCommunityMarketItemClickListener? = null

    override fun getItemCount(): Int {
        return postDataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityPreviewViewHolder {
        val viewbinding = FragmentCommunityPreviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  CommunityPreviewViewHolder(viewbinding, parent, listener)
    }

    override fun onBindViewHolder(holder: CommunityPreviewViewHolder, position: Int) {
        holder.bind(postDataList[position])

    }

    fun getItem(position: Int): PostDataInfo {
        return postDataList[position]
    }

    inner class CommunityPreviewViewHolder(viewbinding: FragmentCommunityPreviewItemBinding, itemview: ViewGroup, listener: OnCommunityMarketItemClickListener?) : RecyclerView.ViewHolder(viewbinding.root) {
        val binding = viewbinding

        fun bind(postDataInfo: PostDataInfo) {
            if(postDataInfo.post_photo_uri?.size != 0){
                Glide.with(binding.communityPreviewThumbnail).load(viewmodel.getPostPhotoThumbnailData(postDataInfo.post_photo_uri?.get(0)))
            }
            binding.communityPreviewName.text = postDataInfo.post_name
            binding.communityPreviewTitle.text = postDataInfo.post_title
            binding.communityPreviewContents.text = postDataInfo.post_contents
            binding.communityPreviewTime.text = postDataInfo.post_date

       }
       init {
            itemView.setOnClickListener(){
                listener?.onPreviewItemClick(bindingAdapterPosition)
                return@setOnClickListener
            }
       }
    }
}