package com.example.userapp.ui.main.community.write

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.userapp.MainActivity
import com.example.userapp.MainActivityViewModel
import com.example.userapp.R
import com.example.userapp.base.BaseFragment
import com.example.userapp.databinding.FragmentCommunityGetPhotoBinding
import com.example.userapp.ui.main.MainViewModel
import com.example.userapp.ui.main.community.CommunityViewModel
import com.example.userapp.ui.main.community.preview.CommunityPreviewMarketRecyclerAdapter

class CommunityGetPhotoFragment : BaseFragment<FragmentCommunityGetPhotoBinding, CommunityViewModel>() {
    override lateinit var viewbinding: FragmentCommunityGetPhotoBinding
    override val viewmodel : CommunityViewModel by viewModels()

    lateinit var collection_name : String
    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentCommunityGetPhotoBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        collection_name = arguments?.getString("collection_name").toString()
        var ac = activity as MainActivity
        var photoUriArray : ArrayList<String> = arguments?.getStringArrayList("photoUriArray") as ArrayList<String>
        val adapter = context?.let { CommunityGetPhotoGridAdapter(it, photoUriArray, viewmodel) }
            ?.apply {
                listener =
                    object :
                        CommunityGetPhotoGridAdapter.OnCommunityLocalPhotoItemClickListener {
                        override fun onLocalPhotoItemClick(position: Int) {
                            let {
                                ac.selected_items.add(getItem(position))
                            }
                            System.out.println(getItem(position))
                        }
                }
            }
        viewbinding.grid.numColumns=3 // 한 줄에 3개씩
        viewbinding.grid.adapter = adapter

    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.selectPhotoButton.setOnClickListener{
            var uriArray : ArrayList<String> = viewmodel.getPhoto()
            var bundle = bundleOf(
                "uriArray" to uriArray
            )
            when(collection_name){
                "1_free" -> findNavController().navigate(R.id.action_communityGetPhoto_to_communityWriteFree)
                "2_emergency" -> findNavController().navigate(R.id.action_communityGetPhoto_to_communityWriteEmergency)
                "3_suggest" -> findNavController().navigate(R.id.action_communityGetPhoto_to_communityWriteSuggest)
                "4_with" -> findNavController().navigate(R.id.action_communityGetPhoto_to_communityWriteWith)
                "5_market" -> findNavController().navigate(R.id.action_communityGetPhoto_to_communityWriteMarket)
            }
        }
    }
}