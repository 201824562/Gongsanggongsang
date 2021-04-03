package com.example.gongsanggongsang.Third

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gongsanggongsang.R
import kotlinx.android.synthetic.main.fragment_community_market.*
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

        community_market_button.setOnClickListener{
            findNavController().navigate(R.id.action_baseFragment_to_communityMarket)
        }
        community_with_button.setOnClickListener{
            findNavController().navigate(R.id.action_baseFragment_to_communityWith)
        }
        community_suggest_button.setOnClickListener{
            findNavController().navigate(R.id.action_baseFragment_to_communitySuggest)
        }
    }

}