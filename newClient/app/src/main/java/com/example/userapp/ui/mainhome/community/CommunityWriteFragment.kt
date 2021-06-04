package com.example.userapp.ui.mainhome.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.userapp.R
import com.example.userapp.base.BaseFragment
import com.example.userapp.data.entity.PostCommentDataClass
import com.example.userapp.data.model.PostDataInfo
import com.example.userapp.databinding.FragmentCommunityWriteBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDateTime

class CommunityWriteFragment : BaseFragment<FragmentCommunityWriteBinding, CommunityViewModel>() {
    private lateinit var collection_name : String
    private lateinit var document_name : String
    private lateinit var bundle: Bundle
    override lateinit var viewbinding: FragmentCommunityWriteBinding

    override val viewmodel: CommunityViewModel by viewModels()


    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentCommunityWriteBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        collection_name = arguments?.getString("collection_name").toString()
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.run {
            writeRegisterButton.setOnClickListener{
                var time = LocalDateTime.now().toString()

                val post = PostDataInfo(
                    collection_name,
                    "juyong",
                    writeTitle.text.toString(),
                    writeContents.text.toString(),
                    time,
                    ArrayList<PostCommentDataClass>()
                )
                document_name = post.post_title + post.post_date
                viewmodel.insertPostData(post)

                var bundle = bundleOf(
                    "collection_name" to collection_name,
                    "document_name" to document_name
                )
                findNavController().navigate(R.id.action_communityWrite_to_communityPost, bundle)
            }
        }
    }

}