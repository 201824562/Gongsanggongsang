package com.example.adminapp.ui.main.community.post

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminapp.R
import com.example.adminapp.base.BaseFragment
import com.example.adminapp.data.repository.PostCommentDataClass
import com.example.adminapp.databinding.FragmentCommunityPostBinding
import com.example.adminapp.ui.main.community.CommunityViewModel
import com.example.adminapp.ui.main.community.write.CommunityAttachPhotoRecyclerAdapter
import com.google.firebase.storage.FirebaseStorage
import java.time.LocalDateTime


class CommunityPostFragment : BaseFragment<FragmentCommunityPostBinding, CommunityViewModel>(){
    private lateinit var collection_name : String
    private lateinit var document_name : String
    private lateinit var bundle: Bundle
    private var post_comments_array : ArrayList<PostCommentDataClass> = arrayListOf()
    override lateinit var viewbinding: FragmentCommunityPostBinding
    private val fireStorage = FirebaseStorage.getInstance("gs://gongsanggongsang-94f86.appspot.com/")
    override val viewmodel: CommunityViewModel by viewModels()

    private lateinit var adapter: CommunityCommentRecyclerAdapter
    private lateinit var photoAdapter : CommunityAttachPhotoRecyclerAdapter
    private lateinit var getPhotoUri : ArrayList<String>
    var test : ArrayList<String> = arrayListOf()

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
        viewmodel.getDocumentPostData(collection_name, document_name).observe(viewLifecycleOwner) {
            it
            viewbinding.postContents.text = it.post_contents
            viewbinding.postTitle.text = it.post_title
            viewbinding.postCategory.text = it.post_state
            post_comments_array = viewmodel.getDocumentCommentData(it)
            viewbinding.communityPostCommentsNumber.text = post_comments_array.size.toString()
            initCommentRecyclerView(post_comments_array)
            //getPhotoUri = it.post_photo_uri
        }

    }

    override fun initDataBinding(savedInstanceState: Bundle?) {

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.run{
            commentsRegister.setOnClickListener{
                var post_comments = PostCommentDataClass("주용가리", viewbinding.writeComment.text.toString(), LocalDateTime.now().toString())
                post_comments_array.add(post_comments)
                communityPostCommentsNumber.text = post_comments_array.size.toString()
                viewmodel.insertPostCommentData(collection_name, document_name, post_comments_array)
                adapter = CommunityCommentRecyclerAdapter(post_comments_array)
                initCommentRecyclerView(post_comments_array)
                adapter.notifyDataSetChanged()
            }
            postRemoveButton.setOnClickListener{
                viewmodel.deletePostData(collection_name, document_name)
                findNavController().navigate(R.id.action_communityPost_to_communityPreview, bundle)
            }
            postModifyButton.setOnClickListener{
                findNavController().navigate(R.id.action_communityPost_to_communityModify, bundle)
            }
        }
    }
    fun initCommentRecyclerView(post_comments: ArrayList<PostCommentDataClass>){
        viewbinding.postCommentRecyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = CommunityCommentRecyclerAdapter(post_comments)
        }
    }

    fun initPhotoRecyclerView(photo_uri : ArrayList<String>){
        viewbinding.postPhotoRecycler.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context).also { it.orientation = LinearLayoutManager.HORIZONTAL }
            adapter = CommunityAttachPhotoRecyclerAdapter(photo_uri)
        }
    }


}
