package com.example.adminapp.ui.main.community.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminapp.R
import com.example.adminapp.base.BaseFragment
import com.example.adminapp.data.model.PostDataInfo
import com.example.adminapp.databinding.FragmentCommunityPreviewBinding
import com.example.adminapp.ui.main.community.CommunityViewModel


class CommunityPreviewFragment : BaseFragment<FragmentCommunityPreviewBinding, CommunityViewModel>() {
    override lateinit var viewbinding: FragmentCommunityPreviewBinding

    override val viewmodel : CommunityViewModel by viewModels()

    private lateinit var communityPreviewRecyclerAdapter: CommunityPreviewRecyclerAdapter
    private lateinit var communityPreviewMarketRecyclerAdapter: CommunityPreviewMarketRecyclerAdapter

    private lateinit var collection_name : String
    private lateinit var collection_name_bundle : Bundle


    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       viewbinding = FragmentCommunityPreviewBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        collection_name= arguments?.getString("collection_name").toString()
        val test = arrayListOf<String>().toTypedArray()

        collection_name_bundle = bundleOf(
            "uriArray" to test,
        )
        if(collection_name.equals("5_market")){
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
                when(collection_name){
                    "1_free" -> findNavController().navigate(R.id.action_communityPreview_to_communityWriteFree, collection_name_bundle)
                    "2_emergency" -> findNavController().navigate(R.id.action_communityPreview_to_communityWriteEmergency, collection_name_bundle)
                    "3_suggest" -> findNavController().navigate(R.id.action_communityPreview_to_communityWriteSuggest, collection_name_bundle)
                    "4_with" -> findNavController().navigate(R.id.action_communityPreview_to_communityWriteWith, collection_name_bundle)
                    "5_market" -> findNavController().navigate(R.id.action_communityPreview_to_communityWriteMarket, collection_name_bundle)
                }
            }
            previewSearchButton.setOnClickListener {

            }
            previewBackButton.setOnClickListener {

            }
        }

    }
    fun initMarketElseRecyclerView(){
        var list : ArrayList<PostDataInfo> = ArrayList()

        /*viewbinding.communityPreviewRecyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = CommunityPreviewRecyclerAdapter(list)
        }*/
        viewmodel.getCollectionPostData(collection_name).observe(viewLifecycleOwner){ it
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
                            var document_name = getItem(position).post_id
                            var bundle = bundleOf(
                                "collection_name" to collection_name,
                                "document_name" to document_name
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
        viewmodel.getCollectionPostData(collection_name).observe(viewLifecycleOwner){ it
            list.clear()
            list.addAll(it)
            communityPreviewMarketRecyclerAdapter = CommunityPreviewMarketRecyclerAdapter(it)
            viewbinding.communityPreviewRecyclerView.adapter = communityPreviewMarketRecyclerAdapter.apply {
                listener =
                    object :
                        CommunityPreviewMarketRecyclerAdapter.OnCommunityMarketItemClickListener {
                        override fun onPreviewItemClick(position: Int) {
                            var document_name = getItem(position).post_id
                            var bundle = bundleOf(
                                "collection_name" to collection_name,
                                "document_name" to document_name
                            )
                            findNavController().navigate(R.id.action_communityPreview_to_communityPostMarket, bundle)
                        }
                    }
            }
            communityPreviewMarketRecyclerAdapter.notifyDataSetChanged()
        }
    }

}