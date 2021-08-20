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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.userapp.MainActivity
import com.example.userapp.R
import com.example.userapp.base.BaseFragment
import com.example.userapp.data.model.PostDataInfo
import com.example.userapp.databinding.FragmentCommunityWriteWithBinding
import com.example.userapp.ui.main.community.CommunityViewModel
import com.example.userapp.utils.WrapedCommunityDialogBasicOneButton
import java.time.LocalDate
import java.time.LocalTime


class CommunityWriteWithFragment : BaseFragment<FragmentCommunityWriteWithBinding, CommunityViewModel>() {
    private val collection_name = "4_with"
    private lateinit var bundle: Bundle
    override lateinit var viewbinding: FragmentCommunityWriteWithBinding
    override val viewmodel: CommunityViewModel by viewModels()
    private lateinit var withPostData : PostDataInfo
    private var getLocalPhotoUri : ArrayList<String> = arrayListOf()
    private val bitmapArray : ArrayList<Bitmap> = arrayListOf()
    private val uriArray : ArrayList<Uri> = arrayListOf()
    private lateinit var userName : String
    private lateinit var userAgency: String
    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentCommunityWriteWithBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun initViewStart(savedInstanceState: Bundle?) {
        val ac = activity as MainActivity
        userAgency = ac.getUserData()!!.agency
        userName  = ac.getUserData()!!.nickname
        getLocalPhotoUri = ac.getPhoto()
        viewbinding.withWritePhotoRecycler.visibility = View.VISIBLE
        viewbinding.withWritePhotoRecycler.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context).also { it.orientation = LinearLayoutManager.HORIZONTAL }
            adapter = CommunityAttachPhotoRecyclerAdapter(getLocalPhotoUri)
        }
        getBitmap()
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.run {
            withWritePhoto.setOnClickListener{
                getPhotoPermission()
            }
            withWriteRegisterButton.setOnClickListener {
                if(withWriteTitle.text.toString() == "" || withWriteContent.text.toString() == ""){

                }
                else{
                    val postDateNow: String = LocalDate.now().toString()
                    val postTimeNow : String = LocalTime.now().toString()
                    var postAnonymous : Boolean = false
                    if(withWriteAnonymous.isChecked){
                        postAnonymous = true
                    }
                    withPostData = PostDataInfo(
                        collection_name,
                        userName,
                        post_title = withWriteTitle.text.toString(),
                        post_contents = withWriteContent.text.toString(),
                        post_date = postDateNow,
                        post_time = postTimeNow,
                        post_comments = 0,
                        post_id = postDateNow + postTimeNow + userName,
                        post_photo_uri = getLocalPhotoUri,
                        post_state = "모집 중",
                        post_anonymous = postAnonymous
                    )
                    bundle = bundleOf(
                        "collection_name" to collection_name,
                        "document_name" to withPostData.post_id
                    )
                    viewmodel.insertPostData(userAgency, withPostData).observe(viewLifecycleOwner){
                        if(it){
                            findNavController().navigate(R.id.action_communityWriteWith_to_communityPost, bundle)
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
        System.out.println("get");
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
            "collection_name" to collection_name,
            "photoUriArray" to uriArr
        )
        findNavController().navigate(R.id.action_communityWriteWith_to_communityGetPhoto, bundle)

    }
    @RequiresApi(Build.VERSION_CODES.P)
    private fun getBitmap(){
        val bitmap : Bitmap
        for(photo_uri in getLocalPhotoUri) {
            val photoUri = photo_uri.toUri()
            val photoUri2 = Uri.parse("file://" + photoUri)
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
    private fun makeDialog(msg : String){
        val dialog = WrapedCommunityDialogBasicOneButton(requireContext(), msg).apply {
            clickListener = object : WrapedCommunityDialogBasicOneButton.DialogButtonClickListener{
                override fun dialogClickListener() {
                    dismiss()
                }
            }
        }
        showDialog(dialog, viewLifecycleOwner)
    }

}