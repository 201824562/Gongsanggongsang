package com.example.userapp.ui.mainhome.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.userapp.R
import com.example.userapp.base.BaseFragment
import com.example.userapp.data.model.PostDataInfo
import com.example.userapp.data.repository.PostDataRepository
import com.example.userapp.databinding.FragmentCommunityPreviewBinding


class CommunityMainRecyclerAdapter : BaseFragment<FragmentCommunityPreviewBinding, CommunityViewModel>() {
    override lateinit var viewbinding: FragmentCommunityPreviewBinding

    override val viewmodel : CommunityViewModel by viewModels()

    private lateinit var adapter: CommunityPreviewRecyclerAdapter

    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentCommunityPreviewBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        var collection_name= arguments?.getString("collection_name").toString()
        var bundle = bundleOf(
            "collection_name" to collection_name
        )
        initRecyclerView(collection_name)
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {

    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        var collection_name= arguments?.getString("collection_name").toString()
        var bundle = bundleOf(
            "collection_name" to collection_name
        )
        viewbinding.communityWriteButton.setOnClickListener{
            findNavController().navigate(R.id.action_communityPreview_to_communityWrite, bundle)

        }
    }
    fun initRecyclerView(collection_name : String){
        var list : ArrayList<PostDataInfo> = ArrayList()

        viewbinding.communityPreviewRecyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = CommunityPreviewRecyclerAdapter(list)
        }
        viewmodel.getCollectionPostData(collection_name).observe(viewLifecycleOwner){ it
            list.clear()
            list.addAll(it)
            adapter = CommunityPreviewRecyclerAdapter(it)
            viewbinding.communityPreviewRecyclerView.adapter = adapter.apply {
                listener =
                    object : CommunityPreviewRecyclerAdapter.OnCommunityMarketItemClickListener {
                        override fun onPreviewItemClick(position: Int) {
                            var document_name = getItem(position).post_id
                            var bundle = bundleOf(
                                "collection_name" to collection_name,
                                "document_name" to document_name
                            )
                            findNavController().navigate(R.id.action_communityPreview_to_communityPost, bundle)
                        }
                    }
            }
            adapter.notifyDataSetChanged()
        }

    }

}