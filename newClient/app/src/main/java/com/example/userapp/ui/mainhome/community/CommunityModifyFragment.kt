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
import com.example.userapp.databinding.FragmentCommunityModifyBinding
import com.example.userapp.databinding.FragmentCommunityWriteBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDateTime

class CommunityModifyFragment : BaseFragment<FragmentCommunityModifyBinding, CommunityViewModel>() {
    private lateinit var collection_name : String
    private lateinit var document_name : String
    private lateinit var bundle: Bundle
    override lateinit var viewbinding: FragmentCommunityModifyBinding

    override val viewmodel: CommunityViewModel by viewModels()


    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentCommunityModifyBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        collection_name = arguments?.getString("collection_name").toString()
        document_name = arguments?.getString("document_name").toString()
        viewmodel.getDocumentPostData(collection_name, document_name).observe(viewLifecycleOwner){ it
            viewbinding.modifyWriteTitle.setText(it.post_title)
            viewbinding.modifyWriteContents.setText(it.post_contents)
        }

    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {

    }

}