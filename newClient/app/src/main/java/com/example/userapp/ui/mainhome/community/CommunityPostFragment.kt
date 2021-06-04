package com.example.userapp.ui.mainhome.community

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.userapp.R
import com.example.userapp.base.BaseFragment
import com.example.userapp.data.entity.PostCommentDataClass
import com.example.userapp.data.model.PostDataInfo
import com.example.userapp.databinding.FragmentCommunityPostBinding
import com.example.userapp.ui.mainhome.community.CommunityViewModel
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime


class CommunityPostFragment : BaseFragment<FragmentCommunityPostBinding, CommunityViewModel>(){
    private lateinit var collection_name : String
    private lateinit var document_name : String
    private lateinit var bundle: Bundle
    private var post_comments_array : ArrayList<PostCommentDataClass> = arrayListOf()
    override lateinit var viewbinding: FragmentCommunityPostBinding

    override val viewmodel: CommunityViewModel by viewModels()

    private lateinit var adapter: CommunityCommentRecyclerAdapter

    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentCommunityPostBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        collection_name= arguments?.getString("collection_name").toString()
        document_name = arguments?.getString("document_name").toString()
        bundle = bundleOf(
            "collection_name" to collection_name,
            "document_name" to document_name
        )
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {

    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewmodel.getDocumentPostData(collection_name, document_name).observe(viewLifecycleOwner){ it
            viewbinding.postContents.text = it.post_contents
            viewbinding.postTitle.text = it.post_title
        }
        viewbinding.commentsRegister.setOnClickListener{
            var post_comments = PostCommentDataClass("주용가리", viewbinding.writeComment.text.toString(), LocalDateTime.now().toString())
            post_comments_array.add(post_comments)
            viewmodel.insertPostCommentData(collection_name, document_name, post_comments_array)
            adapter = CommunityCommentRecyclerAdapter(post_comments_array)
            initRecyclerView(post_comments_array)
            adapter.notifyDataSetChanged()
        }
        viewbinding.postRemoveButton.setOnClickListener{
            viewmodel.deletePostData(collection_name, document_name)
            findNavController().navigate(R.id.action_communityPost_to_communityPreview, bundle)
        }
        viewbinding.postModifyButton.setOnClickListener{
            findNavController().navigate(R.id.action_communityPost_to_communityModify, bundle)
        }

    }
    fun initRecyclerView(post_comments: ArrayList<PostCommentDataClass>){
        viewbinding.postCommentRecyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = CommunityCommentRecyclerAdapter(post_comments)
        }
    }

}
