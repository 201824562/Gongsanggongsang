package com.example.userapp.ui.main.community.post

import android.annotation.SuppressLint
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
import com.example.userapp.databinding.FragmentCommunityPostBinding
import com.example.userapp.ui.main.community.CommunityViewModel
import com.example.userapp.ui.main.community.write.CommunityAttachPhotoRecyclerAdapter
import java.time.LocalDate
import java.time.LocalTime


class CommunityPostFragment : BaseFragment<FragmentCommunityPostBinding, CommunityViewModel>(){
    private lateinit var collectionName : String
    private lateinit var documentName : String
    private lateinit var bundle: Bundle
    private var postCommentsArray : ArrayList<PostCommentDataClass> = arrayListOf()
    override lateinit var viewbinding: FragmentCommunityPostBinding
    override val viewmodel: CommunityViewModel by viewModels()
    private lateinit var getPhotoUri : ArrayList<String>
    private lateinit var currentPostId : String
    private lateinit var currentPostComment : ArrayList<String>
    private lateinit var attachPostPhotoRecyclerAdapter: CommunityAttachPhotoRecyclerAdapter
    private lateinit var commentRecyclerAdapter: CommunityCommentRecyclerAdapter
    private var localUserName = ""
    var agency = ""
    var test : ArrayList<String> = arrayListOf()

    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentCommunityPostBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun initViewStart(savedInstanceState: Bundle?) {
        val ac = activity as MainActivity
        agency = ac.getUserData()!!.agency
        localUserName = ac.getUserData()!!.nickname
        collectionName= arguments?.getString("collection_name").toString()
        documentName = arguments?.getString("document_name").toString()
        bundle = bundleOf(
            "collection_name" to collectionName,
            "document_name" to documentName
        )
        when(collectionName){
            "1_free" -> viewbinding.postToolbarName.text = "자유게시판"
            "2_emergency" -> viewbinding.postToolbarName.text = "긴급게시판"
            "3_suggest" -> viewbinding.postToolbarName.text = "건의게시판"
            "4_with" -> viewbinding.postToolbarName.text = "함께게시판"
            "5_market" -> viewbinding.postToolbarName.text = "장터게시판"
        }
        viewmodel.getDocumentPostData(agency, collectionName, documentName).observe(viewLifecycleOwner) { it
            currentPostId = it.post_id
            currentPostComment = it.post_comments
            if(it.post_name == localUserName){
                viewbinding.postRemoveButton.visibility = View.VISIBLE
            }
            if(collectionName == "4_with" && localUserName == it.post_name && it.post_state == "모집 중"){
                viewbinding.postWithComplete.visibility = View.VISIBLE
            }
            if(it.post_state != "none"){
                viewbinding.postCategory.text = it.post_state
            }
            if(it.post_anonymous){
                viewbinding.communityPostName.text = "익명"
            }
            else{
                viewbinding.communityPostName.text = it.post_name
            }
            val postDateNow: String = LocalDate.now().toString()
            val postTimeNow: String = LocalTime.now().toString()
            if(it.post_date == postDateNow){
                val hour = postTimeNow.substring(0,2).toInt() - it.post_time.substring(0,2).toInt()
                val minute = postTimeNow.substring(3,5).toInt() - it.post_time.substring(3,5).toInt()
                if(hour == 0){
                    viewbinding.communityPostTime.text = "${minute}분 전"
                }
                else{
                    viewbinding.communityPostTime.text = "${hour}시간 전"
                }
            }
            else{
                viewbinding.communityPostTime.text = it.post_date.substring(5)
            }
            viewbinding.postContents.text = it.post_contents
            viewbinding.postTitle.text = it.post_title
            getPhotoUri = it.post_photo_uri
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
                if(commentAnonymousCheckBox.isChecked){
                    commentAnonymous = true
                }
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
            postWithCompleteButton.setOnClickListener {
                viewmodel.modifyPostPartData(agency, collectionName, documentName, "post_state", "모집 완료").observe(viewLifecycleOwner){
                    if(it){
                        postWithComplete.visibility = View.GONE
                        postCategory.text = "모집 완료"
                    }
                }
            }
            /*postModifyButton.setOnClickListener{
                val bundleModify = bundleOf(
                    "modify" to "modify",
                    "postId" to documentName
                )
                when(collectionName){
                    "1_free" -> findNavController().navigate(R.id.action_communityPost_to_communityFreeWrite, bundleModify)
                    "2_emergency" -> findNavController().navigate(R.id.action_communityPost_to_communityEmergencyWrite, bundleModify)
                    "3_suggest" -> findNavController().navigate(R.id.action_communityPost_to_communitySuggestWrite, bundleModify)
                    "4_with" -> findNavController().navigate(R.id.action_communityPost_to_communityWithWrite, bundleModify)
                    "5_market" -> findNavController().navigate(R.id.action_communityPost_to_communityMarketWrite, bundleModify)
                }
            }*/
        }
    }
    private fun initCommentRecyclerView(){
        viewbinding.postCommentRecyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = CommunityCommentRecyclerAdapter(postCommentsArray)
        }
        commentRecyclerAdapter = CommunityCommentRecyclerAdapter(postCommentsArray)
        viewbinding.postCommentRecyclerView.adapter = commentRecyclerAdapter.apply {
            object :
            CommunityCommentRecyclerAdapter.OnCommunityCommentItemClickListener{
                override fun onCommentItemClick(position: Int) {
                    println(getItem(position).commentId)
                    println("touch")
                    viewmodel.deletePostCommentData(agency, collectionName, documentName, getItem(position)).observe(viewLifecycleOwner){
                        if(it){
                            println("delete success")
                        }
                    }
                }
            }
        }
        commentRecyclerAdapter.notifyDataSetChanged()
    }

    private fun initPhotoRecyclerView(photo_uri : ArrayList<String>){
        viewbinding.postPhotoRecycler.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context).also { it.orientation = LinearLayoutManager.HORIZONTAL }
            adapter = CommunityAttachPhotoRecyclerAdapter(photo_uri)
        }
        attachPostPhotoRecyclerAdapter = CommunityAttachPhotoRecyclerAdapter(photo_uri)
        viewbinding.postPhotoRecycler.adapter = attachPostPhotoRecyclerAdapter.apply {
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
