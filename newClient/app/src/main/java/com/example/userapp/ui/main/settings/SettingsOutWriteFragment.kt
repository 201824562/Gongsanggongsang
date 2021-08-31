package com.example.userapp.ui.main.settings

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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.userapp.MainActivity
import com.example.userapp.R
import com.example.userapp.base.BaseSessionFragment
import com.example.userapp.data.model.PostDataInfo
import com.example.userapp.databinding.FragmentSettingsOutWriteBinding
import com.example.userapp.ui.main.community.CommunityViewModel
import com.example.userapp.ui.main.community.write.CommunityAttachPhotoRecyclerAdapter
import java.time.LocalDate
import java.time.LocalTime


class SettingsOutWriteFragment : BaseSessionFragment<FragmentSettingsOutWriteBinding, CommunityViewModel>() {
    private lateinit var collectionName : String
    private lateinit var documentName : String
    private lateinit var bundle: Bundle
    override lateinit var viewbinding: FragmentSettingsOutWriteBinding
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
        viewbinding = FragmentSettingsOutWriteBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun initViewStart(savedInstanceState: Bundle?) {
        viewmodel.getUserInfo()
        viewmodel.onSuccessGettingUserInfo.observe(this, {
            userAgency = it.agency
            userName = it.name
        })
        val ac : MainActivity = activity as MainActivity
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
                findNavController().navigate(R.id.action_settingsOutWriteFragment_self)
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
                        "OUT",
                        post_name = userName,
                        post_title = marketWriteTitle.text.toString(),
                        post_contents = marketWriteContent.text.toString(),
                        post_date = postDateNow,
                        post_time = postTimeNow,
                        post_comments = 0,
                        post_id = postDateNow + postTimeNow + userName,
                        post_photo_uri = getLocalPhotoUri,
                        post_state = marketWritePrice.text.toString(),
                        post_anonymous = false
                    )
                    bundle = bundleOf(
                        "post_data_info" to postData
                    )
                    viewmodel.insertPostData(postData).observe(viewLifecycleOwner){
                        if(it){
                            findNavController().navigate(R.id.action_settingOutWrite_to_settingOutLog)
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
            "photoUriArray" to uriArr
        )
        findNavController().navigate(R.id.action_settingsOutWrite_to_getPhoto, bundle)

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
        viewbinding.marketWritePhotoRecycler.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context).also { it.orientation = LinearLayoutManager.HORIZONTAL }
            adapter = CommunityAttachPhotoRecyclerAdapter(getLocalPhotoUri)
        }
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
