package com.example.userapp.ui.main.community.post

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.userapp.data.entity.PostCommentDataClass
import com.example.userapp.databinding.FragmentCommunityCommentItemBinding
import com.example.userapp.ui.main.community.CommunityViewModel
import java.time.LocalDate
import java.time.LocalTime


class CommunityCommentRecyclerAdapter(private val communityCommentItems:ArrayList<PostCommentDataClass>): RecyclerView.Adapter<CommunityCommentRecyclerAdapter.CommunityCommentViewHolder>() {
    val viewmodel : CommunityViewModel = CommunityViewModel()
    interface OnCommunityCommentItemClickListener{
        fun onCommentItemClick(position: Int)
    }
    var listener: OnCommunityCommentItemClickListener? = null
    override fun getItemCount(): Int {
        return communityCommentItems.size;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityCommentViewHolder {
        val viewbinding = FragmentCommunityCommentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  CommunityCommentViewHolder(viewbinding, parent, listener)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CommunityCommentViewHolder, position: Int) {
        holder.bind(communityCommentItems[position])
    }

    fun getItem(position: Int): PostCommentDataClass {
        return communityCommentItems[position]
    }

    class CommunityCommentViewHolder(
        viewbinding: FragmentCommunityCommentItemBinding,
        itemview: ViewGroup,
        listener: OnCommunityCommentItemClickListener?
    ) : RecyclerView.ViewHolder(viewbinding.root) {
        val binding = viewbinding
        val listener = listener
        val viewModel : CommunityViewModel = CommunityViewModel()
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(it : PostCommentDataClass) {
            if(it.commentAnonymous){
                binding.communityCommentName.text = "익명"
            }
            else{
                binding.communityCommentName.text = it.commentName
            }
            binding.communityCommentPost.text = it.commentContents
            val postDateNow: String = LocalDate.now().toString()
            val postTimeNow: String = LocalTime.now().toString()
            if(it.commentDate == postDateNow){
                val hour = postTimeNow.substring(0,2).toInt() - it.commentTime.substring(0,2).toInt()
                val minute = postTimeNow.substring(3,5).toInt() - it.commentTime.substring(3,5).toInt()
                if(hour == 0){
                    binding.communityCommentDate.text = "${minute}분 전"
                }
                else{
                    binding.communityCommentDate.text = "${hour}시간 전"
                }
            }
            else{
                binding.communityCommentDate.text = it.commentDate.substring(5)
            }
        }
        init {
            itemView.setOnClickListener{
                listener?.onCommentItemClick(bindingAdapterPosition)
                return@setOnClickListener
            }
        }

    }

}