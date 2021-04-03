package com.example.gongsanggongsangAdmin.First

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gongsanggongsangAdmin.R

class FirstFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater.inflate(R.layout.fragment_home_first, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val toolbar = view.findViewById<Toolbar>(R.id.tab_1_toolbar)
        //val appBarConfig = AppBarConfiguration(navController.graph)
        //toolbar.setupWithNavController(navController, appBarConfig)
        //val navController = navHostFragment.navController

        //val navHostFragment = childFragmentManager.findFragmentById(R.id.nav_host_first) as NavHostFragment? ?: return

        //findNavController().navigate(R.id.first)
    }

}


