package com.example.gongsanggongsang.Third

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gongsanggongsang.data.CommunityPostModel
import com.example.gongsanggongsang.R
import com.example.gongsanggongsang.Third.CommunityPreviewRecyclerAdapter
import com.example.gongsanggongsang.data.CommunityCommentsModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_community_preview.*

class CommunityPreviewFragment : Fragment() {
    var database = FirebaseFirestore.getInstance()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_community_preview, container, false)
        return rootView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var collection_name= arguments?.getString("collection_name").toString()
        var bundle = bundleOf(
            "collection_name" to collection_name
        )
        initRecyclerView(collection_name)
        community_write_button.setOnClickListener{
            findNavController().navigate(R.id.action_communityPreview_to_communityWrite, bundle)
        }

    }
    fun initRecyclerView(collection_name : String) {
        var testlist = arrayListOf<CommunityPostModel>()
        var mCommunityRecyclerAdapter: CommunityPreviewRecyclerAdapter =
            CommunityPreviewRecyclerAdapter(testlist).apply {
                listener =
                    object : CommunityPreviewRecyclerAdapter.OnCommunityMarketItemClickListener {
                        override fun onPreviewItemClick(position: Int) {
                            var document_name = getItem(position).date.toString() + getItem(position).title.toString()
                            var bundle = bundleOf(
                                "collection_name" to collection_name,
                                "document_name" to document_name
                            )
                            findNavController().navigate(R.id.communityPost, bundle)
                        }
                    }
            }

        community_preview_recycler_view.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = mCommunityRecyclerAdapter
        }
        database.collection(collection_name)
                .get()
                .addOnSuccessListener { result ->
                    testlist.clear()
                    for (document in result){
                        val item = CommunityPostModel("주용가리",
                            document["title"] as String,
                            document["contents"] as String,
                            document["date"] as String,
                            document["comments"] as ArrayList<CommunityCommentsModel>)
                        testlist.add(item)
                    }
                    mCommunityRecyclerAdapter.notifyDataSetChanged()
                }
    }
}