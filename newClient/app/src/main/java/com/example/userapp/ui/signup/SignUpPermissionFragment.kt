package com.example.userapp.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.userapp.R
import com.example.userapp.base.BaseFragment
import com.example.userapp.databinding.FragmentSignupPermissionBinding
import com.example.userapp.utils.MatchedDialogBasicOneButton

class SignUpPermissionFragment : BaseFragment<FragmentSignupPermissionBinding, SignUpViewModel>() {
    override lateinit var viewbinding: FragmentSignupPermissionBinding
    override val viewmodel: SignUpViewModel by navGraphViewModels(R.id.signUpGraph)

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewbinding = FragmentSignupPermissionBinding.inflate(inflater, container, false)
        return viewbinding.root
    }
    private var nextBtnAvailable : Boolean = false

    override fun initViewStart(savedInstanceState: Bundle?) { getCheckedInfo() }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.run {
            onClickedAllBtn.observe(viewLifecycleOwner){
                when (it) {
                    true -> checkEveryBtn()
                    false -> unCheckEveryBtn(false)
                }
            }
            onClickedFirstBtn.observe(viewLifecycleOwner){
                when (it) {
                    true -> checkFirstBtn()
                    false -> unCheckFirstBtn()
                }
            }
            onClickedSecondBtn.observe(viewLifecycleOwner){
                when (it) {
                    true -> checkSecondBtn()
                    false -> unCheckSecondBtn()
                }
            }
            checkClickedState.observe(viewLifecycleOwner){
                if (viewmodel.checkBtnState()) makeNextButtonAvailable()
                else makeNextButtonNotAvailable()
            }
        }
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
       viewbinding.run {
           permissionAllBtn.setOnClickListener { viewmodel.changeAllBtnValue() }
           permissionFirstBtn.setOnClickListener { viewmodel.changeFirstBtnValue() }
           permissionSecondBtn.setOnClickListener { viewmodel.changeSecondBtnValue() }

           //TODO : 디테일 정보 관련해서 -> 1.내용 추가할 거 있는지 물어보기. 2.버튼요구하기.
           detailInfoFistBtn.setOnClickListener {
               val dialog = MatchedDialogBasicOneButton(requireContext(), "이용약관 동의", getString(R.string.permission_content1)).apply {
                       clickListener = object : MatchedDialogBasicOneButton.DialogButtonClickListener {
                               override fun dialogCloseClickListener() {
                                   dismiss()
                               }
                               override fun dialogClickListener() {
                                   dismiss()
                                   viewmodel.changeFirstBtnValueTrue()
                               }
                           }
                   }
               showDialog(dialog, viewLifecycleOwner)
           }
           detailInfoSecondBtn.setOnClickListener {
               val dialog = MatchedDialogBasicOneButton(requireContext(), "개인정보 수집 및 이용동의", getString(R.string.permission_content2)).apply {
                   clickListener = object : MatchedDialogBasicOneButton.DialogButtonClickListener {
                       override fun dialogCloseClickListener() {
                           dismiss()
                       }
                       override fun dialogClickListener() {
                           dismiss()
                           viewmodel.changeSecondBtnValueTrue()
                       }
                   }
               }
               showDialog(dialog, viewLifecycleOwner)
           }
           signupNextbtn.setOnClickListener {
               if (nextBtnAvailable) findNavController().navigate(R.id.action_signUpPermissionFragment_to_signUpAgencyFragment)
           }
       }
    }

    private fun getCheckedInfo() {
        when (viewmodel.clickedAllBtn) {
            true -> {
                makeNextButtonAvailable()
                checkEveryBtn()
            }
            false -> {
                makeNextButtonNotAvailable()
                unCheckEveryBtn(true)
            }
        }
        when (viewmodel.clickedFirstBtn) {
            true -> {
                checkFirstBtn()
                viewmodel.observeBtnState()
            }
            false -> {
                unCheckFirstBtn()
                viewmodel.observeBtnState()
            }
        }
        when (viewmodel.clickedSecondBtn) {
            true -> {
                checkSecondBtn()
                viewmodel.observeBtnState()
            }
            false -> {
                unCheckSecondBtn()
                viewmodel.observeBtnState()
            }
        }

    }

    private fun checkEveryBtn() {
        checkAllBtn()
        checkFirstBtn()
        checkSecondBtn()
    }

    private fun unCheckEveryBtn(boolean: Boolean) {
        if (boolean){
            unCheckAllBtn()
            unCheckFirstBtn()
            unCheckSecondBtn()
        }
        else {
            unCheckAllBtn()
        }

    }

    private fun checkAllBtn() { viewbinding.permissionAllBtn.setBackgroundResource(R.drawable.ic_checkbox_bluegreen) }
    private fun unCheckAllBtn() { viewbinding.permissionAllBtn.setBackgroundResource(R.drawable.ic_checkbox_gray) }
    private fun checkFirstBtn() { viewbinding.permissionFirstBtn.setBackgroundResource(R.drawable.ic_check_bluegreen) }
    private fun unCheckFirstBtn() { viewbinding.permissionFirstBtn.setBackgroundResource(R.drawable.ic_check_gray) }
    private fun checkSecondBtn() { viewbinding.permissionSecondBtn.setBackgroundResource(R.drawable.ic_check_bluegreen) }
    private fun unCheckSecondBtn() { viewbinding.permissionSecondBtn.setBackgroundResource(R.drawable.ic_check_gray) }

    private fun makeNextButtonAvailable() {
        nextBtnAvailable = true
        viewbinding.signupNextbtn.setBackgroundResource(R.drawable.button_5dp_rectangle_bluegreen)
    }

    private fun makeNextButtonNotAvailable() {
        nextBtnAvailable = false
        viewbinding.signupNextbtn.setBackgroundResource(R.drawable.button_5dp_rectangle_black20)
    }

}