package com.example.gongsanggongsangAdmin.Fifth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.gongsanggongsangAdmin.R
import com.example.gongsanggongsangAdmin.data.UserDataClass
import kotlinx.android.synthetic.main.fragment_home_fifth.*

class FifthFragment : Fragment() {
    private val viewModel : AdministerViewmodel by viewModels()
    var userlist : List<UserDataClass> = listOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_home_fifth, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cardview.setOnClickListener {
            findNavController().navigate(R.id.action_baseFragment_to_userListFragment)
        }

    }
}