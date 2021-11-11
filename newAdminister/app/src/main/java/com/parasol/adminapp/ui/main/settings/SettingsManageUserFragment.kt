package com.parasol.adminapp.ui.main.settings

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
import com.parasol.adminapp.R
import com.parasol.adminapp.ui.base.BaseSessionFragment
import com.parasol.adminapp.data.model.User
import com.parasol.adminapp.databinding.FragmentSettingsManageUserBinding
import com.parasol.adminapp.databinding.ItemSettingsAllowedUserBinding
import com.parasol.adminapp.utils.WrapedDialogBasicTwoButton

class SettingsManageUserFragment : BaseSessionFragment<FragmentSettingsManageUserBinding, SettingsViewModel>() {
    override lateinit var viewbinding: FragmentSettingsManageUserBinding
    override val viewmodel: SettingsViewModel by viewModels()
    private lateinit var settingsManageUserRVAdapter: SettingsManageUserRVAdapter

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewbinding = FragmentSettingsManageUserBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        viewbinding.backBtn.setOnClickListener { findNavController().navigate(R.id.action_settingsManageUserFragment_pop) }
        setRecyclerView()
    }

    override fun initDataBinding(savedInstanceState: Bundle?) { }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewmodel.getSettingsAllowedUserList().observe(viewLifecycleOwner){
            if (it.isEmpty()){ showEmptyView() }
            else showRV(it)
        }
    }

    private fun setRecyclerView() {
        settingsManageUserRVAdapter = SettingsManageUserRVAdapter(requireContext(), object : SettingsManageUserRVAdapter.OnItemClickListener {
            override fun denyUserClick(position: Int, userData: User) {
                makeDialog(userData, "${userData.name}님을 퇴출하시겠습니까?\n" +
                        "해당 유저의 정보는 삭제되며 되돌릴 수 없습니다.", "퇴출")
            }
        })
        viewbinding.settingsManageUserRv.adapter = settingsManageUserRVAdapter
    }

    private fun showEmptyView(){
        viewbinding.apply {
            settingsManageUserEmptyView.visibility = View.VISIBLE
            settingsManageUserRv.visibility = View.GONE
        }
    }

    private fun showRV(list : List<User>){
        viewbinding.run{
            settingsManageUserEmptyView.visibility = View.GONE
            settingsManageUserRv.visibility = View.VISIBLE
            settingsManageUserRVAdapter.submitList(list)
        }
    }

    private fun makeDialog(userData: User, content : String, customButton : String){
        val dialog = WrapedDialogBasicTwoButton(requireContext(), content, "취소", customButton).apply {
            clickListener = object : WrapedDialogBasicTwoButton.DialogButtonClickListener{
                override fun dialogCloseClickListener() { dismiss() }
                override fun dialogCustomClickListener() {
                    viewmodel.makeUserWithdrawal(userData)
                    dismiss()
                }
            }
        }
        showDialog(dialog, viewLifecycleOwner)
    }
}

class SettingsManageUserRVAdapter(private val context : Context, private val  listener : OnItemClickListener)
    : ListAdapter<User, SettingsManageUserRVAdapter.ViewHolder>(AddressDiffCallback) {

    companion object {
        val AddressDiffCallback = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return (oldItem.id == newItem.id)
            }
            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface OnItemClickListener {
        fun denyUserClick (position: Int, userData : User)
    }

    inner class ViewHolder(val binding: ItemSettingsAllowedUserBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemSettingsAllowedUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { item ->
            holder.binding.itemAllowedUserName.text = item.name
            holder.binding.itemAllowedUserNickname.text = item.nickname
            holder.binding.itemAllowedUserBirthday.text = showBirthText(item.birthday)
            holder.binding.itemAllowedUserSmsInfo.text = showSmsText(item.smsInfo)
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