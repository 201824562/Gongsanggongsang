package com.example.userapp.ui.main.community.post

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.example.userapp.ui.main.community.CommunityViewModel

class CommunityShowPhotoGridAdapter(val context: Context, uri_arr:ArrayList<String>, viewModel: CommunityViewModel) : BaseAdapter() {
    private var photoUrlItems = ArrayList<String>()
    private var viewModel = viewModel
    init {
        this.photoUrlItems = uri_arr
    }

    override fun getCount(): Int {
        return photoUrlItems.size
    }

    override fun getItem(position: Int): Any {
        return photoUrlItems[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(p: Int, convertView: View?, parent: ViewGroup?): View {
        val imageView = ImageView(context)
        val display = context.getResources().getDisplayMetrics()
        imageView.setOnClickListener {
            viewModel.selectPhoto(photoUrlItems[p])
        }
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.layoutParams = LinearLayout.LayoutParams(display.widthPixels/5, display.heightPixels/5)
        Glide.with(context).load(photoUrlItems[p]).into(imageView)

        return imageView
    }
}