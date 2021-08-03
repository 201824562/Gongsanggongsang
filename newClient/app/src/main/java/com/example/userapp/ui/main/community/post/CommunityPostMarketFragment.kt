package com.example.userapp.ui.main.community.post

import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
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
import com.example.userapp.databinding.FragmentCommunityPostMarketBinding
import com.example.userapp.ui.main.community.CommunityViewModel
import com.example.userapp.ui.main.community.write.CommunityAttachPhotoRecyclerAdapter
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.math.log


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
        collection_name= arguments?.getString("collection_name").toString()
        document_name = arguments?.getString("document_name").toString()
        bundle = bundleOf(
            "collection_name" to collection_name,
            "document_name" to document_name
        )
        viewmodel.getDocumentPostData(agency, collection_name, document_name).observe(viewLifecycleOwner){ it
            if(it.post_name == localUserName){
                viewbinding.postRemoveButton.visibility = View.VISIBLE
                viewbinding.postModifyButton.visibility = View.VISIBLE
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

            viewbinding.communityPostCommentsNumber.text = post_comments_array.size.toString()
            initCommentRecyclerView(post_comments_array)
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
                viewmodel.insertPostCommentData(agency, collection_name, document_name, postComment)
                //postCommentsArray.add(postComment)
                //adapter = CommunityCommentRecyclerAdapter(postCommentsArray)
                //initCommentRecyclerView(postCommentsArray)
                //adapter.notifyDataSetChanged()
            }
            postRemoveButton.setOnClickListener{
                viewmodel.deletePostData(agency, collection_name, document_name)
                findNavController().navigate(R.id.action_communityPost_to_communityPreview, bundle)
            }
            postModifyButton.setOnClickListener{
                //findNavController().navigate(R.id.action_communityPost_to_communityModify, bundle)
            }
            marketPostCompleteButton.setOnClickListener {

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
