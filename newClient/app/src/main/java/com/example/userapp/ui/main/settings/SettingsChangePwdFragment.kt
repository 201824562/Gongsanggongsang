package com.example.userapp.ui.main.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.userapp.R
import com.example.userapp.base.BaseSessionFragment
import com.example.userapp.databinding.FragmentSettingsChangeInfoDetailBinding
import com.example.userapp.databinding.FragmentSettingsChangePwdBinding
import com.example.userapp.utils.WrapedDialogBasicTwoButton
import com.example.userapp.utils.hideKeyboard

class SettingsChangePwdFragment : BaseSessionFragment<FragmentSettingsChangePwdBinding, SettingsViewModel>() {
    override lateinit var viewbinding: FragmentSettingsChangePwdBinding
    override val viewmodel: SettingsViewModel by viewModels()

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewbinding = FragmentSettingsChangePwdBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) { viewbinding.fragmentContent.setOnClickListener { hideKeyboard(it) } }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.run {
            invalidUserPwdEventLiveData.observe(viewLifecycleOwner) { setPwdErrorMessage(it) }
            validUserPwdEventLiveData.observe(viewLifecycleOwner){ setPwdEmptyMessage() }
            invalidUserPwd2EventLiveData.observe(viewLifecycleOwner) { setPwd2ErrorMessage(it) }
            validUserPwd2EventLiveData.observe(viewLifecycleOwner){ setPwd2EmptyMessage() }
            sendChangeInfoEventLiveData.observe(viewLifecycleOwner){ makeDialog() }
            onSuccessChangeInfoEvent.observe(viewLifecycleOwner){ findNavController().navigate(R.id.action_settingsChangePwdFragment_pop ) }

        }
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.run {

            editTextPwd.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus && (getUserPwd().isEmpty()||getUserPwd().isBlank())) {
                    setPwdErrorMessage("8~22자리의 영문/숫자로 입력해주세요.")
                    setPwd2EmptyMessage()
                }
            }
            editTextPwd2.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus && (getUserPwd().isEmpty()|| getUserPwd().isBlank())) {
                    setPwd2ErrorMessage("비밀번호를 먼저 입력해주세요.")
                    setPwdEmptyMessage()
                }else if (hasFocus && getUserPwd().isNotEmpty() && getUserPwd().isNotBlank()) setPwdEmptyMessage()
            }

            showingPwdBtn.setOnClickListener {
                showingPwdBtn.isSelected = !showingPwdBtn.isSelected
                if (showingPwdBtn.isSelected) {
                    editTextPwd.inputType = 0x00000001
                    editTextPwd.typeface = resources.getFont(R.font.notosan_font_family) }
                else editTextPwd.inputType = 0x00000081
            }
            showingPwd2Btn.setOnClickListener {
                showingPwd2Btn.isSelected = !showingPwd2Btn.isSelected
                if (showingPwd2Btn.isSelected) {
                    editTextPwd2.inputType = 0x00000001
                    editTextPwd2.typeface = resources.getFont(R.font.notosan_font_family) }
                else editTextPwd2.inputType = 0x00000081
            }
            changeInfoBtn.setOnClickListener { viewmodel.checkForSendChangePwd(getUserPwd(), getUserPwd2()) }
        }
    }

    private fun setPwdErrorMessage(message: String) {
        viewbinding.editTextPwd.setBackgroundResource(R.drawable.edittext_underline_pinkishorange)
        viewbinding.textPwdWarning.apply {
            text = message
            visibility = View.VISIBLE
        }
    }
    private fun setPwdEmptyMessage() {
        viewbinding.editTextPwd.setBackgroundResource(R.drawable.edittext_underline_black20)
        viewbinding.textPwdWarning.text = "" }

    private fun setPwd2ErrorMessage(message: String) {
        viewbinding.editTextPwd2.setBackgroundResource(R.drawable.edittext_underline_pinkishorange)
        viewbinding.textPwd2Warning.apply {
            text = message
            visibility = View.VISIBLE
        }
    }
    private fun setPwd2EmptyMessage() {
        viewbinding.editTextPwd2.setBackgroundResource(R.drawable.edittext_underline_black20)
        viewbinding.textPwd2Warning.text = "" }

    private fun getUserPwd() = viewbinding.editTextPwd.text.toString().trim()
    private fun getUserPwd2() = viewbinding.editTextPwd2.text.toString().trim()

    private fun makeDialog(){
        val dialog = WrapedDialogBasicTwoButton(requireContext(), "변경하시겠습니까?", "취소", "변경하기").apply {
            clickListener = object : WrapedDialogBasicTwoButton.DialogButtonClickListener{
                override fun dialogCloseClickListener() { dismiss() }
                override fun dialogCustomClickListener() {
                    viewmodel.sendChangePwdToServer(getUserPwd())
                }
            }
        }
        showDialog(dialog, viewLifecycleOwner)
    }

}