package com.example.adminapp.ui.main.community.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminapp.MainActivity
import com.example.adminapp.R
import com.example.adminapp.base.BaseFragment
import com.example.adminapp.data.model.PostDataInfo
import com.example.adminapp.databinding.FragmentCommunityPreviewBinding
import com.example.adminapp.ui.main.community.CommunityViewModel

class CommunityPreviewFragment : BaseFragment<FragmentCommunityPreviewBinding, CommunityViewModel>() {
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

        var ac = activity as MainActivity
        userAgency = ac.getAdminData()!!.agency

        ac.selectedItems.clear()
        viewmodel.deletePostPhoto()

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
        viewmodel.getCategoryAllPostData(userAgency, collectionName).observe(viewLifecycleOwner){
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

    override fun initDataBinding(savedInstanceState: Bundle?) {

    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.run{
            previewBackButton.setOnClickListener {
                findNavController().navigate(R.id.action_communityPreviewFragment_pop)
            }
            previewWriteRegisterButton.setOnClickListener{
                when(collectionName){
                    "5_market" -> findNavController().navigate(R.id.action_communityPreview_to_communityWriteMarket, collectionNameBundle)
                    else -> findNavController().navigate(R.id.action_communityPreview_to_communityWrite, collectionNameBundle)
                }
            }
            previewSearchButton.setOnClickListener {
                findNavController().navigate(R.id.action_communityPreview_communitySearch, collectionNameBundle)
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
                        var documentName = getItem(position).post_id
                        var bundle = bundleOf(
                            "collection_name" to collectionName,
                            "document_name" to documentName
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
                        var documentName = getItem(position).post_id
                        var bundle = bundleOf(
                            "collection_name" to collectionName,
                            "document_name" to documentName
                        )
                        findNavController().navigate(R.id.action_communityPreview_to_communityPostMarket, bundle)
                    }
                }
        }
    }

}