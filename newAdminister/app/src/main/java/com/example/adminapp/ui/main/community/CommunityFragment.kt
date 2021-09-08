package com.example.adminapp.ui.main.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.adminapp.R
import com.example.adminapp.ui.base.BaseSessionFragment
import com.example.adminapp.databinding.FragmentMainhomeCommunityBinding

class CommunityFragment : BaseSessionFragment<FragmentMainhomeCommunityBinding, CommunityViewModel>(){
    override lateinit var viewbinding: FragmentMainhomeCommunityBinding

    override val viewmodel: CommunityViewModel by viewModels()

    private lateinit var toCollectionBundle : Bundle

    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentMainhomeCommunityBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.run {
            communityMarketButton.setOnClickListener {
                toCollectionBundle = bundleOf("getCollectionName" to "5_MARKET")
                findNavController().navigate(R.id.action_mainFragment_to_communityPreviewFragment, toCollectionBundle)
            }
            communityWithButton.setOnClickListener {
                toCollectionBundle = bundleOf("getCollectionName" to "4_WITH")
                findNavController().navigate(R.id.action_mainFragment_to_communityPreviewFragment, toCollectionBundle)
            }
            communitySuggestButton.setOnClickListener {
                toCollectionBundle = bundleOf("getCollectionName" to "3_SUGGEST")
                findNavController().navigate(R.id.action_mainFragment_to_communityPreviewFragment, toCollectionBundle)
            }
            communityEmergencyButton.setOnClickListener {
                toCollectionBundle = bundleOf("getCollectionName" to "2_EMERGENCY")
                findNavController().navigate(R.id.action_mainFragment_to_communityPreviewFragment, toCollectionBundle)
            }
            communityFreeButton.setOnClickListener{
                toCollectionBundle = bundleOf("getCollectionName" to "1_FREE")
                findNavController().navigate(R.id.action_mainFragment_to_communityPreviewFragment, toCollectionBundle)
            }
        }
    }
}