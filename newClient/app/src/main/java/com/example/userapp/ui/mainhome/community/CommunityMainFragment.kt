package com.example.userapp.ui.mainhome.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.userapp.R
import com.example.userapp.base.BaseFragment
import com.example.userapp.databinding.FragmentMainhomeCommunityBinding

class CommunityMainFragment : BaseFragment<FragmentMainhomeCommunityBinding, CommunityViewModel>(){
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
                collection_name = "COMMUNITY_MARKET"
                collection_name_bundle = bundleOf(
                    "collection_name" to collection_name
                )
                findNavController().navigate(R.id.action_mainFragment_to_communityPreviewFragment, collection_name_bundle)
            }
            communityWithButton.setOnClickListener {
                collection_name = "COMMUNITY_WITH"
                collection_name_bundle = bundleOf(
                    "collection_name" to collection_name
                )
                findNavController().navigate(R.id.action_mainFragment_to_communityPreviewFragment, collection_name_bundle)
            }
            communityTendinousButton.setOnClickListener {
                collection_name = "COMMUNITY_SUGGEST"
                collection_name_bundle = bundleOf(
                    "collection_name" to collection_name
                )
                findNavController().navigate(R.id.action_mainFragment_to_communityPreviewFragment, collection_name_bundle)
            }
        }
    }
}