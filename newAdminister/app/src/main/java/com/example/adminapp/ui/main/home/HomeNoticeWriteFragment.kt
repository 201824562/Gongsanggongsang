package com.example.adminapp.ui.main.home

import android.Manifest
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
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
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminapp.MainActivity
import com.example.adminapp.R
import com.example.adminapp.base.BaseSessionFragment
import com.example.adminapp.data.model.AlarmItem
import com.example.adminapp.data.model.PostDataInfo
import com.example.adminapp.data.model.RemoteUserInfo
import com.example.adminapp.databinding.FragmentCommunityWriteBinding
import com.example.adminapp.databinding.FragmentMainhomeHomeNoticeWriteBinding
import com.example.adminapp.ui.main.community.CommunityViewModel
import com.example.adminapp.ui.main.community.write.CommunityAttachPhotoRecyclerAdapter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class HomeNoticeWriteFragment : BaseSessionFragment<FragmentMainhomeHomeNoticeWriteBinding, CommunityViewModel>() {
    private lateinit var collectionName : String

    override lateinit var viewbinding: FragmentMainhomeHomeNoticeWriteBinding
    override val viewmodel: CommunityViewModel by viewModels()
    private val categorySpinnerArray = arrayOf("", "공지", "행사", "기타")
    private var writePostCategoryData = "none"
    private lateinit var postData : PostDataInfo
    private lateinit var attachPostPhotoRecyclerAdapter : CommunityAttachPhotoRecyclerAdapter
    private var getLocalPhotoUri : ArrayList<String> = arrayListOf()
    private val bitmapArray : ArrayList<Bitmap> = arrayListOf()
    private val uriArray : ArrayList<Uri> = arrayListOf()

    var postPhotoUri : ArrayList<String> = arrayListOf()
    private var tokenTitle = "공지"
    private var getUserToken : ArrayList<RemoteUserInfo> = arrayListOf()
    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentMainhomeHomeNoticeWriteBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun initViewStart(savedInstanceState: Bundle?) {

        val ac = activity as MainActivity
        getLocalPhotoUri = ac.getPhoto()
        getBitmap()

        initWriteCategorySelect()
        initAttachPhotoRecycler()
        //TODO: mainActivity clear 처리
    }

    override fun initDataBinding(savedInstanceState: Bundle?){
        viewmodel.getAllUserIdToken().observe(viewLifecycleOwner) {
            viewmodel.getTokenArrayList = MutableLiveData()
            Log.e("checkck", "{$it}")
            getUserToken = it
        }
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
                findNavController().navigate(R.id.action_homeNoticeWriteFragment_pop)
            }
            writePhoto.setOnClickListener{
                getPhotoPermission()
            }
            writeRegisterButton.setOnClickListener {
                if(writeTitle.text.toString() == "" || writeContent.text.toString() == "" || writePostCategoryData == "none"){
                    showToast("빈 칸을 채워주세요.")
                }
                else{
                    val postDateNow: String = LocalDate.now().toString()
                    val postTimeNow : String = LocalTime.now().toString()
                    postData = PostDataInfo(
                        "notice",
                        "관리자",
                        post_title = writeTitle.text.toString(),
                        post_contents = writeContent.text.toString(),
                        post_date = postDateNow,
                        post_time = postTimeNow,
                        post_comments = 0,
                        post_id = postDateNow + postTimeNow + "관리자",
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
                                for(user in getUserToken){
                                    for(token in user.fcmToken){
                                        viewmodel.registerNotificationToFireStore(tokenTitle, tokenTitle + "게시판에 올린 글에 답변이 달렸어요!", token)
                                    }
                                    Log.e("chekckcck", "{$user.id}")
                                    val documentId = LocalDateTime.now().toString() + "notice" + "관리자"  //TODO : 날짜 + 타입 + 보내는사람닉네임
                                    val data = AlarmItem(documentId,
                                        LocalDateTime.now().toString(),
                                        user.id,
                                        "공지사항이 있어요!", tokenTitle, null,
                                        postData.makeToPostAlarmData(),
                                        null)
                                    viewmodel.registerAlarmData(user.id, documentId, data)
                                }
                                viewmodel.onSuccessRegisterAlarmData.observe(viewLifecycleOwner){
                                    findNavController().navigate(R.id.action_homeNoticeWrite_to_homeNoticePost, bundle)
                                }
                            }
                        }
                    }
                    else{
                        viewmodel.uploadPhoto(bitmapArray, uriArray)
                        viewmodel.getUploadPhoto().observe(viewLifecycleOwner){
                            if(it){
                                viewmodel.insertPostData(postData).observe(viewLifecycleOwner){
                                    if(it){
                                        for(user in getUserToken){
                                            for(token in user.fcmToken){
                                                viewmodel.registerNotificationToFireStore(tokenTitle, tokenTitle + "게시판에 올린 글에 답변이 달렸어요!", token)
                                            }
                                            Log.e("chekckcck", "{$user.id}")
                                            val documentId = LocalDateTime.now().toString() + collectionName + "관리자"  //TODO : 날짜 + 타입 + 보내는사람닉네임
                                            val data = AlarmItem(documentId,
                                                LocalDateTime.now().toString(),
                                                user.id,
                                                "공지사항 어요!", tokenTitle, null,
                                                    postData.makeToPostAlarmData(),
                                                null)
                                            viewmodel.registerAlarmData(user.id, documentId, data)
                                        }
                                        viewmodel.onSuccessRegisterAlarmData.observe(viewLifecycleOwner){
                                            findNavController().navigate(R.id.action_homeNoticeWrite_to_homeNoticePost, bundle)
                                        }
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
            showSnackbar("권한이 거부되었습니다.")
            showPermissionRationale("")
        }
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
            "collection_name" to "notice",
            "photoUriArray" to uriArr
        )
        findNavController().navigate(R.id.action_homeNoticeWriteFragment_to_homeNoticePhotoFragment, bundle)

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
                    }
                }
            }
        }
    }

}