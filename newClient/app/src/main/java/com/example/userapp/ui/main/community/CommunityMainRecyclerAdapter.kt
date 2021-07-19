package com.example.userapp.ui.main.community

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.userapp.data.model.PostDataInfo
import com.example.userapp.databinding.FragmentCommunityMainItemBinding
import com.example.userapp.databinding.FragmentCommunityPreviewItemBinding



class CommunityMainRecyclerAdapter(var communityMainItemList : ArrayList<CommunityMainItem>): RecyclerView.Adapter<CommunityMainRecyclerAdapter.CommunityMainViewHolder>() {
    val viewmodel : CommunityViewModel = CommunityViewModel()
    interface OnCommunityMainItemClickListener{
        fun onMainItemClick(position: Int)
    }
    var listener: OnCommunityMainItemClickListener? = null

    override fun getItemCount(): Int {
        return communityMainItemList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityMainViewHolder {
        val viewbinding = FragmentCommunityMainItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  CommunityMainViewHolder(viewbinding, parent, listener)
    }

    override fun onBindViewHolder(holder: CommunityMainViewHolder, position: Int) {
        holder.bind(communityMainItemList[position])

    }

    fun getItem(position: Int): CommunityMainItem {
        return communityMainItemList[position]
    }


    inner class CommunityMainViewHolder(viewbinding: FragmentCommunityMainItemBinding, itemview: ViewGroup, listener: OnCommunityMainItemClickListener?) : RecyclerView.ViewHolder(viewbinding.root) {
        val binding = viewbinding

        fun bind(communityMainItem: CommunityMainItem) {
            binding.communityMainItemImage.setImageDrawable(communityMainItem.community_main_icon)
            binding.communityMainItemName.text = communityMainItem.community_main_name
            binding.communityMainItemHello.text = communityMainItem.community_main_hello
        }
        init {
            itemView.setOnClickListener(){
                listener?.onMainItemClick(bindingAdapterPosition)
                return@setOnClickListener
            }
        }
    }
}