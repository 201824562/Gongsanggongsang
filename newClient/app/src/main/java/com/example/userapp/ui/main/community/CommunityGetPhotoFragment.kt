package com.example.userapp.ui.main.community

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.userapp.R
import com.example.userapp.base.BaseFragment
import com.example.userapp.databinding.FragmentCommunityGetPhotoBinding
import com.example.userapp.databinding.FragmentMainhomeCommunityBinding

class CommunityGetPhotoFragment : BaseFragment<FragmentCommunityGetPhotoBinding, CommunityViewModel>() {
    override lateinit var viewbinding: FragmentCommunityGetPhotoBinding
    override val viewmodel: CommunityViewModel by navGraphViewModels(R.id.communityWriteGraph)

    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentCommunityGetPhotoBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        var photoUriArray : ArrayList<String> = arguments?.getStringArrayList("photoUriArray") as ArrayList<String>
        val adapter = context?.let { CommunityGetPhotoGridAdapter(it, photoUriArray, viewmodel) }
        viewbinding.grid.numColumns=3 // 한 줄에 3개씩
        viewbinding.grid.adapter = adapter
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.selectPhotoButton.setOnClickListener{
            findNavController().navigate(R.id.action_communityGetPhoto_to_communityWrite)
        }
    }
}