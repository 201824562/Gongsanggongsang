package com.example.adminapp.ui.main.community.preview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminapp.R
import com.example.adminapp.base.BaseSessionFragment
import com.example.adminapp.data.model.PostDataInfo
import com.example.adminapp.databinding.FragmentCommunitySearchBinding
import com.example.adminapp.ui.main.community.CommunityViewModel
import com.example.adminapp.utils.hideKeyboard

class CommunitySearchFragment : BaseSessionFragment<FragmentCommunitySearchBinding, CommunityViewModel>(){
    override lateinit var viewbinding: FragmentCommunitySearchBinding

    override val viewmodel: CommunityViewModel by viewModels()

    private lateinit var communityPreviewRecyclerAdapter : CommunityPreviewRecyclerAdapter
    private var communityPreviewItem : ArrayList<PostDataInfo> = arrayListOf()
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
        collectionName = arguments?.getString("getCollectionName").toString()

    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
    }

    override fun onDetach() {
        super.onDetach()
        viewmodel.initCategoryPostData()
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.run{
            searchBackButton.setOnClickListener {
                findNavController().navigate(R.id.action_communitySearch_pop)
            }
            searchCompleteButton.setOnClickListener {
                if(searchKeyword.text.isNotEmpty()){
                    viewmodel.getSearchPostData(collectionName, searchKeyword.text.toString()).observe(viewLifecycleOwner){
                        Log.e("checkck", "{$it}")
                        searchKeyword.setText("")
                        hideKeyboard(viewbinding.root)
                        if(it.isEmpty()) { searchNoResultPage.visibility = View.VISIBLE }
                        else {
                            communityPreviewItem = it
                            communityPreviewRecyclerAdapter.notifyDataSetChanged()
                        }
                    }
                }
                else{
                    showToast("검색어를 입력해주세요.")
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
                        var postItemDataInfo : PostDataInfo = getItem(position)
                        var bundle = bundleOf(
                            "post_data_info" to postItemDataInfo
                        )
                        findNavController().navigate(R.id.action_communitySearch_to_communityPost, bundle)
                    }
                }
        }
        communityPreviewRecyclerAdapter.notifyDataSetChanged()
    }
}