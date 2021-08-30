package com.example.adminapp.ui.main.settings

import android.os.Bundle
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
import com.example.adminapp.databinding.FragmentSettingsOutBinding
import com.example.adminapp.ui.main.community.CommunityViewModel
import com.example.adminapp.ui.main.community.preview.CommunityPreviewMarketRecyclerAdapter

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