package com.example.gongsanggongsangAdmin.Fifth.UserInfo

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.gongsanggongsangAdmin.data.UserDataClass
import com.example.gongsanggongsangAdmin.databinding.FragmentUserlistItemsBinding
import kotlinx.android.synthetic.main.fragment_userlist_items.view.*


//Waiting 하는 친구의 어댑터이다.
class Administer_RCV( received_items : List<UserDataClass>, received_viewmodel: AdministerViewmodel) : RecyclerView.Adapter<Administer_RCV.SearchViewHolder>() {

    val viewmodel : AdministerViewmodel = received_viewmodel
    private lateinit var binding : FragmentUserlistItemsBinding
    var rc_userItems = received_items

    override fun getItemCount(): Int {
        return rc_userItems.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        binding = FragmentUserlistItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(parent, binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(rc_userItems[position])
    }

    inner class SearchViewHolder (received_parent: ViewGroup, binding: FragmentUserlistItemsBinding) : RecyclerView.ViewHolder(binding.root)
    {
        val parent : ViewGroup = received_parent

        fun bind (data : UserDataClass){
            binding.userLogindata = data
            itemView.Yes_btn.setOnClickListener {
                viewmodel.acceptUser(data) //수락시 -> 이동해야됨. (데이터가)!!!!!!!!!
                viewmodel.getAllWaitingusers()
                Toast.makeText(parent.context, "해당 회원이 가입되었습니다.", Toast.LENGTH_SHORT).show()
            }
            itemView.No_btn.setOnClickListener {
                viewmodel.deleteWaitingUser(data)
                viewmodel.updateAllWaitingUsers()
                Toast.makeText(parent.context, "해당 회원이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
            }

        }
    }
}


