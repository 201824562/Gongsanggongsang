package com.example.userapp.ui.main.community.preview

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isInvisible
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.userapp.R
import com.example.userapp.data.model.PostDataInfo
import com.example.userapp.databinding.FragmentCommunityPreviewItemBinding
import com.example.userapp.ui.main.community.CommunityViewModel
import java.time.LocalDate
import java.time.LocalTime


class CommunityPreviewRecyclerAdapter(var postDataList : ArrayList<PostDataInfo>): RecyclerView.Adapter<CommunityPreviewRecyclerAdapter.CommunityPreviewViewHolder>() {

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CommunityPreviewViewHolder, position: Int) {
        holder.bind(postDataList[position])

    }

    fun getItem(position: Int): PostDataInfo {
        return postDataList[position]
    }

    inner class CommunityPreviewViewHolder(viewbinding: FragmentCommunityPreviewItemBinding, itemview: ViewGroup, listener: OnCommunityMarketItemClickListener?) : RecyclerView.ViewHolder(viewbinding.root) {
        val binding = viewbinding

        @SuppressLint("ResourceAsColor")
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(postDataInfo: PostDataInfo) {
            if(postDataInfo.post_category != "4_WITH" && postDataInfo.post_state != "none"){
                binding.communityPreviewCategory.text = postDataInfo.post_state
            }
            else{
                binding.communityPreviewName.text = postDataInfo.post_name
            }
            binding.communityPreviewName.text = postDataInfo.post_name
            binding.communityPreviewCommentsNumber.text = postDataInfo.post_comments.toString()
            binding.communityPreviewTitle.text = postDataInfo.post_title
            binding.communityPreviewContents.text = postDataInfo.post_contents
            binding.communityPreviewPhotoNumber.text = postDataInfo.post_photo_uri.size.toString()
            if(postDataInfo.post_category == "4_WITH" && !postDataInfo.post_anonymous){
                binding.communityPreviewCategory.text = "모집 중"
                binding.communityPreviewCategory.setTextColor(Color.parseColor("#ff9966"))
            }
            if(postDataInfo.post_category == "4_WITH" && postDataInfo.post_anonymous){
                binding.communityPreviewCategory.text = "모집 완료"
                binding.communityPreviewPoint.setBackgroundColor(Color.parseColor("#7f000000"))
            }

            val postDateNow: String = LocalDate.now().toString()
            val postTimeNow: String = LocalTime.now().toString()
            if(postDataInfo.post_date == postDateNow){
                val hour = postTimeNow.substring(0,2).toInt() - postDataInfo.post_time.substring(0,2).toInt()
                val minute = postTimeNow.substring(3,5).toInt() - postDataInfo.post_time.substring(3,5).toInt()
                if(hour == 0){
                    binding.communityPreviewTime.text = "${minute}분 전"
                }
                else{
                    binding.communityPreviewTime.text = "${hour}시간 전"
                }
            }
            else{
                binding.communityPreviewTime.text = postDataInfo.post_date.substring(5)
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