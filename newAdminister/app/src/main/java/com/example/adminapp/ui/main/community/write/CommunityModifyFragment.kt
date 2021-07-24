package com.example.adminapp.ui.main.community.write

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.adminapp.base.BaseFragment
import com.example.adminapp.databinding.FragmentCommunityModifyBinding
import com.example.adminapp.ui.main.community.CommunityViewModel

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
            viewbinding.writeModifyTitle.setText(it.post_title)
            viewbinding.writeModifyContents.setText(it.post_contents)
        }
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.writeRegisterModifyButton.setOnClickListener{
            var modifyTitle = viewbinding.writeModifyTitle.text.toString()
            var modifyContent = viewbinding.writeModifyContents.text.toString()
            viewmodel.updatePostData(collection_name, document_name, modifyTitle, modifyContent)
        }
    }

}