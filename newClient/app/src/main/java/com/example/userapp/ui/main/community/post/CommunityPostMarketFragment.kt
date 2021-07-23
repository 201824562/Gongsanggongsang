package com.example.userapp.ui.main.community.post

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.userapp.R
import com.example.userapp.base.BaseFragment
import com.example.userapp.data.entity.PostCommentDataClass
import com.example.userapp.databinding.FragmentCommunityPostBinding
import com.example.userapp.databinding.FragmentCommunityPostMarketBinding
import com.example.userapp.ui.main.community.CommunityViewModel
import com.example.userapp.ui.main.community.write.CommunityAttachPhotoRecyclerAdapter
import java.time.LocalDateTime


class CommunityPostMarketFragment : BaseFragment<FragmentCommunityPostMarketBinding, CommunityViewModel>(){
    private lateinit var collection_name : String
    private lateinit var document_name : String
    private lateinit var bundle: Bundle
    private var post_comments_array : ArrayList<PostCommentDataClass> = arrayListOf()
    override lateinit var viewbinding: FragmentCommunityPostMarketBinding

    override val viewmodel: CommunityViewModel by viewModels()

    private lateinit var adapter: CommunityCommentRecyclerAdapter
    private lateinit var photoAdapter : CommunityAttachPhotoRecyclerAdapter
    private var getPhotoUri : ArrayList<String> = arrayListOf()
    var test : ArrayList<String> = arrayListOf()

    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentCommunityPostMarketBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        collection_name= arguments?.getString("collection_name").toString()
        document_name = arguments?.getString("document_name").toString()
        bundle = bundleOf(
            "collection_name" to collection_name,
            "document_name" to document_name
        )
        viewmodel.getDocumentPostData(collection_name, document_name).observe(viewLifecycleOwner){ it
            viewbinding.marketPostContents.text = it.post_contents
            viewbinding.marketPostTitle.text = it.post_title
            viewbinding.marketPostPrice.text = it.post_state
            post_comments_array = viewmodel.getDocumentCommentData(it)
            viewmodel.getPostPhotoData(it.post_photo_uri).observe(viewLifecycleOwner){ it
                Log.e("check", "$it")
                initPhotoRecyclerView(it)
            }
            getPhotoUri = it.post_photo_uri
            viewbinding.communityPostCommentsNumber.text = post_comments_array.size.toString()
            initCommentRecyclerView(post_comments_array)
            //getPhotoUri = it.post_photo_uri
        }
        /*viewmodel.getPostPhotoData().observe(viewLifecycleOwner){ it
            initPhotoRecyclerView(it)
        }*/
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewmodel.getPostPhotoData(getPhotoUri).observe(viewLifecycleOwner){ it
            Log.e("check", "$it")
            initPhotoRecyclerView(it)
        }
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
        viewbinding.marketPostPhotoRecycler.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context).also { it.orientation = LinearLayoutManager.HORIZONTAL }
            adapter = CommunityAttachPhotoRecyclerAdapter(photo_uri)
        }
    }


}
