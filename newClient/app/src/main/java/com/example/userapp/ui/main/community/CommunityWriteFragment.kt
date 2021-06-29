package com.example.userapp.ui.main.community

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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
    private val OPEN_GALLERY = 1
    override val viewmodel: CommunityViewModel by viewModels()


    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentCommunityWriteBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        collection_name = arguments?.getString("collection_name").toString()
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
                    ArrayList<PostCommentDataClass>(),
                    writeTitle.text.toString() + time
                )
                document_name = post.post_title + post.post_date
                viewmodel.insertPostData(post)

                var bundle = bundleOf(
                    "collection_name" to collection_name,
                    "document_name" to document_name
                )
                findNavController().navigate(R.id.action_communityWrite_to_communityPost, bundle)
            }
            attachImageButton.setOnClickListener {
                /*val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.setType("image/*")
                startActivityForResult(intent, OPEN_GALLERY)*/
*/
                getPhotoPermission()
            }
        }
    }

    private val requestLocationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()){ result : Boolean ->
        if (result) //getAllPhoto()
        else showSnackbar("권한이 거부되었습니다.")
    }

    private fun getPhotoPermission(){
        requestPermission(requestLocationPermissionLauncher, Manifest.permission.READ_EXTERNAL_STORAGE,
            "권한이 거부되었습니다."){
            //getAllPhoto()
        }
    }

    /*private fun getAllPhoto(){
        val cursor = ContentResolver.query(
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
            }
            cursor.close()
        }
        val adapter = MyAdapter(this,uriArr)
        gridView.numColumns=3 // 한 줄에 3개씩
        gridView.adapter = adapter
    }*/


}