package com.example.adminapp.ui.mainhome.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.adminapp.base.BaseFragment
import com.example.adminapp.databinding.FragmentMainhomeHomeBinding

class HomeFragment : BaseFragment<FragmentMainhomeHomeBinding, HomeViewModel>(){
    override lateinit var viewbinding: FragmentMainhomeHomeBinding
    override val viewmodel: HomeViewModel by viewModels()

    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentMainhomeHomeBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
    }
}