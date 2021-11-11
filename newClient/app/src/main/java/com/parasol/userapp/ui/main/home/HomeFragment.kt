package com.parasol.userapp.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.parasol.userapp.R
import com.parasol.userapp.ui.base.BaseSessionFragment
import com.parasol.userapp.data.model.PostDataInfo
import com.parasol.userapp.databinding.FragmentMainhomeHomeBinding
import com.parasol.userapp.ui.main.community.CommunityViewModel

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
        initMainHomePhotoCardRV()
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.getNoticePostData().observe(viewLifecycleOwner){
            homeNoticeItem = it
            homeNoticeRecyclerAdapter.notifyDataSetChanged()
            initMainHomeNoticeRecyclerView()
        }
        homeViewModel.getUserPhotoCardDataList().observe(viewLifecycleOwner){
            homePreviewPhotoCardRecyclerAdapter.submitList(it)
        }
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewmodel.getUserInfo()
        viewbinding.run {
            mainHomeToEquipmentReservation.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_reservationEquipmentFragment)
            }
            mainHomeToFacilityReservation.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_reservationFacilityFragment)
            }
            mainHomeToSuggestCommunity.setOnClickListener {
                toCollectionBundle = bundleOf( "getCollectionName" to "3_SUGGEST")
                findNavController().navigate(R.id.action_mainFragment_to_communityPreviewFragment, toCollectionBundle)
            }
            mainHomeToWithCommunity.setOnClickListener {
                toCollectionBundle = bundleOf( "getCollectionName" to "4_WITH")
                findNavController().navigate(R.id.action_mainFragment_to_communityPreviewFragment, toCollectionBundle)
            }
            mainHomeToMarketCommunity.setOnClickListener {
                toCollectionBundle = bundleOf( "getCollectionName" to "5_MARKET")
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
