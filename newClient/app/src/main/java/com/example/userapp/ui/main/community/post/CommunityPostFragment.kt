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
    private lateinit var adapter: CommunityCommentRecyclerAdapter
    private lateinit var getPhotoUri : ArrayList<String>
    private lateinit var currentPostId : String
    private lateinit var currentPostComment : ArrayList<String>
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
                viewbinding.postModifyButton.visibility = View.VISIBLE
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
            viewbinding.communityPostCommentsNumber.text = it.post_comments.size.toString()
            viewbinding.postContents.text = it.post_contents
            viewbinding.postTitle.text = it.post_title
            getPhotoUri = it.post_photo_uri
            viewmodel.getPostPhotoData(it.post_photo_uri).observe(viewLifecycleOwner){ it
                initPhotoRecyclerView(it)
                getPhotoUri.clear()
            }
        }
        viewmodel.getDocumentCommentData(agency, collectionName, documentName).observe(viewLifecycleOwner){
            initCommentRecyclerView(it)
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
                var commentAnonymousName = ""
                val postComment = PostCommentDataClass(
                    localUserName,
                    commentContents = writeComment.text.toString(),
                    commentDate = postDateNow,
                    commentTime = postTimeNow,
                    commentAnonymous = commentAnonymous,
                    postDateNow + postTimeNow + localUserName,
                )
                viewmodel.insertPostCommentData(agency, collectionName, documentName, postComment)
                currentPostComment.add("1")
                viewmodel.modifyPostPartData(agency, collectionName, documentName, "post_comments", currentPostComment)
                postCommentsArray.add(postComment)
                adapter = CommunityCommentRecyclerAdapter(postCommentsArray)
                adapter.notifyDataSetChanged()
            }
            postRemoveButton.setOnClickListener{
                viewmodel.deletePostData(agency, collectionName, documentName)
                findNavController().navigate(R.id.action_communityPost_to_communityPreview, bundle)
            }
            postModifyButton.setOnClickListener{
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
            }
        }
    }
    private fun initCommentRecyclerView(post_comments: ArrayList<PostCommentDataClass>){
        viewbinding.postCommentRecyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = CommunityCommentRecyclerAdapter(post_comments)
        }
    }

    private fun initPhotoRecyclerView(photo_uri : ArrayList<String>){
        viewbinding.postPhotoRecycler.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context).also { it.orientation = LinearLayoutManager.HORIZONTAL }
            adapter = CommunityAttachPhotoRecyclerAdapter(photo_uri)
        }
    }


}
