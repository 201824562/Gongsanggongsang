package com.example.gongsanggongsang.Second

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gongsanggongsang.R

class CommunalFacilityFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_communal_facility, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }
}