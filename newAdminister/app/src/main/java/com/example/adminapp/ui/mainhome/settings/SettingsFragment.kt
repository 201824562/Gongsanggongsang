package com.example.adminapp.ui.mainhome.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.adminapp.R
import com.example.adminapp.base.BaseFragment
import com.example.adminapp.databinding.FragmentMainhomeSettingsBinding

class SettingsFragment : BaseFragment<FragmentMainhomeSettingsBinding, SettingsViewModel>(){

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
        viewbinding.cardView.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_settingsFragment)
        }
    }

}