package com.example.userapp.ui.main.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.userapp.MainActivity
import com.example.userapp.R
import com.example.userapp.base.BaseSessionFragment
import com.example.userapp.databinding.FragmentMainhomeSettingsBinding
import com.example.userapp.utils.LogoutDialog
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
        viewbinding.logoutBtn.setOnClickListener{
            val dialog = LogoutDialog(requireContext(), "로그아웃하시겠습니까?", "", "로그아웃").apply {
                clickListener = object : LogoutDialog.DialogButtonClickListener{
                    override fun dialogCloseClickListener() { dismiss() }
                    override fun dialogDeleteClickListener() {
                        viewmodel.deleteUserInfo("ljy3237")
                        dismiss()
                    }
                }
            }
            showDialog(dialog, viewLifecycleOwner)
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