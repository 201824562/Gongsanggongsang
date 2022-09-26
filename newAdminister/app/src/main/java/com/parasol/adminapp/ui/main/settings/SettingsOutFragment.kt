package com.parasol.adminapp.ui.main.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.parasol.adminapp.R
import com.parasol.adminapp.ui.base.BaseSessionFragment
import com.parasol.adminapp.data.model.PostDataInfo
import com.parasol.adminapp.databinding.FragmentSettingsOutBinding
import com.parasol.adminapp.ui.main.community.CommunityViewModel
import com.parasol.adminapp.ui.main.community.preview.CommunityPreviewMarketRecyclerAdapter

class SettingsOutFragment : BaseSessionFragment<FragmentSettingsOutBinding, CommunityViewModel>() {
    override lateinit var viewbinding : FragmentSettingsOutBinding

    override val viewmodel: CommunityViewModel by viewModels()
    private var settingsOutLog : ArrayList<PostDataInfo> = arrayListOf()
    private lateinit var settingsOutLogRecyclerAdapter: SettingsOutLogRecyclerAdapter
    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentSettingsOutBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        viewmodel.getPostDataInCategory("OUT").observe(viewLifecycleOwner){
            settingsOutLog = it
            initSettingsLogRecycler()
        }
    }

    override fun onDetach() {
        super.onDetach()
        viewmodel.initCategoryPostData()
    }
    override fun initDataBinding(savedInstanceState: Bundle?) {

    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.previewBackButton.setOnClickListener { findNavController().navigate(R.id.action_settingsOutFragment_pop) }
    }
    private fun initSettingsLogRecycler(){
        viewbinding.communityPreviewRecyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = CommunityPreviewMarketRecyclerAdapter(settingsOutLog)
        }
        settingsOutLogRecyclerAdapter = SettingsOutLogRecyclerAdapter(settingsOutLog)
        viewbinding.communityPreviewRecyclerView.adapter = settingsOutLogRecyclerAdapter.apply {
            listener = object : SettingsOutLogRecyclerAdapter.OnCommunityMarketItemClickListener {
                override fun onPreviewItemClick(position: Int) {
                    var postItemDataInfo : PostDataInfo = getItem(position)
                    var bundle = bundleOf(
                        "post_data_info" to postItemDataInfo
                    )
                    findNavController().navigate(R.id.action_settingOut_to_settingOutPost, bundle)
                }
            }
        }
    }
}