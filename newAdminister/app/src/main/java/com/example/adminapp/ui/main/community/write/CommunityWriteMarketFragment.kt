package com.example.adminapp.ui.main.community.write

import android.Manifest
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.adminapp.MainActivity
import com.example.adminapp.R
import com.example.adminapp.base.BaseFragment
import com.example.adminapp.base.BaseSessionFragment
import com.example.adminapp.data.model.PostDataInfo
import com.example.adminapp.databinding.FragmentCommunityWriteBinding
import com.example.adminapp.databinding.FragmentCommunityWriteMarketBinding
import com.example.adminapp.ui.main.community.CommunityViewModel
import java.time.LocalDate
import java.time.LocalTime


class CommunityWriteMarketFragment : BaseSessionFragment<FragmentCommunityWriteMarketBinding, CommunityViewModel>() {
    private lateinit var collectionName : String
    private lateinit var documentName : String
    private lateinit var bundle: Bundle
    override lateinit var viewbinding: FragmentCommunityWriteMarketBinding
    override val viewmodel: CommunityViewModel by viewModels()
    private lateinit var postData : PostDataInfo
    private lateinit var attachPostPhotoRecyclerAdapter : CommunityAttachPhotoRecyclerAdapter
    private var getLocalPhotoUri : ArrayList<String> = arrayListOf()
    private val bitmapArray : ArrayList<Bitmap> = arrayListOf()
    private val uriArray : ArrayList<Uri> = arrayListOf()
    private lateinit var userAgency : String
    lateinit var userName : String

    var postPhotoUri : ArrayList<String> = arrayListOf()
    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentCommunityWriteMarketBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun initViewStart(savedInstanceState: Bundle?) {
        val ac : MainActivity = activity as MainActivity
        userAgency = ac.getAdminData()!!.agency
        userName = ac.getAdminData()!!.name
        collectionName = arguments?.getString("collection_name").toString()
        documentName = arguments?.getString("postId").toString()
        bundle = bundleOf(
            "collection_name" to collectionName
        )

        getLocalPhotoUri = ac.getPhoto()
        getBitmap()
        initAttachPhotoRecycler()
    }

    override fun initDataBinding(savedInstanceState: Bundle?){
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun initViewFinal(savedInstanceState: Bundle?) {

        viewbinding.run {
            marketWriteBackButton.setOnClickListener {
                findNavController().navigate(R.id.action_communityMarketWrite_pop)
            }
            marketWritePhoto.setOnClickListener{
                getPhotoPermission()
            }
            marketWriteRegisterButton.setOnClickListener {
                if(marketWriteTitle.text.toString() == "" || marketWriteContent.text.toString() == ""){
                    showToast("빈 칸을 채워주세요.")
                }
                else{
                    val postDateNow: String = LocalDate.now().toString()
                    val postTimeNow : String = LocalTime.now().toString()
                    postData = PostDataInfo(
                        collectionName,
                        post_name = userName,
                        post_title = marketWriteTitle.text.toString(),
                        post_contents = marketWriteContent.text.toString(),
                        post_date = postDateNow,
                        post_time = postTimeNow,
                        post_comments = 0,
                        post_id = postDateNow + postTimeNow + userName,
                        post_photo_uri = getLocalPhotoUri,
                        post_state = "판매 중",
                        post_anonymous = false
                    )
                    bundle = bundleOf(
                        "collection_name" to collectionName,
                        "document_name" to postData.post_id
                    )
                    viewmodel.insertPostData(userAgency, postData).observe(viewLifecycleOwner){
                        if(it){
                            findNavController().navigate(R.id.action_communityWriteMarket_to_communityPostMarket, bundle)
                        }
                    }
                }
            }
        }
    }

    private val requestLocationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()){ result : Boolean ->
        if (result) getAllPhoto()
        else showSnackbar("권한이 거부되었습니다.")
    }

    private fun getPhotoPermission(){
        requestPermission(requestLocationPermissionLauncher, Manifest.permission.READ_EXTERNAL_STORAGE,
            "권한이 거부되었습니다."){
            getAllPhoto()
        }
    }

    private fun getAllPhoto(){
        val cursor = activity?.contentResolver?.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC")
        val uriArr = ArrayList<String>()
        if(cursor!=null){
            while(cursor.moveToNext()){
                // 사진 경로 Uri 가져오기
                val uri = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                uriArr.add(uri)
                System.out.println(uri);
            }
            cursor.close()
        }
        val bundle = bundleOf(
            "collection_name" to collectionName,
            "photoUriArray" to uriArr
        )
        findNavController().navigate(R.id.action_communityWrite_to_communityGetPhoto, bundle)

    }
    @RequiresApi(Build.VERSION_CODES.P)
    private fun getBitmap(){
        val bitmap : Bitmap
        for(photo_uri in getLocalPhotoUri) {
            val photoUri = photo_uri.toUri()
            val photoUri2 = Uri.parse("file://$photoUri")
            uriArray.add(photoUri2)
            val bitmap = ImageDecoder.decodeBitmap(
                ImageDecoder.createSource(
                    requireActivity().contentResolver,
                    photoUri2
                )
            )
            bitmapArray.add(bitmap)
        }
    }

    private fun initAttachPhotoRecycler() {
        attachPostPhotoRecyclerAdapter = CommunityAttachPhotoRecyclerAdapter(getLocalPhotoUri)
        viewbinding.marketWritePhotoRecycler.adapter = attachPostPhotoRecyclerAdapter.apply {
            deleteButtonListener =
                object : CommunityAttachPhotoRecyclerAdapter.OnCommunityPhotoDeleteClickListener {
                    override fun onPhotoDeleteButtonClick(position: Int) {
                        getLocalPhotoUri.removeAt(position)
                        attachPostPhotoRecyclerAdapter.notifyDataSetChanged()
                    }

                }
        }
    }

}