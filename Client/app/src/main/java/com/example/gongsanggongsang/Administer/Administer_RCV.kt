package com.example.gongsanggongsang.Administer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.gongsanggongsang.data.UserDataClass
import com.example.gongsanggongsang.databinding.FragmentTestAdministerItemsBinding
import kotlinx.android.synthetic.main.fragment_test_administer_items.view.*

class Administer_RCV( received_items : List<UserDataClass>, received_viewmodel: AdministerViewmodel) : RecyclerView.Adapter<Administer_RCV.SearchViewHolder>() {

    val viewmodel : AdministerViewmodel = received_viewmodel
    private lateinit var binding : FragmentTestAdministerItemsBinding
    var rc_userItems = received_items

    override fun getItemCount(): Int {
        return rc_userItems.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        binding = FragmentTestAdministerItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(parent, binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(rc_userItems[position])
    }

    inner class SearchViewHolder (received_parent: ViewGroup, binding: FragmentTestAdministerItemsBinding) : RecyclerView.ViewHolder(binding.root)
    {
        val parent : ViewGroup = received_parent

        fun bind (data : UserDataClass){
            binding.userLogindata = data
            itemView.Yes_btn.setOnClickListener {
                viewmodel.acceptUser(data)
                viewmodel.updateAllusers()
                Toast.makeText(parent.context, "해당 회원이 가입되었습니다.", Toast.LENGTH_SHORT).show()
            }
            itemView.No_btn.setOnClickListener {
                viewmodel.denyUser(data)
                viewmodel.updateAllusers()
                Toast.makeText(parent.context, "해당 회원이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
            }

        }
    }
}