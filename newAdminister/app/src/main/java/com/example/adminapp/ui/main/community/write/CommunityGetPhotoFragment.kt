package com.example.adminapp.ui.main.community.write

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.adminapp.MainActivity
import com.example.adminapp.R
import com.example.adminapp.base.BaseFragment
import com.example.adminapp.databinding.FragmentCommunityGetPhotoBinding
import com.example.adminapp.ui.main.community.CommunityViewModel

class CommunityGetPhotoFragment : BaseFragment<FragmentCommunityGetPhotoBinding, CommunityViewModel>() {
    override lateinit var viewbinding: FragmentCommunityGetPhotoBinding
    override val viewmodel : CommunityViewModel by viewModels()
    private var localSelectedPhotoItem : ArrayList<String> = arrayListOf()
    lateinit var collection_name : String
    var howManyPhoto  = 0
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
        var photoUriArray : ArrayList<String> = arguments?.getStringArrayList("photoUriArray") as ArrayList<String>
        val adapter = context?.let { CommunityGetPhotoGridAdapter(it, photoUriArray, viewmodel) }
            ?.apply {
                listener =
                    object :
                        CommunityGetPhotoGridAdapter.OnCommunityLocalPhotoItemClickListener {
                        override fun onLocalPhotoItemClick(position: Int) {
                            let {
                                if(getItem(position) in localSelectedPhotoItem){
                                    localSelectedPhotoItem.remove(getItem(position))
                                    howManyPhoto--
                                    viewbinding.selectHowmanyPhoto.setText(howManyPhoto.toString())
                                }
                                else{
                                    localSelectedPhotoItem.add(getItem(position))
                                    howManyPhoto++
                                    viewbinding.selectHowmanyPhoto.setText(howManyPhoto.toString())
                                }
                            }

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
            if(howManyPhoto <= 5){
                var ac = activity as MainActivity
                ac.selectedItems = this.localSelectedPhotoItem
                findNavController().navigate(R.id.action_communityGetPhotoFragment_pop)
            }
            //TODO: 커스텀 토스트로 바꾸기
            else{
                Toast.makeText(context, "허용 가능한 사진 수를 초과하였습니다.", Toast.LENGTH_SHORT)
            }
        }
    }
}