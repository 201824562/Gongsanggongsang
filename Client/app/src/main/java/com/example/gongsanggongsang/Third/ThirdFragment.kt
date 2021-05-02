package com.example.gongsanggongsang.Third

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.gongsanggongsang.R
import kotlinx.android.synthetic.main.fragment_home_third.*

class ThirdFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home_third, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var collection_name : String = ""

        community_market_button.setOnClickListener{
            collection_name = "COMMUNITY_Market"
            var bundle = bundleOf(
                "collection_name" to collection_name
            )
            findNavController().navigate(R.id.action_baseFragment_to_communityPreviewFragment, bundle)
        }
        community_with_button.setOnClickListener{
            collection_name = "COMMUNITY_With"
            var bundle = bundleOf(
                "collection_name" to collection_name
            )
            findNavController().navigate(R.id.action_baseFragment_to_communityPreviewFragment, bundle)
        }
        community_suggest_button.setOnClickListener{
            collection_name = "COMMUNITY_Suggest"
            var bundle = bundleOf(
                "collection_name" to collection_name
            )
            findNavController().navigate(R.id.action_baseFragment_to_communityPreviewFragment, bundle)
        }
    }

}