package com.example.gongsanggongsangAdmin.Fifth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.gongsanggongsangAdmin.R
import com.example.gongsanggongsangAdmin.data.UserDataClass
import kotlinx.android.synthetic.main.fragment_test_administer.*

class FifthFragment : Fragment() {
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