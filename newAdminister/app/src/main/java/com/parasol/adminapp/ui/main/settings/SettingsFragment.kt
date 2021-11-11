package com.parasol.adminapp.ui.main.settings

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.parasol.adminapp.MainActivity
import com.parasol.adminapp.R
import com.parasol.adminapp.ui.base.BaseSessionFragment
import com.parasol.adminapp.data.model.AdminModel
import com.parasol.adminapp.databinding.FragmentSettingsBinding
import com.parasol.adminapp.restartActivity
import com.parasol.adminapp.utils.WrapedDialogAccentTwoButton
import com.parasol.adminapp.utils.WrapedDialogBasicOneButton
import com.parasol.adminapp.utils.WrapedDialogBasicTwoButton
import java.io.IOException

class SettingsFragment : BaseSessionFragment<FragmentSettingsBinding, SettingsViewModel>(){

    override lateinit var viewbinding: FragmentSettingsBinding
    override val viewmodel: SettingsViewModel by viewModels()

    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentSettingsBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) { }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.onSuccessGettingAdminInfo.observe(this, { showProfile(it)  })
        viewmodel.onSuccessGettingNullAdminInfo.observe(this, { restartActivity() })
        viewmodel.onSuccessDeleteFcmTokenFromServer.observe(viewLifecycleOwner){ viewmodel.deleteAdminInfoFromAppDatabase() }
        viewmodel.onSuccessDeleteUserInfo.observe(viewLifecycleOwner){  logout() }
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewmodel.getAdminInfo()
        viewbinding.run {
            changePwdBtn.setOnClickListener { findNavController().navigate(R.id.action_mainFragment_to_settingsChangePwdFragment) }
            allowUserBtn.setOnClickListener { findNavController().navigate(R.id.action_mainFragment_to_settingsAllowUserFragment) }
            manageUserBtn.setOnClickListener { findNavController().navigate(R.id.action_mainFragment_to_settingsManageUserFragment) }
            manageOutBtn.setOnClickListener { findNavController().navigate(R.id.action_mainFragment_to_settingsOutFragment)}
            outdataInitBtn.setOnClickListener{ makeOutReserveDataDialog() }
            withdrawalBtn.setOnClickListener { makeWithdrawalDialog() }
            logoutBtn.setOnClickListener{ makeLogoutDialog() }
        }
    }

    private fun showProfile(adminInfo : AdminModel){
        viewbinding.run {
            userNickname.text = "관리자"
            userName.text = adminInfo.name
            userAgency.text = adminInfo.agency
            userBirth.text = showBirthText(adminInfo.birth)
            userSms.text = showSmsText(adminInfo.phone) }
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

    private fun makeLogoutDialog(){
        val dialog = WrapedDialogBasicTwoButton(requireContext(), "로그아웃하시겠습니까?", "취소", "로그아웃").apply {
            clickListener = object : WrapedDialogBasicTwoButton.DialogButtonClickListener{
                override fun dialogCloseClickListener() { dismiss() }
                override fun dialogCustomClickListener() {
                    viewmodel.deleteDeviceTokenFromServerDatabase()
                    dismiss()
                }
            }
        }
        showDialog(dialog, viewLifecycleOwner)
    }

    private fun makeWithdrawalDialog(){
        val dialog = WrapedDialogAccentTwoButton(requireContext(), "모든 정보가 삭제되며,\n복구할 수 없습니다.\n" +
                "정말 회원탈퇴 하시겠습니까?", "취소", "탈퇴").apply {
            clickListener = object : WrapedDialogAccentTwoButton.DialogButtonClickListener{
                override fun dialogCloseClickListener() { dismiss() }
                override fun dialogCustomClickListener() {
                    //viewmodel.deleteAdminInfoFromServerDatabase()
                    askUsDialog()
                    dismiss()
                }
            }
        }
        showDialog(dialog, viewLifecycleOwner)
    }
    private fun askUsDialog(){
        val dialog = WrapedDialogBasicOneButton(requireContext(), "관리자님의 탈퇴관련은\n 공생공생에 문의해주세요.")
            .apply { clickListener = object : WrapedDialogBasicOneButton.DialogButtonClickListener {
                override fun dialogClickListener() { dismiss() } } }
        showDialog(dialog, viewLifecycleOwner)
    }

    private fun makeOutReserveDataDialog(){
        val dialog = WrapedDialogBasicTwoButton(requireContext(), "퇴실 데이터를 초기화 하시겠습니까?", "취소", "초기화").apply {
            clickListener = object : WrapedDialogBasicTwoButton.DialogButtonClickListener{
                override fun dialogCloseClickListener() { dismiss() }
                @RequiresApi(Build.VERSION_CODES.O)
                override fun dialogCustomClickListener() {
                    viewmodel.outInitialize()
                    Toast.makeText(requireContext(), "퇴실데이터가 현재 날짜 기준으로 초기화 되었습니다!", Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            }
        }
        showDialog(dialog, viewLifecycleOwner)
    }

    private fun logout() {
        try {
            Intent(context, MainActivity::class.java).apply {
                requireActivity().finish()
                startActivity(this)
            }
            requireActivity().finish()
        } catch (e: IOException) {
            findNavController().navigate(R.id.action_global_signInFragment)
        }
    }

}