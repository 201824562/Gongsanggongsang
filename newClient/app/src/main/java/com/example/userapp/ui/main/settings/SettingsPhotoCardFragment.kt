package com.example.userapp.ui.main.settings

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.userapp.R
import com.example.userapp.data.model.PhotoCardInfo
import com.example.userapp.databinding.FragmentSettingsPhotoCardBinding
import com.example.userapp.ui.base.BaseSessionFragment
import com.example.userapp.ui.main.home.HomeViewModel
import com.example.userapp.utils.WrapedDialogBasicTwoButton
import java.time.LocalDateTime

class SettingsPhotoCardFragment : BaseSessionFragment<FragmentSettingsPhotoCardBinding, HomeViewModel>() {
    override lateinit var viewbinding: FragmentSettingsPhotoCardBinding
    override val viewmodel: HomeViewModel by viewModels()

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewbinding = FragmentSettingsPhotoCardBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        viewbinding.backBtn.setOnClickListener { findNavController().navigate(R.id.action_settingsPhotoCardFragment_pop) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.getUserPhotoCardData().observe(viewLifecycleOwner){
            if (it.exists){
                initView(it.data!!.job, it.data.email, it.data.introduce)
            }
        }
        viewmodel.onSuccessGettingUserInfo.observe(viewLifecycleOwner){
            val photoCardInfo = PhotoCardInfo("없음", it.id, it.name, it.nickname, viewbinding.editTextPhotoCardJob.text.toString(),
                viewbinding.editTextPhotoCardEmail.text.toString(), viewbinding.editTextPhotoCardIntroduce.text.toString() , LocalDateTime.now().toString())
            makeDialog(PhotoCardProgress.DONE, photoCardInfo, "등록시, 타인에게 공개되는 소개카드가 생성됩니다.\n계속 진행하시겠습니까?", "등록")
        }
        viewmodel.onSuccessSavePhotoCardData.observe(viewLifecycleOwner){
            findNavController().navigate(R.id.action_settingsPhotoCardFragment_pop)
        }
        viewmodel.onSuccessDeletePhotoCardData.observe(viewLifecycleOwner){
            findNavController().navigate(R.id.action_settingsPhotoCardFragment_pop)
        }
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.photoCardAddBtn.setOnClickListener { checkEditTextValues() }
        viewbinding.photoCardDeleteBtn.setOnClickListener {
            makeDialog(PhotoCardProgress.DELETE, null,
                "삭제시, 타인에게 공개되는 소개카드가 삭제됩니다.\n계속 진행하시겠습니까?", "삭제")
        }

    }

    private fun initView(job : String, email : String, introduce : String){
        viewbinding.run {
            editTextPhotoCardJob.setText(job)
            editTextPhotoCardEmail.setText(email)
            editTextPhotoCardIntroduce.setText(introduce)
            photoCardDeleteBtn.visibility = View.VISIBLE
        }
    }
    private fun checkEditTextValues(){
        viewbinding.run {
            if (editTextPhotoCardJob.text.isEmpty() || editTextPhotoCardJob.text.isBlank()) showToast("소속/직업을 입력해주세요.")
            else if (editTextPhotoCardEmail.text.isEmpty() || editTextPhotoCardEmail.text.isBlank()) showToast("이메일을 입력해주세요.")
            else if (editTextPhotoCardIntroduce.text.isEmpty() ||  editTextPhotoCardIntroduce.text.isBlank()) showToast("소개를 입력해주세요.")
            else viewmodel.getUserInfo()
        }
    }

    private fun makeDialog(type : PhotoCardProgress, data : PhotoCardInfo?, msg : String, customBtn : String){
        val dialog = WrapedDialogBasicTwoButton(requireContext(), msg, "취소", customBtn).apply {
            clickListener = object : WrapedDialogBasicTwoButton.DialogButtonClickListener{
                override fun dialogCloseClickListener() {
                    dismiss()
                }
                override fun dialogCustomClickListener() {
                    if (type == PhotoCardProgress.DELETE){
                        viewmodel.deleteUserPhotoCard()
                    }else {
                        if (data != null) {
                            viewmodel.saveUserPhotoCard(data) }
                        else showToast("입력되지 않은 정보가 있는 경우, 완료하실 수 없습니다.")
                    }
                    dismiss()
                }
            }
        }
        showDialog(dialog, viewLifecycleOwner)
    }
}

enum class PhotoCardProgress{
    DELETE, DONE
}