package com.example.userapp.ui.main.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.userapp.R
import com.example.userapp.base.BaseSessionFragment
import com.example.userapp.data.model.PostDataInfo
import com.example.userapp.databinding.FragmentSettingsOutBinding
import com.example.userapp.ui.main.community.CommunityViewModel
import com.example.userapp.ui.main.community.preview.CommunityPreviewMarketRecyclerAdapter

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

    override fun initDataBinding(savedInstanceState: Bundle?) {

    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.run {
            previewWriteRegisterButton.setOnClickListener { findNavController().navigate(R.id.action_settingsOut_to_settingsOutWrite) }
        }
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