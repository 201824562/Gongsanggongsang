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
import com.example.userapp.base.BaseSessionFragment
import com.example.userapp.data.entity.PostCommentDataClass
import com.example.userapp.databinding.FragmentCommunityPostBinding
import com.example.userapp.ui.main.community.CommunityViewModel
import com.example.userapp.ui.main.community.write.CommunityAttachPostPhotoRecyclerAdapter
import com.example.userapp.utils.WrapedDialogBasicTwoButton
import java.time.LocalDate
import java.time.LocalTime


class CommunityPostMarketFragment : BaseSessionFragment<FragmentCommunityPostBinding, CommunityViewModel>(){
    override lateinit var viewbinding: FragmentCommunityPostBinding
    override val viewmodel: CommunityViewModel by viewModels()

    private lateinit var collectionName : String
    private lateinit var documentName : String
    private lateinit var bundle: Bundle

    private lateinit var attachPostPhotoRecyclerAdapter: CommunityAttachPostPhotoRecyclerAdapter
    private var remoteGetPhotoUri : ArrayList<String> = arrayListOf()

    private lateinit var commentRecyclerAdapter: CommunityCommentRecyclerAdapter
    private var postCommentsArray : ArrayList<PostCommentDataClass> = arrayListOf()

    private var localUserName = ""
    private var agency = ""

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
        initCommentRecyclerView()
        initPhotoRecyclerView()
        viewbinding.run {
            when(collectionName){
                "1_free" -> postToolbarName.text = "자유게시판"
                "2_emergency" -> postToolbarName.text = "긴급게시판"
                "3_suggest" -> postToolbarName.text = "건의게시판"
                "4_with" -> postToolbarName.text = "함께게시판"
                "5_market" -> postToolbarName.text = "장터게시판"
            }
            viewmodel.getPostData(agency, collectionName, documentName).observe(viewLifecycleOwner) {
                if(it.post_name == localUserName){
                    postRemoveButton.visibility = View.VISIBLE
                }
                if(localUserName == it.post_name && it.post_state == "판매 중"){
                    postWithComplete.visibility = View.VISIBLE
                }
                if(it.post_state != "none"){
                    postCategory.text = it.post_state
                }
                val postDateNow: String = LocalDate.now().toString()
                val postTimeNow: String = LocalTime.now().toString()
                if(it.post_date == postDateNow){
                    val hour = postTimeNow.substring(0,2).toInt() - it.post_time.substring(0,2).toInt()
                    val minute = postTimeNow.substring(3,5).toInt() - it.post_time.substring(3,5).toInt()
                    if(hour == 0){
                        communityPostTime.text = "${minute}분 전"
                    }
                    else{
                        communityPostTime.text = "${hour}시간 전"
                    }
                }
                else{
                    communityPostTime.text = it.post_date.substring(5)
                }

                communityPostName.text = it.post_name
                postContents.text = it.post_contents
                postTitle.text = it.post_title

                viewmodel.getPostPhotoData(it.post_photo_uri)
                viewmodel.getPostPhotoSuccess().observe(viewLifecycleOwner){
                    remoteGetPhotoUri = it
                    initPhotoRecyclerView()
                    attachPostPhotoRecyclerAdapter.notifyDataSetChanged()
                }
            }
            viewmodel.getPostCommentData(agency, collectionName, documentName).observe(viewLifecycleOwner){
                postCommentsArray = it
                initCommentRecyclerView()
                commentRecyclerAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.run{
            marketWriteBackButton.setOnClickListener {
                findNavController().navigate(R.id.action_communityPostMarketFragment_pop)
            }
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
                viewmodel.insertPostCommentData(agency, collectionName, documentName, postComment).observe(viewLifecycleOwner){
                    if(it){
                        viewmodel.getPostCommentData(agency, collectionName, documentName).observe(viewLifecycleOwner){
                            postCommentsArray = it
                            commentRecyclerAdapter.notifyDataSetChanged()
                            initCommentRecyclerView()
                            communityPostCommentsNumber.text = it.size.toString()
                            viewmodel.modifyPostPartData(agency, collectionName, documentName, "post_comments", it.size)
                        }
                    }
                }
            }
            postRemoveButton.setOnClickListener{
                makeDialog("정말로 글을 삭제할까요?", "isPost", PostCommentDataClass())
            }
            postWithCompleteButton.setOnClickListener {
                viewmodel.modifyPostPartData(agency, collectionName, documentName, "post_state", "모집 완료").observe(viewLifecycleOwner){
                    if(it){
                        postWithComplete.visibility = View.GONE
                        postCategory.text = "모집 완료"
                    }
                }
            }
        }
    }
    private fun initCommentRecyclerView(){
        viewbinding.postCommentRecyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = CommunityCommentRecyclerAdapter(postCommentsArray, localUserName)
        }
        commentRecyclerAdapter = CommunityCommentRecyclerAdapter(postCommentsArray, localUserName)
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
                    findNavController().navigate(R.id.action_communityPostMarket_to_communityPhoto, bundle)
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
                        "isPost" -> viewmodel.deletePostData(agency, collectionName, documentName).observe(viewLifecycleOwner){
                            if(it){
                                findNavController().navigate(R.id.action_community_post_pop, bundle)
                            }
                        }
                        "isComment" -> viewmodel.deletePostCommentData(agency, collectionName, documentName, commentData).observe(viewLifecycleOwner){
                            if(it){
                                viewmodel.getPostCommentData(agency, collectionName, documentName).observe(viewLifecycleOwner){
                                    postCommentsArray = it
                                    viewmodel.modifyPostPartData(agency, collectionName, documentName, "post_comments", it.size)
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
