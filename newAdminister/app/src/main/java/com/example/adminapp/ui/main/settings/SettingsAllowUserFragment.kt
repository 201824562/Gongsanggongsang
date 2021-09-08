package com.example.adminapp.ui.main.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.adminapp.R
import com.example.adminapp.ui.base.BaseSessionFragment
import com.example.adminapp.data.model.User
import com.example.adminapp.databinding.FragmentSettingsAllowUserBinding
import com.example.adminapp.databinding.ItemSettingsWaitingUserBinding
import com.example.adminapp.utils.WrapedDialogBasicTwoButton

class SettingsAllowUserFragment : BaseSessionFragment<FragmentSettingsAllowUserBinding, SettingsViewModel>() {
    override lateinit var viewbinding: FragmentSettingsAllowUserBinding
    override val viewmodel: SettingsViewModel by viewModels()
    private lateinit var settingsAllowUserRVAdapter: SettingsAllowUserRVAdapter

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewbinding = FragmentSettingsAllowUserBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        viewbinding.backBtn.setOnClickListener { findNavController().navigate(R.id.action_settingsAllowUserFragment_pop) }
        setRecyclerView()
    }

    override fun initDataBinding(savedInstanceState: Bundle?) { }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewmodel.getSettingsWaitingUserList().observe(viewLifecycleOwner){
            if (it.isEmpty()){ showEmptyView() }
            else showRV(it)
        }
    }

    private fun setRecyclerView() {
        settingsAllowUserRVAdapter = SettingsAllowUserRVAdapter(requireContext(), object : SettingsAllowUserRVAdapter.OnItemClickListener {
            override fun allowUserClick(position: Int, userData: User) {
                makeDialog(true, userData, "${userData.name}님을 승인하시겠습니까?", "승인")
            }
            override fun denyUserClick(position: Int, userData: User) {
                makeDialog(false, userData, "${userData.name}님을 승인거부 하시겠습니까?\n" +
                        "해당 유저의 정보는 삭제되며 되돌릴 수 없습니다.", "거부")
            }
        })
        viewbinding.settingsAllowUserRv.adapter = settingsAllowUserRVAdapter
    }

    private fun showEmptyView(){
        viewbinding.apply {
            settingsAllowUserEmptyView.visibility = View.VISIBLE
            settingsAllowUserRv.visibility = View.GONE
        }
    }

    private fun showRV(list : List<User>){
        viewbinding.run{
            settingsAllowUserEmptyView.visibility = View.GONE
            settingsAllowUserRv.visibility = View.VISIBLE
            settingsAllowUserRVAdapter.submitList(list)
        }
    }

    private fun makeDialog(allow : Boolean, userData: User, content : String, customButton : String){
        val dialog = WrapedDialogBasicTwoButton(requireContext(), content, "취소", customButton).apply {
            clickListener = object : WrapedDialogBasicTwoButton.DialogButtonClickListener{
                override fun dialogCloseClickListener() { dismiss() }
                override fun dialogCustomClickListener() {
                    if (allow) viewmodel.makeWaitingUserAllow(userData)
                    else viewmodel.makeWaitingUserDelete(userData)
                    dismiss()
                }
            }
        }
        showDialog(dialog, viewLifecycleOwner)
    }
}

class SettingsAllowUserRVAdapter(private val context : Context, private val  listener : OnItemClickListener)
    : ListAdapter<User, SettingsAllowUserRVAdapter.ViewHolder>(AddressDiffCallback) {

    companion object {
        val AddressDiffCallback = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return (oldItem.id == newItem.id)
            }
            override fun areContentsTheSame(oldItem:User, newItem: User): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface OnItemClickListener {
        fun allowUserClick(position: Int, userData : User)
        fun denyUserClick (position: Int, userData : User)
    }

    inner class ViewHolder(val binding: ItemSettingsWaitingUserBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemSettingsWaitingUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { item ->
            holder.binding.itemWaitingUserName.text = item.name
            holder.binding.itemWaitingUserNickname.text = item.nickname
            holder.binding.itemWaitingUserBirthday.text = showBirthText(item.birthday)
            holder.binding.itemWaitingUserSmsInfo.text = showSmsText(item.smsInfo)
            holder.binding.allowBtn.setOnClickListener { listener.allowUserClick(position, item) }
            holder.binding.denyBtn.setOnClickListener { listener.denyUserClick(position, item) }
        }

    }

    private fun showBirthText(birth : String): String {
        var birthText : String = ""
        birthText += birth.subSequence(0 until 4)
        birthText += "/"
        birthText += birth.subSequence(4 until 6)
        birthText += "/"
        birthText += birth.subSequence(6 until 8)
        return birthText
    }

    private fun showSmsText(sms : String): String {
        var smsText : String = ""
        smsText += sms.subSequence(0 until 3)
        smsText += "-"
        smsText += sms.subSequence(3 until 7)
        smsText += "-"
        smsText += sms.subSequence(7 until 11)
        return smsText
    }

}