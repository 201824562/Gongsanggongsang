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
import com.example.adminapp.databinding.FragmentCommunitySearchBinding
import com.example.adminapp.ui.main.community.CommunityViewModel

class CommunitySearchFragment : BaseFragment<FragmentCommunitySearchBinding, CommunityViewModel>(){
    override lateinit var viewbinding: FragmentCommunitySearchBinding

    override val viewmodel: CommunityViewModel by viewModels()

    private lateinit var communityPreviewRecyclerAdapter : CommunityPreviewRecyclerAdapter
    private var communityPreviewItem : ArrayList<PostDataInfo> = arrayListOf()
    private lateinit var communityPreviewMarketRecyclerAdapter: CommunityPreviewMarketRecyclerAdapter
    private var communityPreviewMarketItem = arrayListOf<PostDataInfo>()

    private lateinit var collectionName : String
    private var userAgency : String = ""
    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentCommunitySearchBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        val ac = activity as MainActivity
        userAgency = ac.getAdminData()!!.agency
        collectionName = arguments?.getString("collection_name").toString()

    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.run{
            previewBackButton.setOnClickListener {
                findNavController().navigate(R.id.action_communitySearch_pop)
            }
            searchCompleteButton.setOnClickListener {
                viewmodel.getSearchPostData(userAgency, collectionName, searchKeyword.text.toString()).observe(viewLifecycleOwner){
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
                        findNavController().navigate(R.id.action_communitySearch_to_communityPost, bundle)
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