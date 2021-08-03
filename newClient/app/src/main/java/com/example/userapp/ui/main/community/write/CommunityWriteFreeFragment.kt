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
import com.example.userapp.databinding.FragmentCommunityWriteFreeBinding
import com.example.userapp.ui.main.community.CommunityViewModel
import java.time.LocalDate
import java.time.LocalTime


class CommunityWriteFreeFragment : BaseFragment<FragmentCommunityWriteFreeBinding, CommunityViewModel>() {
    private val collection_name = "1_free"
    private lateinit var document_name : String
    private lateinit var bundle: Bundle
    override lateinit var viewbinding: FragmentCommunityWriteFreeBinding
    override val viewmodel: CommunityViewModel by viewModels()
    private var getLocalPhotoUri : ArrayList<String> = arrayListOf()
    private lateinit var freePostData : PostDataInfo
    val bitmap_array : ArrayList<Bitmap> = arrayListOf()
    val uri_array : ArrayList<Uri> = arrayListOf()

    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentCommunityWriteFreeBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun initViewStart(savedInstanceState: Bundle?) {
        val ac = activity as MainActivity
        getLocalPhotoUri = ac.getPhoto()
        ac.selectedItems = arrayListOf()
        viewbinding.freeWritePhotoRecycler.visibility = View.VISIBLE
        viewbinding.freeWritePhotoRecycler.run {
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
            freeWritePhoto.setOnClickListener{
                getPhotoPermission()
            }
            freeWriteRegisterButton.setOnClickListener {
                val postDateNow: String = LocalDate.now().toString()
                val postTimeNow : String = LocalTime.now().toString()
                var postThumbnail : String = ""
                if(getLocalPhotoUri.isNotEmpty()){
                    postThumbnail = getLocalPhotoUri.get(0)
                }
                freePostData = PostDataInfo(
                    collection_name,
                    "juyong",
                    post_title = freeWriteTitle.text.toString(),
                    post_contents = freeWriteContent.text.toString(),
                    post_date = postDateNow,
                    post_time = postTimeNow,
                    post_comments = arrayListOf(),
                    post_id = postDateNow + postTimeNow + "juyong",
                    post_photo_uri = getLocalPhotoUri,
                    postThumbnail,
                    post_state = "none",
                    post_anonymous = freeWriteAnonymous.isChecked
                )
                viewmodel.insertPostData(freePostData)
                findNavController().navigate(R.id.action_communityWriteFree_to_communityPreview)
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
        findNavController().navigate(R.id.action_communityWriteFree_to_communityGetPhoto, bundle)

    }
    @RequiresApi(Build.VERSION_CODES.P)
    private fun getBitmap(){
        val bitmap : Bitmap
        for(photo_uri in getLocalPhotoUri) {
            val photoUri = photo_uri.toUri()
            val photoUri2 = Uri.parse("file://" + photoUri)
            uri_array.add(photoUri2)
            val bitmap = ImageDecoder.decodeBitmap(
                ImageDecoder.createSource(
                    requireActivity().contentResolver,
                    photoUri2
                )
            )
            bitmap_array.add(bitmap)
        }
    }

}