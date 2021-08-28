package com.example.adminapp.ui.main.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.adminapp.R
import com.example.adminapp.base.BaseFragment
import com.example.adminapp.base.BaseSessionFragment
import com.example.adminapp.databinding.FragmentMainhomeCommunityBinding

class CommunityMainFragment : BaseSessionFragment<FragmentMainhomeCommunityBinding, CommunityViewModel>(){
    override lateinit var viewbinding: FragmentMainhomeCommunityBinding

    override val viewmodel: CommunityViewModel by viewModels()

    lateinit var collection_name_bundle : Bundle

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
            var collection_name : String
            communityMarketButton.setOnClickListener {
                collection_name = "5_market"
                collection_name_bundle = bundleOf(
                    "collection_name" to collection_name
                )
                findNavController().navigate(R.id.action_mainFragment_to_communityPreviewFragment, collection_name_bundle)
            }
            communityWithButton.setOnClickListener {
                collection_name = "4_with"
                collection_name_bundle = bundleOf(
                    "collection_name" to collection_name
                )
                findNavController().navigate(R.id.action_mainFragment_to_communityPreviewFragment, collection_name_bundle)
            }
            communitySuggestButton.setOnClickListener {
                collection_name = "3_suggest"
                collection_name_bundle = bundleOf("collection_name" to collection_name)
                findNavController().navigate(R.id.action_mainFragment_to_communityPreviewFragment, collection_name_bundle)
            }
            communityEmergencyButton.setOnClickListener {
                collection_name = "2_emergency"
                collection_name_bundle = bundleOf("collection_name" to collection_name)
                findNavController().navigate(R.id.action_mainFragment_to_communityPreviewFragment, collection_name_bundle)

            }
            communityFreeButton.setOnClickListener{
                collection_name = "1_free"
                collection_name_bundle = bundleOf("collection_name" to collection_name)
                findNavController().navigate(R.id.action_mainFragment_to_communityPreviewFragment, collection_name_bundle)

            }
        }
    }
}