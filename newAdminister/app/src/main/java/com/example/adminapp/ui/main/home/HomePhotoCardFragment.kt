package com.example.adminapp.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.adminapp.R
import com.example.adminapp.databinding.FragmentMainhomeDetailPhotoCardBinding
import com.example.adminapp.ui.base.BaseSessionFragment

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
        viewbinding.backBtn.setOnClickListener { findNavController().navigate(R.id.action_homePhotoCardFragment_pop) }
        initMainHomePhotoCardRV()
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.getUserPhotoCardDataList().observe(viewLifecycleOwner){
            homePhotoCardRecyclerAdapter.submitList(it)
        }
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {

    }

    private fun initMainHomePhotoCardRV(){
        viewbinding.run{
            homePhotoCardRecyclerAdapter = HomePhotoCardRecyclerAdapter()
            viewbinding.photoCardRv.adapter = homePhotoCardRecyclerAdapter
        }
    }

}