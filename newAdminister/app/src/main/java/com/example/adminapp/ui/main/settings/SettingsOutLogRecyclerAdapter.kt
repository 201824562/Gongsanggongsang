package com.example.adminapp.ui.main.settings

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.adminapp.data.model.PostDataInfo
import com.example.adminapp.databinding.FragmentSettingsOutLogItemBinding
import java.time.LocalDate
import java.time.LocalTime


class SettingsOutLogRecyclerAdapter(var postDataList : ArrayList<PostDataInfo>): RecyclerView.Adapter<SettingsOutLogRecyclerAdapter.CommunityPreviewViewHolder>() {

    interface OnCommunityMarketItemClickListener{
        fun onPreviewItemClick(position: Int)
    }
    var listener: OnCommunityMarketItemClickListener? = null

    override fun getItemCount(): Int {
        return postDataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityPreviewViewHolder {
        val viewbinding = FragmentSettingsOutLogItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  CommunityPreviewViewHolder(viewbinding, parent, listener)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CommunityPreviewViewHolder, position: Int) {
        holder.bind(postDataList[position])

    }

    fun getItem(position: Int): PostDataInfo {
        return postDataList[position]
    }

    inner class CommunityPreviewViewHolder(viewbinding: FragmentSettingsOutLogItemBinding, itemview: ViewGroup, listener: OnCommunityMarketItemClickListener?) : RecyclerView.ViewHolder(viewbinding.root) {
       val binding = viewbinding

       @RequiresApi(Build.VERSION_CODES.O)
       fun bind(postDataInfo: PostDataInfo) {
           binding.settingsOutItemName.text = postDataInfo.post_name.toString()
           binding.settingsOutItemWhere.text = postDataInfo.post_state + "호"
           val postDateNow: String = LocalDate.now().toString()
           val postTimeNow: String = LocalTime.now().toString()

           if(!postDataInfo.post_anonymous) { binding.settingsOutItemState.text = "승인대기" }
           else { binding.settingsOutItemState.text = "승인완료" }

           if(postDataInfo.post_date == postDateNow){
               val hour = postTimeNow.substring(0,2).toInt() - postDataInfo.post_time.substring(0,2).toInt()
               val minute = postTimeNow.substring(3,5).toInt() - postDataInfo.post_time.substring(3,5).toInt()
               if(hour == 0){
                   binding.settingsOutItemTime.text = "${minute}분 전"
               }
               else{
                   binding.settingsOutItemTime.text = "${hour}시간 전"
               }
           }
           else{
               binding.settingsOutItemTime.text = postDataInfo.post_date.substring(5)
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