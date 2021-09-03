package com.example.userapp.ui.main.home

import android.annotation.SuppressLint
import com.example.userapp.databinding.FragmentMainhomeHomeNoticeBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.userapp.MainActivity
import com.example.userapp.R
import com.example.userapp.base.BaseFragment
import com.example.userapp.base.BaseSessionFragment
import com.example.userapp.data.model.PostDataInfo
import com.example.userapp.ui.main.community.CommunityViewModel
import com.example.userapp.ui.main.community.preview.CommunityPreviewRecyclerAdapter

class HomeNoticeFragment : BaseSessionFragment<FragmentMainhomeHomeNoticeBinding, CommunityViewModel>(){
    override lateinit var viewbinding: FragmentMainhomeHomeNoticeBinding
    override val viewmodel: CommunityViewModel by viewModels()
    private lateinit var noticePreviewRecyclerAdapter: CommunityPreviewRecyclerAdapter
    private var noticePreviewItem : ArrayList<PostDataInfo> = arrayListOf()
    private lateinit var collectionNameBundle : Bundle

    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentMainhomeHomeNoticeBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        collectionNameBundle = bundleOf("collection_name" to "notice")
        initNoticeRecyclerView()
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
    }

    @SuppressLint("ResourceAsColor")
    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewmodel.getNoticePostData().observe(viewLifecycleOwner){ it
            noticePreviewItem = it
            initNoticeRecyclerView()
            noticePreviewRecyclerAdapter.notifyDataSetChanged()
        }
        viewbinding.run {
            previewBackButton.setOnClickListener {
                findNavController().navigate(R.id.action_mainNotice_pop)
            }
            previewSearchButton.setOnClickListener {
                findNavController().navigate(R.id.action_communityNotice_to_communitySearch, collectionNameBundle)
            }
            mainhomeNoticeShowAllButton.isSelected = true
            mainhomeNoticeShowAllButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.applemint))
            mainhomeNoticeShowAllButton.setOnClickListener {
                makeButtonsUnselected()
                it.isSelected = true
                mainhomeNoticeShowAllButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.applemint))
                viewmodel.getNoticePostData().observe(viewLifecycleOwner){
                    noticePreviewItem = it
                    initNoticeRecyclerView()
                    noticePreviewRecyclerAdapter.notifyDataSetChanged()
                }
            }
            mainhomeNoticeShowNoticeButton.setOnClickListener {
                makeButtonsUnselected()
                it.isSelected = true
                mainhomeNoticeShowNoticeButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.applemint))
                viewmodel.getNoticeCategoryPostData("공지").observe(viewLifecycleOwner){
                    noticePreviewItem = it
                    initNoticeRecyclerView()
                    noticePreviewRecyclerAdapter.notifyDataSetChanged()
                }
            }
            mainhomeNoticeShowEventButton.setOnClickListener {
                makeButtonsUnselected()
                it.isSelected = true
                mainhomeNoticeShowEventButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.applemint))
                viewmodel.getNoticeCategoryPostData("행사").observe(viewLifecycleOwner){
                    noticePreviewItem = it
                    initNoticeRecyclerView()
                    noticePreviewRecyclerAdapter.notifyDataSetChanged()
                }

            }
            mainhomeNoticeShowEtcButton.setOnClickListener {
                makeButtonsUnselected()
                it.isSelected = true
                mainhomeNoticeShowEtcButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.applemint))
                viewmodel.getNoticeCategoryPostData("기타").observe(viewLifecycleOwner){
                    noticePreviewItem = it
                    initNoticeRecyclerView()
                    noticePreviewRecyclerAdapter.notifyDataSetChanged()
                }
            }
        }

    }
    fun initNoticeRecyclerView(){
        viewbinding.mainhomeNoticeRecyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = CommunityPreviewRecyclerAdapter(noticePreviewItem)
        }
        noticePreviewRecyclerAdapter = CommunityPreviewRecyclerAdapter(noticePreviewItem)
        viewbinding.mainhomeNoticeRecyclerView.adapter = noticePreviewRecyclerAdapter.apply {
            listener =
                object : CommunityPreviewRecyclerAdapter.OnCommunityMarketItemClickListener {
                    override fun onPreviewItemClick(position: Int) {
                        var postItemDataInfo : PostDataInfo = getItem(position)
                        var bundle = bundleOf(
                            "post_data_info" to postItemDataInfo
                        )
                        findNavController().navigate(R.id.action_mainhomeNoticeFragment_to_noticePostFragment, bundle)
                    }
                }
        }
        noticePreviewRecyclerAdapter.notifyDataSetChanged()
    }
    private fun makeButtonsUnselected(){
        viewbinding.run {
            mainhomeNoticeShowAllButton.apply {
                isSelected = false
                setTextColor(ContextCompat.getColor(context, R.color.black_20)) }
            mainhomeNoticeShowNoticeButton.apply {
                isSelected = false
                setTextColor(ContextCompat.getColor(context, R.color.black_20)) }
            mainhomeNoticeShowEventButton.apply {
                isSelected = false
                setTextColor(ContextCompat.getColor(context, R.color.black_20)) }
            mainhomeNoticeShowEtcButton.apply {
                isSelected = false
                setTextColor(ContextCompat.getColor(context, R.color.black_20)) }
        }
    }
}