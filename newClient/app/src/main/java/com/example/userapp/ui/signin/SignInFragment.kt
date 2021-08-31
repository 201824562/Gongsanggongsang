package com.example.userapp.ui.signin

import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.userapp.R
import com.example.userapp.base.BaseSessionFragment
import com.example.userapp.data.model.UserStatus
import com.example.userapp.databinding.FragmentSigninBinding
import com.example.userapp.restartActivity
import com.example.userapp.restartActivity
import com.example.userapp.service.getFCMToken
import com.example.userapp.utils.WrapedDialogBasicOneButton
import com.example.userapp.utils.hideKeyboard
import kotlinx.android.parcel.Parcelize

class SignInFragment : BaseSessionFragment<FragmentSigninBinding, SignInViewModel>() {
    override lateinit var viewbinding: FragmentSigninBinding
    override val viewmodel: SignInViewModel by navGraphViewModels(R.id.signInGraph)
    private lateinit var connectionManager : ConnectivityManager

    private var idInfoExist : Boolean = false
    private var pwdInfoExist : Boolean = false
    private var nextBtnAvailable : Boolean = false
    private lateinit var token : String

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentSigninBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        when (context){
            null -> {
                showToast("에러가 발생했습니다.\n앱을 재부팅합니다.")
                restartActivity()
            }else ->{ connectionManager = requireContext().getSystemService()!! }
        }
        viewbinding.fragmentContent.setOnClickListener { hideKeyboard(it) }
        when (context){
            null -> {
                showToast("에러가 발생했습니다.\n앱을 재부팅합니다.")
                restartActivity()
            }else ->{ connectionManager = requireContext().getSystemService()!! }
        }
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.run {
            clearTypeData()
            deviceToken.observe(viewLifecycleOwner){ token = it }
            userStatusEvent.observe(viewLifecycleOwner) {
                when (it) {
                    UserStatus.NOT_USER -> showErrorDialog("로그인에 실패했습니다.\n아이디나 비밀번호를 확인해주세요.")
                    UserStatus.WAIT_APPROVE -> showErrorDialog("가입 승인 대기중이에요.\n공간 관리자님께 문의해주세요.")
                    UserStatus.USER -> viewmodel.saveUserInfo(token)
                    else -> showErrorDialog("오류가 발생했습니다. 다시 시도하거나 문의해주세요.") }
            }
            onSuccessSaveUserInfo.observe(viewLifecycleOwner) { findNavController().navigate(R.id.action_signInFragment_to_mainFragment) }
        }
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.run {
            viewmodel.getDeviceToken()
            findIdBtn.setOnClickListener {
                viewmodel.findInfoType = FindInfoType.ID
                findNavController().navigate(R.id.action_signInFragment_to_signInFindInfoFragment)
            }
            findPwdBtn.setOnClickListener {
                viewmodel.findInfoType = FindInfoType.PWD
                findNavController().navigate(R.id.action_signInFragment_to_signInFindInfoFragment)
            }
            editTextId.addTextChangedListener {
                idInfoExist = !(it.toString().isEmpty() || it.toString().isBlank())
                if (checkInfoExistState()) makeNextButtonAvailable()
                else makeNextButtonNotAvailable()
            }
            editTextPwd.addTextChangedListener {
                pwdInfoExist = !(it.toString().isEmpty() || it.toString().isBlank())
                if (checkInfoExistState()) makeNextButtonAvailable()
                else makeNextButtonNotAvailable()
            }
            loginBtn.setOnClickListener {
                if (checkServiceState()){
                    if (nextBtnAvailable) { viewmodel.sendSignInInfo(getUserId(), getUserPwd(), token) }
                }else{ showToast("인터넷 연결이 불안정합니다.\nWifi 상태를 체킹해주세요.") }
            }
        }
    }

    private fun checkServiceState() : Boolean { return connectionManager.activeNetwork != null }

    private fun getUserId() = viewbinding.editTextId.text.toString().trim()
    private fun getUserPwd() = viewbinding.editTextPwd.text.toString().trim()

    private fun checkInfoExistState() : Boolean { return idInfoExist && pwdInfoExist }
    private fun makeNextButtonAvailable() {
        nextBtnAvailable = true
        viewbinding.loginBtn.setBackgroundResource(R.drawable.button_5dp_rectangle_bluegreen) }
    private fun makeNextButtonNotAvailable() {
        nextBtnAvailable = false
        viewbinding.loginBtn.setBackgroundResource(R.drawable.button_5dp_rectangle_black20) }
    private fun showErrorDialog(errorMessage : String){
        val dialog = WrapedDialogBasicOneButton(requireContext(),  errorMessage).apply {
            clickListener = object : WrapedDialogBasicOneButton.DialogButtonClickListener {
                override fun dialogClickListener() { dismiss() }
            }
        }
        showDialog(dialog, viewLifecycleOwner)
    }
}
@Parcelize
enum class FindInfoType() : Parcelable { ID, PWD }