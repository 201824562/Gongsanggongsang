package com.example.userapp.ui.main.community.write

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.userapp.MainActivity
import com.example.userapp.R
import com.example.userapp.ui.base.BaseSessionFragment
import com.example.userapp.data.model.PostDataInfo
import com.example.userapp.databinding.FragmentCommunityWriteBinding
import com.example.userapp.ui.main.community.CommunityViewModel
import java.time.LocalDate
import java.time.LocalTime


class CommunityWriteFragment : BaseSessionFragment<FragmentCommunityWriteBinding, CommunityViewModel>() {
    private lateinit var collectionName : String

    override lateinit var viewbinding: FragmentCommunityWriteBinding
    override val viewmodel: CommunityViewModel by viewModels()
    private val categorySpinnerArray = arrayOf("", "소음", "예약", "냉장고", "세탁실", "수질", "와이파이", "전기", "기타")
    private var writePostCategoryData = "none"
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
        viewbinding = FragmentCommunityWriteBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun initViewStart(savedInstanceState: Bundle?) {
        viewmodel.getUserInfo()
        viewmodel.onSuccessGettingUserInfo.observe(this, {
            userAgency = it.agency
            userName = it.nickname
        })

        collectionName = arguments?.getString("getCollectionName").toString()

        val ac = activity as MainActivity
        getLocalPhotoUri = ac.getPhoto()
        getBitmap()

        when(collectionName){
            "1_FREE" -> viewbinding.previewToolbarName.text = "자유게시판"
            "2_EMERGENCY" -> viewbinding.previewToolbarName.text = "긴급게시판"
            "3_SUGGEST" -> viewbinding.previewToolbarName.text = "건의게시판"
            "4_WITH" -> viewbinding.previewToolbarName.text = "함께게시판"
            "5_MARKET" -> viewbinding.previewToolbarName.text = "장터게시판"
        }

        viewbinding.run {
            if(collectionName == "2_EMERGENCY" || collectionName == "3_SUGGEST"){
                initWriteCategorySelect()
            }
            initAttachPhotoRecycler()
        }
        //TODO: mainActivity clear 처리
    }

    override fun initDataBinding(savedInstanceState: Bundle?){
    }

    override fun onDetach() {
        super.onDetach()
        val ac = activity as MainActivity
        ac.selectedItems.clear()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun initViewFinal(savedInstanceState: Bundle?) {

        viewbinding.run {
            writeBackButton.setOnClickListener {
                findNavController().navigate(R.id.action_communityWrite_pop)
            }
            writeAttachPhotoButton.setOnClickListener{
                getPhotoPermission()
            }
            writeRegisterButton.setOnClickListener {
                if(writeTitle.text.toString() == "" || writeContent.text.toString() == ""){
                    showToast("빈 칸을 채워주세요.")
                }
                else if((collectionName == "2_EMERGENCY" || collectionName == "3_SUGGEST") && writePostCategoryData == "none"){
                    showToast("분류를 선택해주세요.")
                }
                else{
                    val postDateNow: String = LocalDate.now().toString()
                    val postTimeNow : String = LocalTime.now().toString()
                    postData = PostDataInfo(
                        collectionName,
                        post_name = userName,
                        post_title = writeTitle.text.toString(),
                        post_contents = writeContent.text.toString(),
                        post_date = postDateNow,
                        post_time = postTimeNow,
                        post_comments = 0,
                        post_id = postDateNow + postTimeNow + userName,
                        post_photo_uri = getLocalPhotoUri,
                        post_state = writePostCategoryData,
                        post_anonymous = false
                    )
                    val bundle = bundleOf(
                        "post_data_info" to postData,
                    )
                    if(uriArray.isEmpty()){
                        viewmodel.insertPostData(postData).observe(viewLifecycleOwner){
                            if(it){
                                findNavController().navigate(R.id.action_communityWrite_to_communityPost, bundle)
                            }
                        }
                    }
                    else{
                        viewmodel.uploadPhoto(bitmapArray, uriArray)
                        viewmodel.getUploadPhoto().observe(viewLifecycleOwner){
                            if(it){
                                viewmodel.insertPostData(postData).observe(viewLifecycleOwner){
                                    if(it){
                                        findNavController().navigate(R.id.action_communityWrite_to_communityPost, bundle)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private val requestLocationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()){ result : Boolean ->
        if (result) getAllPhoto()
        else {
            showSnackbar("권한이 거부 되었습니다.")
        }
    }

    private fun getPhotoPermission(){
        requestPermission(requestLocationPermissionLauncher, Manifest.permission.READ_EXTERNAL_STORAGE,
            "권한이 거부 되었습니다.\n" + "공생공생에서 기기의 사진 및 미디어에 엑세스하도록 허용하시겠습니까?"){
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
        viewbinding.writePhotoRecycler.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context).also { it.orientation = LinearLayoutManager.HORIZONTAL }
            adapter = CommunityAttachPhotoRecyclerAdapter(getLocalPhotoUri)
        }
        attachPostPhotoRecyclerAdapter = CommunityAttachPhotoRecyclerAdapter(getLocalPhotoUri)
        viewbinding.writePhotoRecycler.adapter = attachPostPhotoRecyclerAdapter.apply {
            deleteButtonListener =
                object : CommunityAttachPhotoRecyclerAdapter.OnCommunityPhotoDeleteClickListener {
                    override fun onPhotoDeleteButtonClick(position: Int) {
                        getLocalPhotoUri.removeAt(position)
                        attachPostPhotoRecyclerAdapter.notifyDataSetChanged()
                    }
                }
        }
        attachPostPhotoRecyclerAdapter.notifyDataSetChanged()
    }
    private fun initWriteCategorySelect(){
        viewbinding.run {
            writeCategory.visibility = View.VISIBLE
            writeCategorySelect.adapter = ArrayAdapter(requireContext(), R.layout.fragment_community_write_category_item, categorySpinnerArray)
            writeCategorySelect.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when(position){
                        0 -> writePostCategoryData = "none"
                        1 -> writePostCategoryData = categorySpinnerArray[1]
                        2 -> writePostCategoryData = categorySpinnerArray[2]
                        3 -> writePostCategoryData = categorySpinnerArray[3]
                        4 -> writePostCategoryData = categorySpinnerArray[4]
                        5 -> writePostCategoryData= categorySpinnerArray[5]
                        6 -> writePostCategoryData = categorySpinnerArray[6]
                        7 -> writePostCategoryData = categorySpinnerArray[7]
                        8 -> writePostCategoryData = categorySpinnerArray[8]
                    }
                }
            }
        }
    }

}