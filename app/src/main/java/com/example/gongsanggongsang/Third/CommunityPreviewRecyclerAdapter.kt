package com.example.gongsanggongsang.Third

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.gongsanggongsang.Data.CommunityMarketPostModel
import com.example.gongsanggongsang.R
import kotlinx.android.synthetic.main.fragment_community_write_preview_item.view.*

class CommunityPreviewRecyclerAdapter(val communityWritePreviewItems:ArrayList<CommunityMarketPostModel>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnCommunityMarketItemClickListener{
        fun onMarketItemClick(position: Int)
    }
    var listener: OnCommunityMarketItemClickListener? = null

    override fun getItemCount(): Int {
        return communityWritePreviewItems.size;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_community_write_preview_item, parent, false);
        val viewHolder = CommunityViewHolder(view, listener)
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val communityWritePreviewModel = communityWritePreviewItems[position]
        val communityPreviewViewHolder = holder as CommunityViewHolder
        communityPreviewViewHolder.bind(communityWritePreviewModel)

    }

    fun getItem(position: Int): CommunityMarketPostModel {
        return communityWritePreviewItems[position]
    }

    class CommunityViewHolder(itemview: View, listener: OnCommunityMarketItemClickListener?) : RecyclerView.ViewHolder(itemview) {
        var title = itemview.community_preview_title
        var contents = itemview.community_preview_write

        fun bind(communityMarketPostModel: CommunityMarketPostModel) {
            title.text = communityMarketPostModel.title
            contents.text = communityMarketPostModel.contents
       }
       init {
            itemview.setOnClickListener(){
                listener?.onMarketItemClick(adapterPosition)
                return@setOnClickListener
            }
       }
    }
}