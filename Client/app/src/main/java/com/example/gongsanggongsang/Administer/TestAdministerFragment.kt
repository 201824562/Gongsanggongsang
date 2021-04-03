package com.example.gongsanggongsang.Administer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gongsanggongsang.data.UserDataClass
import com.example.gongsanggongsang.R
import kotlinx.android.synthetic.main.fragment_test_administer.*
import androidx.fragment.app.viewModels

class TestAdministerFragment : Fragment() {
    private val viewModel : AdministerViewmodel by viewModels()
    private lateinit var adapter: Administer_RCV
    var userlist : List<UserDataClass> = listOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_test_administer, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllusers().observe(viewLifecycleOwner) { receivedUserlist ->
            userlist = receivedUserlist
            adapter = Administer_RCV(userlist, viewModel)
            recyclerView.adapter = adapter
        }

    }
}