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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.userapp.MainActivity
import com.example.userapp.R
import com.example.userapp.base.BaseSessionFragment
import com.example.userapp.data.entity.PostCommentDataClass
import com.example.userapp.databinding.FragmentCommunityPostBinding
import com.example.userapp.service.sendFireStoreNotification
import com.example.userapp.ui.main.community.CommunityViewModel
import com.example.userapp.ui.main.community.write.CommunityAttachPostPhotoRecyclerAdapter
import com.example.userapp.utils.WrapedDialogBasicTwoButton
import java.time.LocalDate
import java.time.LocalTime


class CommunityPostFragment : BaseSessionFragment<FragmentCommunityPostBinding, CommunityViewModel>(){
    override lateinit var viewbinding: FragmentCommunityPostBinding
    override val viewmodel: CommunityViewModel by viewModels()

    private val navArgs : CommunityPostFragmentArgs by navArgs()
    private lateinit var collectionName : String
    private lateinit var documentName : String
    private lateinit var bundle: Bundle

    private lateinit var attachPostPhotoRecyclerAdapter: CommunityAttachPostPhotoRecyclerAdapter
    private var remoteGetPhotoUri : ArrayList<String> = arrayListOf()

    private lateinit var commentRecyclerAdapter: CommunityCommentRecyclerAdapter
    private var postCommentsArray : ArrayList<PostCommentDataClass> = arrayListOf()

