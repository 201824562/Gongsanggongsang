package com.example.userapp.ui.signup

import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.userapp.R
import com.example.userapp.ui.base.BaseFragment
import com.example.userapp.ui.base.BaseSessionFragment
import com.example.userapp.databinding.FragmentSignupPermissionBinding
import com.example.userapp.restartActivity
import com.example.userapp.utils.MatchedFullDialogBasicOneButton

class SignUpPermissionFragment : BaseSessionFragment<FragmentSignupPermissionBinding, SignUpViewModel>() {
    override lateinit var viewbinding: FragmentSignupPermissionBinding
    override val viewmodel: SignUpViewModel by navGraphViewModels(R.id.signUpGraph)
    private lateinit var connectionManager : ConnectivityManager

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewbinding = FragmentSignupPermissionBinding.inflate(inflater, container, false)
        return viewbinding.root
    }
    private var nextBtnAvailable : Boolean = false

    override fun initViewStart(savedInstanceState: Bundle?) {
        viewmodel.cameFromPermission = true
        getCheckedInfo()
        when (context){
            null -> {
                showToast("에러가 발생했습니다.\n앱을 재부팅합니다.")
                restartActivity()
            }else ->{ connectionManager = requireContext().getSystemService()!! }
        }
    }

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
            checkPermissionClickedState.observe(viewLifecycleOwner){
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
           detailInfoFirstLayout.setOnClickListener { makeFirstDialog() }
           detailInfoFistBtn.setOnClickListener { makeFirstDialog() }
           detailInfoSecondLayout.setOnClickListener { makeSecondDialog()  }
           detailInfoSecondBtn.setOnClickListener { makeSecondDialog() }
           signupNextbtn.setOnClickListener {
               if (nextBtnAvailable) {
                   if (checkServiceState()){
                       if (nextBtnAvailable) { findNavController().navigate(SignUpPermissionFragmentDirections.
                       actionSignUpPermissionFragmentToSignUpAgencyFragment(true)) }
                   }else{ showToast("인터넷 연결이 불안정합니다.\nWifi 상태를 체킹해주세요.") }
               }
           }
       }
    }
    private fun checkServiceState() : Boolean { return connectionManager.activeNetwork != null }

    private fun makeFirstDialog(){
        val dialog = MatchedFullDialogBasicOneButton(requireContext(), "이용약관 동의", getString(R.string.permission_content1)).apply {
            clickListener = object : MatchedFullDialogBasicOneButton.DialogButtonClickListener {
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
    private fun makeSecondDialog(){
        val dialog = MatchedFullDialogBasicOneButton(requireContext(), "개인정보 수집 및 이용동의", getString(R.string.permission_content2)).apply {
            clickListener = object : MatchedFullDialogBasicOneButton.DialogButtonClickListener {
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
                viewmodel.observePermissionBtnState()
            }
            false -> {
                unCheckFirstBtn()
                viewmodel.observePermissionBtnState()
            }
        }
        when (viewmodel.clickedSecondBtn) {
            true -> {
                checkSecondBtn()
                viewmodel.observePermissionBtnState()
            }
            false -> {
                unCheckSecondBtn()
                viewmodel.observePermissionBtnState()
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