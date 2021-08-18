package com.example.userapp.ui.main.community.post

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
import com.example.userapp.MainActivity
import com.example.userapp.R
import com.example.userapp.base.BaseFragment
import com.example.userapp.data.entity.PostCommentDataClass
import com.example.userapp.databinding.FragmentCommunityPostMarketBinding
import com.example.userapp.ui.main.community.CommunityViewModel
import com.example.userapp.ui.main.community.write.CommunityAttachPhotoRecyclerAdapter
import java.time.LocalDate
import java.time.LocalTime


class CommunityPostMarketFragment : BaseFragment<FragmentCommunityPostMarketBinding, CommunityViewModel>(){
    private lateinit var collectionName : String
    private lateinit var documentName : String
    private lateinit var bundle: Bundle
    private var postCommentsArray : ArrayList<PostCommentDataClass> = arrayListOf()
    override lateinit var viewbinding: FragmentCommunityPostMarketBinding
    private lateinit var currentPostComment : ArrayList<String>
    override val viewmodel: CommunityViewModel by viewModels()
    private lateinit var attachPostPhotoRecyclerAdapter: CommunityAttachPhotoRecyclerAdapter
    private lateinit var adapter: CommunityCommentRecyclerAdapter
    private lateinit var photoAdapter : CommunityAttachPhotoRecyclerAdapter
    private var getPhotoUri : ArrayList<String> = arrayListOf()
    var agency = ""
    var localUserName = ""


    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentCommunityPostMarketBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        val ac = activity as MainActivity
        agency = ac.getUserData()!!.agency
        localUserName = ac.getUserData()!!.nickname
        viewmodel.deletePostPhoto()
        collectionName= arguments?.getString("collection_name").toString()
        documentName = arguments?.getString("document_name").toString()
        bundle = bundleOf(
            "collection_name" to collectionName,
            "document_name" to documentName
        )
        viewmodel.getDocumentPostData(agency, collectionName, documentName).observe(viewLifecycleOwner){ it
            if(it.post_name == localUserName){
                viewbinding.postRemoveButton.visibility = View.VISIBLE
            }
            if(localUserName == it.post_name && !it.post_anonymous){
                viewbinding.postMarketComplete.visibility = View.VISIBLE
            }
            viewbinding.communityPostName.text = it.post_name
            viewbinding.communityPostTime.text = it.post_date
            viewbinding.marketPostContents.text = it.post_contents
            viewbinding.marketPostTitle.text = it.post_title
            viewbinding.marketPostPrice.text = it.post_state
            //post_comments_array = viewmodel.getDocumentCommentData(it)

            viewmodel.getPostPhotoData(it.post_photo_uri).observe(viewLifecycleOwner){ it
                initPhotoRecyclerView(it)
                getPhotoUri.clear()
            }

        }
        viewmodel.getDocumentCommentData(agency, collectionName, documentName).observe(viewLifecycleOwner){
            postCommentsArray = it
            viewbinding.communityPostCommentsNumber.text = it.size.toString()
            initCommentRecyclerView()
        }

    }

    override fun initDataBinding(savedInstanceState: Bundle?) {

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.run{
            commentsRegister.setOnClickListener{
                val postDateNow: String = LocalDate.now().toString()
                val postTimeNow : String = LocalTime.now().toString()
                var commentAnonymous = false
                val postComment = PostCommentDataClass(
                    localUserName,
                    commentContents = writeComment.text.toString(),
                    commentDate = postDateNow,
                    commentTime = postTimeNow,
                    commentAnonymous = commentAnonymous,
                    postDateNow + postTimeNow + localUserName,
                )
                viewmodel.insertPostCommentData(agency, collectionName, documentName, postComment).observe(viewLifecycleOwner){
                    if(it){
                        viewmodel.getDocumentCommentData(agency, collectionName, documentName).observe(viewLifecycleOwner){
                            postCommentsArray = it
                            initCommentRecyclerView()
                            viewbinding.communityPostCommentsNumber.text = it.size.toString()
                        }
                    }
                }
                currentPostComment.add("1")
                viewmodel.modifyPostPartData(agency, collectionName, documentName, "post_comments", currentPostComment)

            }
            postRemoveButton.setOnClickListener{
                viewmodel.deletePostData(agency, collectionName, documentName)
                findNavController().navigate(R.id.action_communityPost_to_communityPreview, bundle)
            }

            marketPostCompleteButton.setOnClickListener {

            }
        }
    }
    fun initCommentRecyclerView(){
        viewbinding.postCommentRecyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = CommunityCommentRecyclerAdapter(postCommentsArray)
        }
    }

    fun initPhotoRecyclerView(photo_uri : ArrayList<String>){
        viewbinding.marketPostPhotoRecycler.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context).also { it.orientation = LinearLayoutManager.HORIZONTAL }
            adapter = CommunityAttachPhotoRecyclerAdapter(photo_uri)
        }
        attachPostPhotoRecyclerAdapter = CommunityAttachPhotoRecyclerAdapter(photo_uri)
        viewbinding.marketPostPhotoRecycler.adapter = attachPostPhotoRecyclerAdapter.apply {
            listener =
                object :
                    CommunityAttachPhotoRecyclerAdapter.OnCommunityPhotoItemClickListener {
                    override fun onPhotoItemClick(position: Int) {
                        var bundle = bundleOf(
                            "photo_uri" to photo_uri.toTypedArray(),
                        )
                        findNavController().navigate(R.id.action_communityPost_to_communityPhoto, bundle)
                    }
                }
        }
    }

}
