package com.example.adminapp.ui.mainhome.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.adminapp.base.BaseFragment
import com.example.adminapp.databinding.FragmentMainhomeCommunityBinding

class CommunityFragment : BaseFragment<FragmentMainhomeCommunityBinding, CommunityViewModel>(){
    override lateinit var viewbinding: FragmentMainhomeCommunityBinding

    override val viewmodel: CommunityViewModel by viewModels()

    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentMainhomeCommunityBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
    }

}