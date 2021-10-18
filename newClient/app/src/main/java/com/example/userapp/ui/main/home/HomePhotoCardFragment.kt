package com.example.userapp.ui.main.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.userapp.R
import com.example.userapp.databinding.FragmentMainhomeDetailPhotoCardBinding
import com.example.userapp.databinding.FragmentMainhomeHomeBinding
import com.example.userapp.ui.base.BaseSessionFragment
import com.example.userapp.ui.base.BaseSessionViewModel
import com.example.userapp.ui.main.community.CommunityViewModel

class HomePhotoCardFragment :  BaseSessionFragment<FragmentMainhomeDetailPhotoCardBinding, HomeViewModel>() {
    override lateinit var viewbinding: FragmentMainhomeDetailPhotoCardBinding
    override val viewmodel: HomeViewModel by viewModels()
    private lateinit var homePhotoCardRecyclerAdapter: HomePhotoCardRecyclerAdapter

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentMainhomeDetailPhotoCardBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        initMainHomePhotoCardRV()
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewbinding.backBtn.setOnClickListener { findNavController().navigate(R.id.action_homePhotoCardFragment_pop) }
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewmodel.getNoticePostData().observe(viewLifecycleOwner){
            homePhotoCardRecyclerAdapter.submitList(it)
        }
    }

    private fun initMainHomePhotoCardRV(){
        viewbinding.run{
            homePhotoCardRecyclerAdapter = HomePhotoCardRecyclerAdapter()
            viewbinding.photoCardRv.adapter = homePhotoCardRecyclerAdapter
        }
    }

}