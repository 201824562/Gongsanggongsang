package com.example.gongsanggongsang.Third

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gongsanggongsang.R
import com.example.gongsanggongsang.data.CommunityCommentsModel
import com.example.gongsanggongsang.data.CommunityPostModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_community_post.*
import kotlinx.android.synthetic.main.fragment_community_preview.*
import kotlinx.android.synthetic.main.fragment_community_write.*
import java.time.LocalDateTime

class CommunityPostFragment : Fragment() {
    var database = FirebaseFirestore.getInstance()
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_community_post, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var document_name = arguments?.getString("document_name").toString()
        var collection_name = arguments?.getString("collection_name").toString()
        var comments_array : ArrayList<CommunityCommentsModel> = ArrayList<CommunityCommentsModel>()
        var mCommunityRecyclerAdapter =  CommunityCommentRecyclerAdapter(comments_array)
        database
            .collection(collection_name)
            .document(document_name)
            .get()
            .addOnSuccessListener { result ->
                if(result["comments"].toString() != "[]"){
                    var server_comments = result["comments"].toString().replace("[", "").replace("]", "")
                            .replace("{", "").replace("}", "").split(",")
                    for (i in 0 until server_comments.size step 3){
                        var comments_element = CommunityCommentsModel(server_comments[i+2].split("=")[1],
                                server_comments[i+1].split("=")[1],
                                server_comments[i].split("=")[1])
                        comments_array.add(comments_element)
                    }
                    initCommentRecyclerView(collection_name, document_name, comments_array)
                }
                post_title.text = result["title"].toString()
                post_contents.text = result["contents"].toString()
            }
        //댓글 등록
        var map= mutableMapOf<String,Any>()
        comments_register.setOnClickListener{
            var comment_add_element = CommunityCommentsModel("주용가리", write_comment.text.toString(), LocalDateTime.now().toString())
            comments_array.add(comment_add_element)
            map["comments"] = comments_array
            database
                .collection(collection_name)
                .document(document_name)
                .update(map)
                .addOnSuccessListener {
                    initCommentRecyclerView(collection_name, document_name, comments_array)
                }
        }
    }
    fun initCommentRecyclerView(collection_name : String, document_name : String, comments_array : ArrayList<CommunityCommentsModel>){
        var mCommunityRecyclerAdapter =  CommunityCommentRecyclerAdapter(comments_array)
        post_comment_recycler_view.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = mCommunityRecyclerAdapter
        }
    }
}
