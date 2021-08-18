package com.example.userapp.ui.main.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.userapp.MainActivity
import com.example.userapp.R
import com.example.userapp.base.BaseFragment
import com.example.userapp.databinding.FragmentMainhomeHomeBinding
import com.example.userapp.ui.main.community.CommunityViewModel

class HomeFragment : BaseFragment<FragmentMainhomeHomeBinding, CommunityViewModel>(){
    override lateinit var viewbinding: FragmentMainhomeHomeBinding
    override val viewmodel: CommunityViewModel by viewModels()
    var agency = ""
    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentMainhomeHomeBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        val ac = activity as MainActivity
        ac.getUserData()?.let {
            agency = it.agency
        }
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.run {
            mainHomeNoticeAllButton.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_mainhomeNoticeFragment)
            }

            viewmodel.getNoticePostData(agency).observe(viewLifecycleOwner){
                when {
                    it.size >= 3 -> {
                        mainHomeNotice1Title.text = it[0].post_title
                        mainHomeNotice1Date.text = it[0].post_date
                        mainHomeNotice2Title.text = it[1].post_title
                        mainHomeNotice2Date.text = it[1].post_date
                        mainHomeNotice3Title.text = it[2].post_title
                        mainHomeNotice3Date.text = it[2].post_date
                    }
                    it.size == 2 -> {
                        mainHomeNotice1Title.text = it[0].post_title
                        mainHomeNotice1Date.text = it[0].post_date
                        mainHomeNotice2Title.text = it[1].post_title
                        mainHomeNotice2Date.text = it[1].post_date
                    }
                    it.size == 1 -> {
                        mainHomeNotice1Title.text = it[0].post_title
                        mainHomeNotice1Date.text = it[0].post_date
                    }
                }
            }
        }
    }
}