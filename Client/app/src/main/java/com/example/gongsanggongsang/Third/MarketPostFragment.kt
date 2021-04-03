package com.example.gongsanggongsang.Third

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gongsanggongsang.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_community_market_post.*

class MarketPostFragment : Fragment() {
    var database = FirebaseFirestore.getInstance()
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_community_market_post, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var title = arguments?.getString("key").toString()
        database
            .collection("COMMUNITY_Market")
            .document(title)
            .get()
            .addOnSuccessListener { result ->
                market_original_post_title.setText(result["title"].toString())
                market_original_post_contents.setText(result["contents"].toString())
            }
        //val navHostFragment = childFragmentManager.findFragmentById(Rt.id.nav_host_fourth) as NavHostFragment? ?: return
        //findNavController().navigate(R.id.fourth)
    }

}
