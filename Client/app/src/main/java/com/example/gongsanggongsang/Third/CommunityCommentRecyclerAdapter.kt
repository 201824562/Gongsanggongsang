package com.example.gongsanggongsang.Third

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gongsanggongsang.R
import com.example.gongsanggongsang.data.PostCommentDataClass
import kotlinx.android.synthetic.main.fragment_community_comment_item.view.*


class CommunityCommentRecyclerAdapter(val communityCommentItems:ArrayList<PostCommentDataClass>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return communityCommentItems.size;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_community_comment_item, parent, false);
        val viewHolder = CommunityCommentViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val communityCommentModel = communityCommentItems[position]
        val communityCommentViewHolder = holder as CommunityCommentViewHolder
        communityCommentViewHolder.bind(communityCommentModel)
    }

    fun getItem(position: Int): PostCommentDataClass {
        return communityCommentItems[position]
    }

    class CommunityCommentViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var name = itemview.community_comment_name
        var comment = itemview.community_comment_post
        var date = itemview.community_comment_date

        fun bind(communityCommentModel: PostCommentDataClass) {
            name.text = communityCommentModel.name
            comment.text = communityCommentModel.contents
            date.text = communityCommentModel.date
        }

    }
}