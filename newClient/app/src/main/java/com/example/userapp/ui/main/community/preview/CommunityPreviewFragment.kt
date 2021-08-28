package com.example.userapp.ui.main.community.preview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.userapp.MainActivity
import com.example.userapp.R
import com.example.userapp.base.BaseFragment
import com.example.userapp.base.BaseSessionFragment
import com.example.userapp.data.model.PostDataInfo
import com.example.userapp.databinding.FragmentCommunityPreviewBinding
import com.example.userapp.ui.main.community.CommunityViewModel

class CommunityPreviewFragment : BaseSessionFragment<FragmentCommunityPreviewBinding, CommunityViewModel>() {
    override lateinit var viewbinding: FragmentCommunityPreviewBinding

    override val viewmodel : CommunityViewModel by viewModels()

    private lateinit var communityPreviewRecyclerAdapter: CommunityPreviewRecyclerAdapter
    private var communityPreviewItem = arrayListOf<PostDataInfo>()
    private lateinit var communityPreviewMarketRecyclerAdapter: CommunityPreviewMarketRecyclerAdapter
    private var communityPreviewMarketItem = arrayListOf<PostDataInfo>()

    private lateinit var collectionName : String
    private lateinit var collectionNameBundle : Bundle
    private var userAgency = ""

    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       viewbinding = FragmentCommunityPreviewBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        collectionName= arguments?.getString("collection_name").toString()
        collectionNameBundle = bundleOf(
            "collection_name" to collectionName
        )
        viewmodel.getUserInfo()

        when(collectionName){
            "1_free" -> viewbinding.previewToolbarName.text = "자유게시판"
            "2_emergency" -> viewbinding.previewToolbarName.text = "긴급게시판"
            "3_suggest" -> viewbinding.previewToolbarName.text = "건의게시판"
            "4_with" -> viewbinding.previewToolbarName.text = "함께게시판"
            "5_market" -> viewbinding.previewToolbarName.text = "장터게시판"
        }

        //TODO: notifyset 안 먹어서 리사이클러뷰 계속 초기화 됨.
        when(collectionName){
            "5_market" -> initMarketRecyclerView()
            else -> initMarketElseRecyclerView()
        }
        viewmodel.getCategoryAllPostData(collectionName).observe(viewLifecycleOwner){
            when(collectionName){
                "5_market" -> {
                    communityPreviewMarketItem = it
                    initMarketRecyclerView()
                    communityPreviewMarketRecyclerAdapter.notifyDataSetChanged()
                }
                else -> {
                    communityPreviewItem = it
                    initMarketElseRecyclerView()
                    communityPreviewRecyclerAdapter.notifyDataSetChanged()
                }
            }

        }
    }

    override fun onDetach() {
        super.onDetach()
        viewmodel.initCategoryPostData()
    }
    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.onSuccessGettingUserInfo.observe(this, {
            userAgency = it.agency
        })
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.run{
            previewWriteRegisterButton.setOnClickListener{
                when(collectionName){
                    "5_market" -> findNavController().navigate(R.id.action_communityPreview_to_communityWriteMarket, collectionNameBundle)
                    else -> findNavController().navigate(R.id.action_communityPreview_to_communityWrite, collectionNameBundle)
                }
            }
            previewSearchButton.setOnClickListener {
                findNavController().navigate(R.id.action_communityPreview_communitySearch, collectionNameBundle)
            }
            previewBackButton.setOnClickListener {
                findNavController().navigate(R.id.action_communityPreviewFragment_self)
            }
        }

    }

    private fun initMarketElseRecyclerView(){
        viewbinding.communityPreviewRecyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = CommunityPreviewRecyclerAdapter(communityPreviewItem)
        }
        communityPreviewRecyclerAdapter = CommunityPreviewRecyclerAdapter(communityPreviewItem)
        viewbinding.communityPreviewRecyclerView.adapter = communityPreviewRecyclerAdapter.apply {
            listener =
                object : CommunityPreviewRecyclerAdapter.OnCommunityMarketItemClickListener {
                    override fun onPreviewItemClick(position: Int) {
                        var postItemDataInfo : PostDataInfo = getItem(position)
                        var bundle = bundleOf(
                            "post_data_info" to postItemDataInfo
                        )
                        findNavController().navigate(R.id.action_communityPreview_to_communityPost, bundle)
                    }
                }
        }
        communityPreviewRecyclerAdapter.notifyDataSetChanged()
    }

    private fun initMarketRecyclerView(){
        viewbinding.communityPreviewRecyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = CommunityPreviewMarketRecyclerAdapter(communityPreviewMarketItem)
        }
        communityPreviewMarketRecyclerAdapter = CommunityPreviewMarketRecyclerAdapter(communityPreviewMarketItem)
        viewbinding.communityPreviewRecyclerView.adapter = communityPreviewMarketRecyclerAdapter.apply {
            listener = object : CommunityPreviewMarketRecyclerAdapter.OnCommunityMarketItemClickListener {
                    override fun onPreviewItemClick(position: Int) {
                        var postItemDataInfo : PostDataInfo = getItem(position)
                        var bundle = bundleOf(
                            "post_data_info" to postItemDataInfo
                        )
                        findNavController().navigate(R.id.action_communityPreview_to_communityPostMarket, bundle)
                    }
                }
        }
    }

}