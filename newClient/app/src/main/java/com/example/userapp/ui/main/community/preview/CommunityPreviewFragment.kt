package com.example.userapp.ui.main.community.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.userapp.R
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

    private val getArgs : CommunityPreviewFragmentArgs by navArgs()
    private lateinit var getCollection : String
    private lateinit var toCollectionBundle : Bundle


    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       viewbinding = FragmentCommunityPreviewBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        getCollection = getArgs.getCollectionName
        toCollectionBundle = bundleOf( "getCollectionName" to getCollection)

        when(getCollection){
            "1_FREE" -> viewbinding.previewToolbarName.text = "자유게시판"
            "2_EMERGENCY" -> viewbinding.previewToolbarName.text = "긴급게시판"
            "3_SUGGEST" -> viewbinding.previewToolbarName.text = "건의게시판"
            "4_WITH" -> viewbinding.previewToolbarName.text = "함께게시판"
            "5_MARKET" -> viewbinding.previewToolbarName.text = "장터게시판"
        }

        when(getCollection){
            "5_MARKET" -> initMarketRecyclerView()
            else -> initMarketElseRecyclerView()
        }
    }

    override fun onDetach() {
        super.onDetach()
        viewmodel.initCategoryPostData()
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.getPostDataInCategory(getCollection).observe(viewLifecycleOwner){
            when(getCollection){
                "5_MARKET" -> {
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

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.run{
            previewWriteRegisterButton.setOnClickListener{
                when(getCollection){
                    "5_MARKET" -> findNavController().navigate(R.id.action_communityPreview_to_communityWriteMarket, toCollectionBundle)
                    else -> findNavController().navigate(R.id.action_communityPreview_to_communityWrite, toCollectionBundle)
                }
            }
            previewSearchButton.setOnClickListener {
                findNavController().navigate(R.id.action_communityPreview_communitySearch, toCollectionBundle)
            }
            previewBackButton.setOnClickListener {
                findNavController().navigate(R.id.action_communityPreview_pop)
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