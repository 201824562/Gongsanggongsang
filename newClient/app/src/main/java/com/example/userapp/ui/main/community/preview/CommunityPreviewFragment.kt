package com.example.userapp.ui.main.community.preview

import android.os.Bundle
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
import com.example.userapp.data.model.PostDataInfo
import com.example.userapp.databinding.FragmentCommunityPreviewBinding
import com.example.userapp.ui.main.community.CommunityViewModel

class CommunityPreviewFragment : BaseFragment<FragmentCommunityPreviewBinding, CommunityViewModel>() {
    override lateinit var viewbinding: FragmentCommunityPreviewBinding

    override val viewmodel : CommunityViewModel by viewModels()

    private lateinit var communityPreviewRecyclerAdapter: CommunityPreviewRecyclerAdapter
    private lateinit var communityPreviewMarketRecyclerAdapter: CommunityPreviewMarketRecyclerAdapter

    private lateinit var collectionName : String
    private lateinit var collectionNameBundle : Bundle
    var agency = ""
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
            "colletion_name" to collectionName
        )
        var ac = activity as MainActivity
        agency = ac.getUserData()!!.agency
        ac.selectedItems.clear()
        viewmodel.deletePostPhoto()
        when(collectionName){
            "1_free" -> viewbinding.previewToolbarName.text = "자유게시판"
            "2_emergency" -> viewbinding.previewToolbarName.text = "긴급게시판"
            "3_suggest" -> viewbinding.previewToolbarName.text = "건의게시판"
            "4_with" -> viewbinding.previewToolbarName.text = "함께게시판"
            "5_market" -> viewbinding.previewToolbarName.text = "장터게시판"
        }
        if(collectionName == "5_market"){
            initMarketRecyclerView()
        }
        else{
            initMarketElseRecyclerView()
        }
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {

    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.run{
            previewWriteRegisterButton.setOnClickListener{
                when(collectionName){
                    "1_free" -> findNavController().navigate(R.id.action_communityPreview_to_communityWriteFree, collectionNameBundle)
                    "2_emergency" -> findNavController().navigate(R.id.action_communityPreview_to_communityWriteEmergency, collectionNameBundle)
                    "3_suggest" -> findNavController().navigate(R.id.action_communityPreview_to_communityWriteSuggest, collectionNameBundle)
                    "4_with" -> findNavController().navigate(R.id.action_communityPreview_to_communityWriteWith, collectionNameBundle)
                    "5_market" -> findNavController().navigate(R.id.action_communityPreview_to_communityWriteMarket, collectionNameBundle)
                }
            }
            previewSearchButton.setOnClickListener {

            }
            previewBackButton.setOnClickListener {

            }
        }

    }
    fun initMarketElseRecyclerView(){
        viewmodel.getCollectionPostData(agency, collectionName).observe(viewLifecycleOwner){ it
            viewbinding.communityPreviewRecyclerView.run {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                adapter = CommunityPreviewRecyclerAdapter(it)
            }
            communityPreviewRecyclerAdapter = CommunityPreviewRecyclerAdapter(it)
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
    }
    fun initMarketRecyclerView(){
        var list : ArrayList<PostDataInfo> = ArrayList()
        viewbinding.communityPreviewRecyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = CommunityPreviewMarketRecyclerAdapter(list)
        }
        viewmodel.getCollectionPostData(agency, collectionName).observe(viewLifecycleOwner){ it
            list.clear()
            list.addAll(it)
            communityPreviewMarketRecyclerAdapter = CommunityPreviewMarketRecyclerAdapter(it)
            viewbinding.communityPreviewRecyclerView.adapter = communityPreviewMarketRecyclerAdapter.apply {
                listener =
                    object :
                        CommunityPreviewMarketRecyclerAdapter.OnCommunityMarketItemClickListener {
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
            communityPreviewMarketRecyclerAdapter.notifyDataSetChanged()
        }
    }

}