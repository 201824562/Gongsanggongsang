package com.parasol.userapp.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.parasol.userapp.R
import com.parasol.userapp.databinding.FragmentMainhomeDetailPhotoCardBinding
import com.parasol.userapp.ui.base.BaseSessionFragment

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
        viewbinding.myPhotoCardBtn.setOnClickListener { findNavController().navigate(R.id.action_homePhotoCardFragment_to_settingsPhotoCardFragment) }
    }

    private fun initMainHomePhotoCardRV(){
        viewbinding.run{
            homePhotoCardRecyclerAdapter = HomePhotoCardRecyclerAdapter()
            viewbinding.photoCardRv.adapter = homePhotoCardRecyclerAdapter
        }
    }

}