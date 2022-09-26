package com.example.adminapp.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminapp.R
import com.example.adminapp.ui.base.BaseSessionFragment
import com.example.adminapp.data.model.ReservationType
import com.example.adminapp.data.model.PostDataInfo
import com.example.adminapp.databinding.FragmentMainhomeHomeBinding
import com.example.adminapp.ui.main.MainFragmentDirections
import com.example.adminapp.ui.main.community.CommunityViewModel

class HomeFragment : BaseSessionFragment<FragmentMainhomeHomeBinding, CommunityViewModel>(){
    override lateinit var viewbinding: FragmentMainhomeHomeBinding
    override val viewmodel: CommunityViewModel by viewModels()
    private val homeViewModel : HomeViewModel by viewModels()

    private lateinit var homeNoticeRecyclerAdapter: HomeNoticeRecyclerAdapter
    private lateinit var homePreviewPhotoCardRecyclerAdapter: HomePreviewPhotoCardRecyclerAdapter
    private var homeNoticeItem : ArrayList<PostDataInfo> = arrayListOf()

    private lateinit var toCollectionBundle : Bundle

    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentMainhomeHomeBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        initMainHomeNoticeRecyclerView()
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.getNoticePostData().observe(viewLifecycleOwner){
            homeNoticeItem = it
            homeNoticeRecyclerAdapter.notifyDataSetChanged()
            initMainHomeNoticeRecyclerView()
            initMainHomePhotoCardRV()
        }
        homeViewModel.getUserPhotoCardDataList().observe(viewLifecycleOwner){
            homePreviewPhotoCardRecyclerAdapter.submitList(it)
        }
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
            mainHomeToSuggestCommunity.setOnClickListener {
                toCollectionBundle = bundleOf( "getCollectionName" to "3_SUGGEST")
                findNavController().navigate(R.id.action_mainFragment_to_communityPreviewFragment, toCollectionBundle)
            }
            mainHomeToWithCommunity.setOnClickListener {
                toCollectionBundle = bundleOf( "getCollectionName" to "4_WITH")
                findNavController().navigate(R.id.action_mainFragment_to_communityPreviewFragment, toCollectionBundle)
            }
            mainHomeToMarketCommunity.setOnClickListener {
                toCollectionBundle = bundleOf( "getCollectionName" to "2_EMERGENCY")
                findNavController().navigate(R.id.action_mainFragment_to_communityPreviewFragment, toCollectionBundle)
            }
            mainHomeNoticeAllButton.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_mainhomeNoticeFragment)
            }
            mainHomePhotoCardAllButton.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_homePhotoCardFragment)
            }
        }
    }
    private fun initMainHomeNoticeRecyclerView(){
        viewbinding.run {
            homeNoticeRecyclerAdapter = HomeNoticeRecyclerAdapter(homeNoticeItem)
            mainHomeNoticeNoticeRecycler.run {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                adapter = homeNoticeRecyclerAdapter
            }
        }
    }
    private fun initMainHomePhotoCardRV(){
        viewbinding.run{
            homePreviewPhotoCardRecyclerAdapter = HomePreviewPhotoCardRecyclerAdapter()
            viewbinding.mainHomePhotoCardRv.adapter = homePreviewPhotoCardRecyclerAdapter
        }
    }
}