    private var localUserName = ""

    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentCommunityPostBinding.inflate(inflater, container, false)
        return viewbinding.root
    }



    override fun initViewStart(savedInstanceState: Bundle?) {
        viewmodel.getUserInfo()
        collectionName = navArgs.postDataInfo.post_category
        documentName = navArgs.postDataInfo.post_id
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.onSuccessGettingUserInfo.observe(this, {
            localUserName = it.nickname
            initPostView()
            if(collectionName == "4_WITH") { initWithPostView() }
        })
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.run{
            postBackButton.setOnClickListener { findNavController().navigate(R.id.action_community_post_pop) }
            commentsRegister.setOnClickListener{
                val postDateNow: String = LocalDate.now().toString()
                val postTimeNow : String = LocalTime.now().toString()
                val postComment = PostCommentDataClass(
                    localUserName,
                    commentContents = writeComment.text.toString(),
                    commentDate = postDateNow,
                    commentTime = postTimeNow,
                    commentAnonymous = false,
                    commentId = postDateNow + postTimeNow + localUserName,
                )
                viewmodel.insertPostCommentData(collectionName, documentName, postComment).observe(viewLifecycleOwner){
                    if(it){
                        showToast("댓글이 등록되었습니다.")
                        viewmodel.getPostCommentData(collectionName, documentName).observe(viewLifecycleOwner){
                            postCommentsArray = it
                            commentRecyclerAdapter.notifyDataSetChanged()
                            initCommentRecyclerView()
                            communityPostCommentsNumber.text = it.size.toString()
                            viewmodel.modifyPostPartData(collectionName, documentName, "post_comments", it.size)
                        }
                    }
                }
                viewmodel.registerNotificationToFireStore("공생공생", "내가 쓴 게시글에 댓글이 달렸어요!")
            }
            postRemoveButton.setOnClickListener{ makeDialog("정말로 글을 삭제할까요?", "isPost", PostCommentDataClass()) }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        viewmodel.initPostData()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initPostView(){
        val postDateNow: String = LocalDate.now().toString()
        val postTimeNow: String = LocalTime.now().toString()
        initCommentRecyclerView()
        initPhotoRecyclerView()

        viewbinding.run {
            when(collectionName){
                "1_FREE" -> postToolbarName.text = "자유게시판"
                "2_EMERGENCY" -> postToolbarName.text = "긴급게시판"
                "3_SUGGEST" -> postToolbarName.text = "건의게시판"
                "4_WITH" -> postToolbarName.text = "함께게시판"
            }
            if(navArgs.postDataInfo.post_name == localUserName) { postRemoveButton.visibility = View.VISIBLE }
            if(collectionName != "4_WITH" && navArgs.postDataInfo.post_state != "none"){ postCategory.text = navArgs.postDataInfo.post_state }
            if(navArgs.postDataInfo.post_date == postDateNow) {
                val hour = postTimeNow.substring(0,2).toInt() - navArgs.postDataInfo.post_time.substring(0,2).toInt()
                val minute = postTimeNow.substring(3,5).toInt() - navArgs.postDataInfo.post_time.substring(3,5).toInt()
                if(hour == 0){ communityPostTime.text = "${minute}분 전" }
                else{ communityPostTime.text = "${hour}시간 전" }
            }
            else{ communityPostTime.text = navArgs.postDataInfo.post_date.substring(5) }
            communityPostName.text = navArgs.postDataInfo.post_name
            postContents.text = navArgs.postDataInfo.post_contents
            postTitle.text = navArgs.postDataInfo.post_title

            viewmodel.getPostPhotoData(navArgs.postDataInfo.post_photo_uri)
            viewmodel.getPostPhotoSuccess().observe(viewLifecycleOwner){
                remoteGetPhotoUri = it
                initPhotoRecyclerView()
                attachPostPhotoRecyclerAdapter.notifyDataSetChanged()
            }
            viewmodel.getPostCommentData(collectionName, documentName).observe(viewLifecycleOwner){
                postCommentsArray = it
                communityPostCommentsNumber.text = it.size.toString()
                initCommentRecyclerView()
                commentRecyclerAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun initWithPostView() {
        viewbinding.run {
            if(localUserName == navArgs.postDataInfo.post_name && !navArgs.postDataInfo.post_anonymous) { postWithComplete.visibility = View.VISIBLE }
            if(navArgs.postDataInfo.post_state == "none" && !navArgs.postDataInfo.post_anonymous) { postCategory.text = "모집 중"}
            if(navArgs.postDataInfo.post_state == "none" && navArgs.postDataInfo.post_anonymous) { postCategory.text = "모집 완료" }
            postWithCompleteButton.setOnClickListener {
                viewmodel.modifyPostPartData(collectionName, documentName, "post_anonymous", true).observe(viewLifecycleOwner){
                    if(it){
                        postWithComplete.visibility = View.GONE
                        postCategory.text = "모집 완료"
                    }
                }
            }
        }
    }
    private fun initCommentRecyclerView(){
        commentRecyclerAdapter = CommunityCommentRecyclerAdapter(postCommentsArray, localUserName)
        viewbinding.postCommentRecyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            commentRecyclerAdapter = CommunityCommentRecyclerAdapter(postCommentsArray, localUserName)
        }
        viewbinding.postCommentRecyclerView.adapter = commentRecyclerAdapter.apply {
            listener = object : CommunityCommentRecyclerAdapter.OnCommunityCommentItemClickListener{
                override fun onCommentItemClick(position: Int) {
                    makeDialog("정말로 댓글을 삭제할까요?", "isComment", getItem(position))
                }
            }
            tagListener = object  : CommunityCommentRecyclerAdapter.OnCommunityCommentItemTagClickListener{
                override fun onCommentItemTagClick(position: Int) {
                    val tagName = "@" + getItem(position).commentName
                    viewbinding.writeComment.setText(tagName)
                }
            }
        }
        commentRecyclerAdapter.notifyDataSetChanged()
    }

    private fun initPhotoRecyclerView(){
        viewbinding.postPhotoRecycler.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context).also { it.orientation = LinearLayoutManager.HORIZONTAL }
            adapter = CommunityAttachPostPhotoRecyclerAdapter(remoteGetPhotoUri)
        }
        attachPostPhotoRecyclerAdapter = CommunityAttachPostPhotoRecyclerAdapter(remoteGetPhotoUri)
        viewbinding.postPhotoRecycler.adapter = attachPostPhotoRecyclerAdapter.apply {
            listener = object :
                    CommunityAttachPostPhotoRecyclerAdapter.OnCommunityPhotoItemClickListener {
                    override fun onPhotoItemClick(position: Int) {
                        var bundle = bundleOf(
                            "photo_uri" to remoteGetPhotoUri.toTypedArray(),
                        )
                        findNavController().navigate(R.id.action_communityPostFragment_to_communityGetPhotoFragment, bundle)
                    }
                }
        }
    }
    private fun makeDialog(msg : String, isPostOrComment : String, commentData : PostCommentDataClass){
        val dialog = WrapedDialogBasicTwoButton(requireContext(), msg, "취소", "확인").apply {
            clickListener = object : WrapedDialogBasicTwoButton.DialogButtonClickListener{
                override fun dialogCloseClickListener() {
                    dismiss()
                }
                override fun dialogCustomClickListener() {
                    when(isPostOrComment){
                        "isPost" -> viewmodel.deletePostData(collectionName, documentName).observe(viewLifecycleOwner){
                            if(it){
                                findNavController().navigate(R.id.action_community_post_pop, bundle)
                            }
                        }
                        "isComment" -> viewmodel.deletePostCommentData(collectionName, documentName, commentData).observe(viewLifecycleOwner){
                            if(it){
                                viewmodel.getPostCommentData(collectionName, documentName).observe(viewLifecycleOwner){
                                    postCommentsArray = it
                                    viewmodel.modifyPostPartData(collectionName, documentName, "post_comments", it.size)
                                    commentRecyclerAdapter.notifyDataSetChanged()
                                }
                                dismiss()
                            }
                        }
                    }

                }
            }
        }
        showDialog(dialog, viewLifecycleOwner)
    }
}
