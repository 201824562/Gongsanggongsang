package com.example.adminapp.ui.main.community.post

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminapp.R
import com.example.adminapp.base.BaseSessionFragment
import com.example.adminapp.data.entity.PostCommentDataClass
import com.example.adminapp.data.model.AlarmItem
import com.example.adminapp.databinding.FragmentCommunityPostBinding
import com.example.adminapp.ui.main.community.CommunityViewModel
import com.example.adminapp.ui.main.community.write.CommunityAttachPostPhotoRecyclerAdapter
import com.example.adminapp.utils.WrapedDialogBasicTwoButton
import com.example.adminapp.utils.hideKeyboard
import java.time.LocalDate
import java.time.LocalDateTime
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

    private var localUserName = "관리자"
    private var agency = ""
    private var tokenTitle = ""
    private var toUserNameTag = ""
    private var tokenContent = ""
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
        //val ac = activity as MainActivity
        //token = ac.token
        initPostView()
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {

    }

    override fun onDestroy() {
        super.onDestroy()
        viewmodel.initPostData()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.run{
            marketWriteBackButton.setOnClickListener {
                findNavController().navigate(R.id.action_community_post_pop)
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
                viewmodel.insertPostCommentData(collectionName, documentName, postComment).observe(viewLifecycleOwner){
                    if(it){
                        viewmodel.postCommentUploadSuccess = MutableLiveData()
                        showToast("댓글이 등록되었습니다.")
                        writeComment.setText("")
                        hideKeyboard(viewbinding.root)
                        writeCommentTagName.text = ""
                        writeCommentTagName.visibility = View.GONE
                        writeCommentTagNameDeleteBtn.visibility = View.GONE
                        viewmodel.getPostCommentData(collectionName, documentName).observe(viewLifecycleOwner){
                            postCommentsArray = it
                            commentRecyclerAdapter.notifyDataSetChanged()
                            initCommentRecyclerView()
                            communityPostCommentsNumber.text = it.size.toString()
                            viewmodel.modifyPostPartData(collectionName, documentName, "post_comments", it.size)
                        }
                        if(localUserName != navArgs.postDataInfo.post_name){
                            viewmodel.getUserToken(navArgs.postDataInfo.post_name).observe(viewLifecycleOwner){
                                viewmodel.getTokenArrayList = MutableLiveData()
                                for(user in it){
                                    for(token in user.fcmToken){
                                        viewmodel.registerNotificationToFireStore(tokenTitle, tokenTitle + "게시판에 올린 글에 답변이 달렸어요!", token)
                                    }
                                    Log.e("chekckcck", "{$user.id}")
                                    val documentId = LocalDateTime.now().toString() + collectionName + localUserName  //TODO : 날짜 + 타입 + 보내는사람닉네임
                                    val data = AlarmItem(documentId,
                                        LocalDateTime.now().toString(),
                                        user.id,
                                        tokenTitle + "게시판에 올린 글에 답변이 달렸어요!", tokenTitle, null,
                                        navArgs.postDataInfo.makeToPostAlarmData(),
                                        null)
                                    viewmodel.registerAlarmData(user.id, documentId, data)
                                }
                            }
                        }
                        if(toUserNameTag != ""){
                            viewmodel.getTokenArrayList = MutableLiveData()
                            viewmodel.getUserToken(toUserNameTag).observe(viewLifecycleOwner){
                                viewmodel.getTokenArrayList = MutableLiveData()
                                for(user in it){
                                    for(token in user.fcmToken){
                                        viewmodel.registerNotificationToFireStore(tokenTitle, tokenTitle + "게시판에 올린 댓글에 답변이 달렸어요!", token)
                                    }
                                    Log.e("chekckcck", user.id)
                                    val documentId = LocalDateTime.now().toString() + collectionName + localUserName  //TODO : 날짜 + 타입 + 보내는사람닉네임
                                    val data = AlarmItem(documentId, LocalDateTime.now().toString(), user.id,
                                        tokenTitle + "게시판에 올린 댓글에 답변이 달렸어요!", tokenTitle, null, navArgs.postDataInfo.makeToPostAlarmData(), null)
                                    viewmodel.registerAlarmData(user.id, documentId, data)
                                }
                            }
                        }
                    }
                }
                //sendNotification(PushNotification)
            }
            postRemoveButton.setOnClickListener{
                makeDialog("정말로 글을 삭제할까요?", "isPost", PostCommentDataClass())
            }
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initPostView(){
        val postDateNow: String = LocalDate.now().toString()
        val postTimeNow: String = LocalTime.now().toString()
        collectionName= navArgs.postDataInfo.post_category
        documentName = navArgs.postDataInfo.post_id

        initCommentRecyclerView()
        initPhotoRecyclerView()

        viewbinding.run {
            when(collectionName){
                "1_FREE" -> {
                    postToolbarName.text = "자유게시판"
                    tokenTitle = "자유"
                }
                "2_EMERGENCY" -> {
                    postToolbarName.text = "긴급게시판"
                    tokenTitle = "긴급"
                }
                "3_SUGGEST" -> {
                    postToolbarName.text = "건의게시판"
                    tokenTitle = "건의"
                }
                "4_WITH" -> {
                    postToolbarName.text = "함께게시판"
                    tokenTitle = "함께"
                }
            }
            writeCommentTagNameDeleteBtn.setOnClickListener {
                writeCommentTagName.text = ""
                writeCommentTagName.visibility = View.GONE
                writeCommentTagNameDeleteBtn.visibility = View.GONE
                toUserNameTag = ""
            }

            if(navArgs.postDataInfo.post_name == localUserName) { postRemoveButton.visibility = View.VISIBLE }
            if(collectionName == "4_WITH" && localUserName == navArgs.postDataInfo.post_name && !navArgs.postDataInfo.post_anonymous) { postWithComplete.visibility = View.VISIBLE }
            if(navArgs.postDataInfo.post_state != "none"){ postCategory.text = navArgs.postDataInfo.post_state }
            if(collectionName == "4_WITH" && navArgs.postDataInfo.post_state == "none" && !navArgs.postDataInfo.post_anonymous) {
                postCategory.text = "모집 중"
                postCategory.setTextColor(ContextCompat.getColor(requireContext(), R.color.pale_orange))
            }
            if(collectionName == "4_WITH" && navArgs.postDataInfo.post_state == "none" && navArgs.postDataInfo.post_anonymous) {postCategory.text = "모집 완료"}

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
                    viewbinding.writeCommentTagName.visibility = View.VISIBLE
                    viewbinding.writeCommentTagNameDeleteBtn.visibility = View.VISIBLE
                    viewbinding.writeCommentTagName.setText(tagName)
                    viewbinding.writeComment.setHint("")
                    toUserNameTag = tagName.substring(1)
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
                        findNavController().navigate(R.id.action_communityPost_to_communityPhoto, bundle)
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
                                findNavController().navigate(R.id.action_community_post_pop)
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
