package com.example.userapp.ui.main.home

import com.example.userapp.databinding.FragmentMainhomeHomeNoticeBinding
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
import com.example.userapp.ui.main.community.CommunityViewModel
import com.example.userapp.ui.main.community.preview.CommunityPreviewRecyclerAdapter

class HomeNoticeFragment : BaseFragment<FragmentMainhomeHomeNoticeBinding, CommunityViewModel>(){
    override lateinit var viewbinding: FragmentMainhomeHomeNoticeBinding
    override val viewmodel: CommunityViewModel by viewModels()
    private lateinit var noticePreviewRecyclerAdapter: CommunityPreviewRecyclerAdapter
    private var noticePreviewItem : ArrayList<PostDataInfo> = arrayListOf()
    var agency = ""
    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentMainhomeHomeNoticeBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        viewbinding.mainhomeNoticeRecyclerView.run {
            noticePreviewRecyclerAdapter = CommunityPreviewRecyclerAdapter(noticePreviewItem)
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = noticePreviewRecyclerAdapter
        }
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        val ac = activity as MainActivity
        agency = ac.getUserData()!!.agency
        viewmodel.getCollectionPostData(agency, "notice").observe(viewLifecycleOwner){ it
            noticePreviewItem = it
            noticePreviewRecyclerAdapter.notifyDataSetChanged()
            viewbinding.mainhomeNoticeRecyclerView.adapter = noticePreviewRecyclerAdapter.apply {
                listener =
                    object : CommunityPreviewRecyclerAdapter.OnCommunityMarketItemClickListener {
                        override fun onPreviewItemClick(position: Int) {
                            var collectionName = "notice"
                            var documentName = getItem(position).post_id
                            var bundle = bundleOf(
                                "collection_name" to collectionName,
                                "document_name" to documentName
                            )
                            findNavController().navigate(R.id.action_communityPreview_to_communityPost, bundle)
                        }
                    }
            }
            noticePreviewRecyclerAdapter.notifyDataSetChanged()
        }
        viewbinding.run {
            mainhomeNoticeShowAllButton.setOnClickListener {
                viewmodel.getCollectionPostData(agency, "notice").observe(viewLifecycleOwner){
                    noticePreviewItem = it
                    noticePreviewRecyclerAdapter.notifyDataSetChanged()
                }
            }
            mainhomeNoticeShowNoticeButton.setOnClickListener {
                viewmodel.getNoticeCategoryPostData(agency,"공지").observe(viewLifecycleOwner){
                    noticePreviewItem = it
                    noticePreviewRecyclerAdapter.notifyDataSetChanged()
                }
            }
            mainhomeNoticeShowEventButton.setOnClickListener {
                viewmodel.getNoticeCategoryPostData(agency,"행사").observe(viewLifecycleOwner){
                    noticePreviewItem = it
                    noticePreviewRecyclerAdapter.notifyDataSetChanged()
                }

            }
            mainhomeNoticeShowEtcButton.setOnClickListener {
                viewmodel.getNoticeCategoryPostData(agency,"기타").observe(viewLifecycleOwner){
                    noticePreviewItem = it
                    noticePreviewRecyclerAdapter.notifyDataSetChanged()
                }

            }
        }

    }
}