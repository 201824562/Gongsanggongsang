package com.example.userapp.ui.main.settings

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.userapp.base.BaseSessionFragment
import com.example.userapp.databinding.FragmentSettingsBinding
import com.example.userapp.databinding.FragmentSettingsChangeInfoBinding
import kotlinx.android.parcel.Parcelize

class SettingsChangeInfoFragment: BaseSessionFragment<FragmentSettingsChangeInfoBinding, SettingsViewModel>() {
    override lateinit var viewbinding: FragmentSettingsChangeInfoBinding
    override val viewmodel: SettingsViewModel by viewModels()

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewbinding = FragmentSettingsChangeInfoBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) { }

    override fun initDataBinding(savedInstanceState: Bundle?) { }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.run {
         changeNicknameBtn.setOnClickListener {
             findNavController().navigate(SettingsChangeInfoFragmentDirections
                 .actionSettingsChangeInfoFragmentToSettingsChangeInfoDetailFragment(ChangeInfoType.NICKNAME)) }
         changeIdBtn.setOnClickListener {
             findNavController().navigate(SettingsChangeInfoFragmentDirections
                 .actionSettingsChangeInfoFragmentToSettingsChangeInfoDetailFragment(ChangeInfoType.ID)) }
         changePwdBtn.setOnClickListener {
             findNavController().navigate(SettingsChangeInfoFragmentDirections
                 .actionSettingsChangeInfoFragmentToSettingsChangeInfoDetailFragment(ChangeInfoType.PWD)) }
        }
    }

}

@Parcelize
enum class ChangeInfoType() : Parcelable {
    NICKNAME, ID, PWD
}