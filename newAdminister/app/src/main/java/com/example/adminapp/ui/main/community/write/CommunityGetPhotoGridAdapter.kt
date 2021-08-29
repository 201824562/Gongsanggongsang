package com.example.adminapp.ui.main.community.write

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.example.adminapp.ui.main.community.CommunityViewModel

class CommunityGetPhotoGridAdapter(val context: Context, uri_arr:ArrayList<String>, viewModel: CommunityViewModel) : BaseAdapter() {
    private var photo_url_items = ArrayList<String>()
    private var selectedItems = arrayListOf<Int>()
    interface OnCommunityLocalPhotoItemClickListener{
        fun onLocalPhotoItemClick(position: Int)
    }
    var listener: OnCommunityLocalPhotoItemClickListener? = null
    private var viewModel = viewModel
    init {
        this.photo_url_items = uri_arr
    }

    override fun getCount(): Int {
        return photo_url_items.size
    }

    override fun getItem(position: Int): String {
        return photo_url_items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(p: Int, convertView: View?, parent: ViewGroup?): View {
        val imageView = ImageView(context)
        val display = context.resources.displayMetrics
        imageView.setOnClickListener {
            listener?.onLocalPhotoItemClick(p)
            if(p in selectedItems){
                imageView.colorFilter = null;
                selectedItems.remove(p)
            }
            else{
                imageView.setColorFilter(Color.parseColor("#cfe1df"), PorterDuff.Mode.OVERLAY);
                selectedItems.add(p)
            }
            return@setOnClickListener
        }
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.layoutParams = LinearLayout.LayoutParams(display.widthPixels/3,display.widthPixels/3)
        Glide.with(context).load(photo_url_items[p]).into(imageView)

        return imageView
    }

}