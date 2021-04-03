package com.example.gongsanggongsangAdmin.Third

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gongsanggongsangAdmin.R

class WithPostFragment : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_community_with_post, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val navHostFragment = childFragmentManager.findFragmentById(R.id.nav_host_fourth) as NavHostFragment? ?: return
        //findNavController().navigate(R.id.fourth)
    }

}
