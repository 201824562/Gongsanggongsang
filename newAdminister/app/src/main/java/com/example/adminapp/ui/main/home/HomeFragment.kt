package com.example.adminapp.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.adminapp.MainActivity
import com.example.adminapp.R
import com.example.adminapp.base.BaseFragment
import com.example.adminapp.base.BaseSessionFragment
import com.example.adminapp.data.model.ReservationType
import com.example.adminapp.databinding.FragmentMainhomeHomeBinding
import com.example.adminapp.ui.main.MainFragment
import com.example.adminapp.ui.main.MainFragmentDirections
import com.example.adminapp.ui.main.community.CommunityViewModel

class HomeFragment : BaseSessionFragment<FragmentMainhomeHomeBinding, CommunityViewModel>(){
    override lateinit var viewbinding: FragmentMainhomeHomeBinding
    override val viewmodel: CommunityViewModel by viewModels()

    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentMainhomeHomeBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {  }

    override fun initDataBinding(savedInstanceState: Bundle?) {
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.run {
            textMainHomeToEquipment.setOnClickListener {
                findNavController().navigate(MainFragmentDirections.actionMainFragmentToReservationAddFragment(ReservationType.EQUIPMENT)) }
            iconMainHomeToEquipment.setOnClickListener {
                findNavController().navigate(MainFragmentDirections.actionMainFragmentToReservationAddFragment(ReservationType.EQUIPMENT)) }
            textMainHomeToFacility.setOnClickListener {
                findNavController().navigate(MainFragmentDirections.actionMainFragmentToReservationAddFragment(ReservationType.FACILITY)) }
            iconMainHomeToFacility.setOnClickListener {
                findNavController().navigate(MainFragmentDirections.actionMainFragmentToReservationAddFragment(ReservationType.FACILITY)) }
            mainHomeNoticeAllButton.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_mainhomeNoticeFragment) }

            viewmodel.getNoticePostData().observe(viewLifecycleOwner){
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