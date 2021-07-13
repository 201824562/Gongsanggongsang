package com.example.userapp.ui.main.community

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide

class CommunityShowPhotoGridAdapter(val context: Context, uri_arr:ArrayList<String>, viewModel: CommunityViewModel) : BaseAdapter() {
    private var photo_url_items = ArrayList<String>()
    private var viewModel = viewModel
    init {
        this.photo_url_items = uri_arr
    }

    override fun getCount(): Int {
        return photo_url_items.size
    }

    override fun getItem(position: Int): Any {
        return photo_url_items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(p: Int, convertView: View?, parent: ViewGroup?): View {
        val imageView = ImageView(context)
        val display = context.getResources().getDisplayMetrics()
        imageView.setOnClickListener {
            viewModel.selectPhoto(photo_url_items[p])
        }
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.layoutParams = LinearLayout.LayoutParams(display.widthPixels/5, display.heightPixels/5)
        Glide.with(context).load(photo_url_items[p]).into(imageView)

        return imageView
    }
}