package com.example.adminapp.ui.main.community.preview

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.adminapp.data.model.PostDataInfo
import com.example.adminapp.databinding.FragmentCommunityPreviewMarketItemBinding
import java.time.LocalDate
import java.time.LocalTime


class CommunityPreviewMarketRecyclerAdapter(var postDataList : ArrayList<PostDataInfo>): RecyclerView.Adapter<CommunityPreviewMarketRecyclerAdapter.CommunityPreviewMarketViewHolder>() {
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CommunityPreviewMarketViewHolder, position: Int) {
        holder.bind(postDataList[position])

    }

    fun getItem(position: Int): PostDataInfo {
        return postDataList[position]
    }

    inner class CommunityPreviewMarketViewHolder(viewbinding: FragmentCommunityPreviewMarketItemBinding, itemview: ViewGroup, listener: OnCommunityMarketItemClickListener?) : RecyclerView.ViewHolder(viewbinding.root) {
        val binding = viewbinding

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(postDataInfo: PostDataInfo) {
            binding.communityMarketPreviewTitle.text = postDataInfo.post_title
            binding.communityMarketPreviewPrice.text = postDataInfo.post_state
            binding.communityMarketPreviewContent.text = postDataInfo.post_contents
            binding.communityMarketPreviewCommentNumber.text = postDataInfo.post_comments.toString()
            binding.communityMarketPreviewName.text = postDataInfo.post_name.toString()
            binding.communityPreviewPhotoNumber.text = postDataInfo.post_photo_uri.size.toString()
            if(!postDataInfo.post_anonymous){
                binding.communityMarketPreviewCategory.text = "판매 중"
            }
            else{ binding.communityMarketPreviewCategory.text = "판매 완료" }

            val postDateNow: String = LocalDate.now().toString()
            val postTimeNow: String = LocalTime.now().toString()
            if(postDataInfo.post_date == postDateNow){
                val hour = postTimeNow.substring(0,2).toInt() - postDataInfo.post_time.substring(0,2).toInt()
                val minute = postTimeNow.substring(3,5).toInt() - postDataInfo.post_time.substring(3,5).toInt()
                if(hour == 0){
                    binding.communityMarketPreviewTime.text = "${minute}분 전"
                }
                else{
                    binding.communityMarketPreviewTime.text = "${hour}시간 전"
                }
            }
            else{
                binding.communityMarketPreviewTime.text = postDataInfo.post_date.substring(5)
            }

        }
        init {
            itemView.setOnClickListener(){
                listener?.onPreviewItemClick(bindingAdapterPosition)
                return@setOnClickListener
            }
        }
    }
}