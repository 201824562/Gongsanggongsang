package com.example.gongsanggongsang

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.gongsanggongsang.DataModel.CommunityPostModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_community_write.*

class CommunityWriteFragment : Fragment() {
    var db : FirebaseFirestore? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_community_write, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = FirebaseFirestore.getInstance()

        write_register_button.setOnClickListener{
            var writeInformation = CommunityPostModel()

            writeInformation.title = write_title.toString()
            writeInformation.contents = write_contents.toString()
            db.collection("test").document("test").set(writeInformation)
            findNavController().navigate(R.id.fragment_community_write)
        }
    }
}