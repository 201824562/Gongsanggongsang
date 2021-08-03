package com.example.userapp.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.userapp.R
import com.example.userapp.base.BaseFragment
import com.example.userapp.databinding.FragmentMainhomeHomeBinding

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
        viewmodel
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.run {
            mainHomeNoticeAllButton.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_mainhomeNoticeFragment)
            }
        }
    }
}