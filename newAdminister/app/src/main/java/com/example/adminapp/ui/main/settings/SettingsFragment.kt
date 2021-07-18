package com.example.adminapp.ui.main.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.adminapp.R
import com.example.adminapp.base.BaseSessionFragment
import com.example.adminapp.databinding.FragmentMainhomeSettingsBinding
import com.example.adminapp.utils.WrapedDialogBasicTwoButton

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

    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.run {
            cardView.setOnClickListener { /*findNavController().navigate(R.id.action_mainFragment_to_settingsFragment) */}
            logoutBtn.setOnClickListener{
                val dialog = WrapedDialogBasicTwoButton(requireContext(), "로그아웃하시겠습니까?", "", "로그아웃").apply {
                    clickListener = object : WrapedDialogBasicTwoButton.DialogButtonClickListener{
                        override fun dialogCloseClickListener() { dismiss() }
                        override fun dialogDeleteClickListener() {
                            viewmodel.deleteUserInfo()
                            dismiss()
                        }
                    }
                }
                showDialog(dialog, viewLifecycleOwner)
            }
        }
    }

}