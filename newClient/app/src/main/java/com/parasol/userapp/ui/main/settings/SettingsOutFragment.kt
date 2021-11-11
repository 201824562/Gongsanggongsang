package com.parasol.userapp.ui.main.settings

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.parasol.userapp.MainActivity
import com.parasol.userapp.R
import com.parasol.userapp.data.entity.UnableTimeList
import com.parasol.userapp.ui.base.BaseSessionFragment
import com.parasol.userapp.data.model.PostDataInfo
import com.parasol.userapp.data.model.ReservationFacility
import com.parasol.userapp.databinding.FragmentSettingsOutBinding
import com.parasol.userapp.ui.main.MainFragmentDirections
import com.parasol.userapp.ui.main.community.CommunityViewModel
import com.parasol.userapp.ui.main.community.preview.CommunityPreviewMarketRecyclerAdapter

class SettingsOutFragment : BaseSessionFragment<FragmentSettingsOutBinding, SettingsViewModel>() {
    override lateinit var viewbinding : FragmentSettingsOutBinding
    override val viewmodel: SettingsViewModel by viewModels()
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
            Log.e("test","1111")
        }
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {

    }
    
    @RequiresApi(Build.VERSION_CODES.O)
    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewmodel.getinitRefTimeData()
        viewbinding.run {
            previewWriteRegisterButton.setOnClickListener {
                findNavController().navigate(SettingsOutFragmentDirections.actionSettingsOutFragmentToSettingsOutReserveFragment(
                    ReservationFacility(
                        "Out",
                        0,
                        10,
                        10,
                        true,
                        emptyList()
                    )
                ) )
//                findNavController().navigate(R.id.action_settingsOut_to_settingsOutWrite)
            }
            previewBackButton.setOnClickListener { findNavController().navigate(R.id.action_settingsOutFragment_pop) }
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