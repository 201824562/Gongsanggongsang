package com.example.adminapp.ui.main.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.adminapp.MainActivity
import com.example.adminapp.R
import com.example.adminapp.base.BaseSessionFragment
import com.example.adminapp.databinding.FragmentMainhomeSettingsBinding
import com.example.adminapp.utils.WrapedDialogBasicTwoButton
import java.io.IOException

class SettingsFragment : BaseSessionFragment<FragmentMainhomeSettingsBinding, SettingsViewModel>(){

    override lateinit var viewbinding: FragmentMainhomeSettingsBinding
    override val viewmodel: SettingsViewModel by viewModels()

    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentMainhomeSettingsBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {

    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.onSuccessDeleteUserInfo.observe(viewLifecycleOwner){  logout() }
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.run {
            cardView.setOnClickListener { /*findNavController().navigate(R.id.action_mainFragment_to_settingsFragment) */}
            logoutBtn.setOnClickListener{
                val dialog = WrapedDialogBasicTwoButton(requireContext(), "정말 로그아웃 하시겠어요?", "취소", "로그아웃").apply {
                    clickListener = object : WrapedDialogBasicTwoButton.DialogButtonClickListener{
                        override fun dialogCloseClickListener() { dismiss() }
                        override fun dialogCustomClickListener() {
                            viewmodel.deleteUserInfo()
                            dismiss()
                        }
                    }
                }
                showDialog(dialog, viewLifecycleOwner)
            }
        }
    }

    private fun logout() {
        try {
            Intent(context, MainActivity::class.java).apply {
                requireActivity().finish()
                startActivity(this)
            }
            requireActivity().finish()
        } catch (e: IOException) {
            findNavController().navigate(R.id.action_global_introFragment)
        }
    }

}