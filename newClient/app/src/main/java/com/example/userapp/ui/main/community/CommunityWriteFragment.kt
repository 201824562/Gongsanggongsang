package com.example.userapp.ui.main.community

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapRegionDecoder
import android.graphics.ImageDecoder
import android.graphics.Rect
import android.media.Image
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
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.userapp.R
import com.example.userapp.base.BaseFragment
import com.example.userapp.data.entity.PostCommentDataClass
import com.example.userapp.data.model.PostDataInfo
import com.example.userapp.databinding.FragmentCommunityWriteBinding
import java.time.LocalDateTime


class CommunityWriteFragment : BaseFragment<FragmentCommunityWriteBinding, CommunityViewModel>() {
    private lateinit var collection_name : String
    private lateinit var document_name : String
    private lateinit var bundle: Bundle
    override lateinit var viewbinding: FragmentCommunityWriteBinding
    override val viewmodel: CommunityViewModel by navGraphViewModels(R.id.communityWriteGraph)
    private var getPhotoUri : ArrayList<String> = arrayListOf()
    val bitmap_array : ArrayList<Bitmap> = arrayListOf()
    val uri_array : ArrayList<Uri> = arrayListOf()

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
        collection_name = arguments?.getString("collection_name").toString()
        getPhotoUri = viewmodel.getPhoto()

        viewbinding.attachImageRecycler.visibility = View.VISIBLE
        viewbinding.attachImageRecycler.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context).also { it.orientation = LinearLayoutManager.HORIZONTAL }
            adapter = CommunityAttachPhotoRecyclerAdapter(getPhotoUri)
        }
        getBitmap()

    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.run {
            writeRegisterButton.setOnClickListener {
                var time = LocalDateTime.now().toString()
                val post = PostDataInfo(
                    collection_name,
                    "juyong",
                    writeTitle.text.toString(),
                    writeContents.text.toString(),
                    time,
                    arrayListOf(),
                    writeTitle.text.toString() + time,
                    getPhotoUri
                )
                document_name = post.post_title + post.post_date
                viewmodel.uploadPhoto(bitmap_array,uri_array)
                viewmodel.insertPostData(post)

                var bundle = bundleOf(
                    "collection_name" to collection_name,
                    "document_name" to document_name
                )
                findNavController().navigate(R.id.action_communityWrite_to_communityPost, bundle)
            }
            attachImageButton.setOnClickListener {
                getPhotoPermission()
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
            "photoUriArray" to uriArr
        )
        findNavController().navigate(R.id.action_communityWrite_to_communityGetPhoto, bundle)

    }
    @RequiresApi(Build.VERSION_CODES.P)
    private fun getBitmap(){
        val bitmap : Bitmap
        for(photo_uri in getPhotoUri) {
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