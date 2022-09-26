package com.parasol.userapp.ui.main.community.write

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.parasol.userapp.databinding.FragmentCommunityAttachImageItemBinding
import com.parasol.userapp.databinding.FragmentCommunityAttachPostPhotoItemBinding


class CommunityAttachPostPhotoRecyclerAdapter(val attachPhotoItems:ArrayList<String>): RecyclerView.Adapter<CommunityAttachPostPhotoRecyclerAdapter.CommunityAttachPhotoViewHolder>() {
    interface OnCommunityPhotoItemClickListener{
        fun onPhotoItemClick(position: Int)
    }
    interface OnCommunityPhotoDeleteClickListener{
        fun onPhotoDeleteButtonClick(position: Int)
    }
    var listener: OnCommunityPhotoItemClickListener? = null
    var deleteButtonListener: OnCommunityPhotoDeleteClickListener? = null
    //var listener: OnAttachPhotoItemDeleteButtonClickListener? = null

    override fun getItemCount(): Int {
        return attachPhotoItems.size;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityAttachPhotoViewHolder {
        val viewbinding = FragmentCommunityAttachPostPhotoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  CommunityAttachPhotoViewHolder(viewbinding, parent, listener, deleteButtonListener)
    }

    override fun onBindViewHolder(holder: CommunityAttachPhotoViewHolder, position: Int) {
        holder.bind(attachPhotoItems[position])
    }

    class CommunityAttachPhotoViewHolder(
        viewbinding: FragmentCommunityAttachPostPhotoItemBinding,
        itemview: ViewGroup,
        listener: OnCommunityPhotoItemClickListener?,
        deleteButtonListener: OnCommunityPhotoDeleteClickListener?
    ) : RecyclerView.ViewHolder(viewbinding.root) {
        val binding = viewbinding
        fun bind(it : String) {
            Glide.with(itemView).load(it).
            apply(RequestOptions.overrideOf(512, 512))
                .apply(RequestOptions.centerCropTransform())
                .into(binding.attachImage)
        }
        init {
            itemView.setOnClickListener(){
                listener?.onPhotoItemClick(bindingAdapterPosition)
                return@setOnClickListener
            }
        }

    }


}