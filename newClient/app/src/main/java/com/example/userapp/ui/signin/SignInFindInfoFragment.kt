package com.example.userapp.ui.signin

import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.example.userapp.R
import com.example.userapp.ui.base.BaseSessionFragment
import com.example.userapp.databinding.FragmentSigninFindInfoBinding
import com.example.userapp.restartActivity
import com.example.userapp.ui.main.settings.ChangeInfoType
import com.example.userapp.ui.main.settings.SettingsChangeInfoDetailFragmentArgs
import com.example.userapp.utils.WrapedDialogBasicOneButton
import com.example.userapp.utils.WrapedDialogBasicTwoButton
import com.example.userapp.utils.hideKeyboard

class SignInFindInfoFragment : BaseSessionFragment<FragmentSigninFindInfoBinding, SignInViewModel>() {
    override lateinit var viewbinding: FragmentSigninFindInfoBinding
    override val viewmodel: SignInViewModel by navGraphViewModels(R.id.signInGraph)
    private lateinit var connectionManager : ConnectivityManager
    private var type : FindInfoType = FindInfoType.ID

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewbinding = FragmentSigninFindInfoBinding.inflate(inflater, container, false)
        return viewbinding.root
    }
    private fun setArgVar(){
        viewmodel.run { type = findInfoType }
    }
    override fun initViewStart(savedInstanceState: Bundle?) {
        when (context){
            null -> {
                showToast("에러가 발생했습니다.\n앱을 재부팅합니다.")
                restartActivity()
            }else ->{ connectionManager = requireContext().getSystemService()!! }
        }
        viewbinding.fragmentContent.setOnClickListener { hideKeyboard(it) }
        setArgVar()
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        if (type == FindInfoType.PWD) setPwdView()
        viewmodel.run {
            invalidUserNameEventLiveData.observe(viewLifecycleOwner) {
                when (findInfoType){
                    FindInfoType.ID -> setNameErrorMessage(it)
                    FindInfoType.PWD -> setName2ErrorMessage(it) }
            }
            validUserNameEventLiveData.observe(viewLifecycleOwner){
                when (findInfoType){
                    FindInfoType.ID -> setNameEmptyMessage()
                    FindInfoType.PWD -> setName2EmptyMessage() }
            }
            invalidUserBirthEventLiveData.observe(viewLifecycleOwner) { setBirthInfoErrorMessage(it) }
            validUserBirthEventLiveData.observe(viewLifecycleOwner) { setBirthInfoEmptyMessage() }
            invalidUserSmsEventLiveData.observe(viewLifecycleOwner) { setSmsInfoErrorMessage(it) }
            validUserSmsEventLiveData.observe(viewLifecycleOwner) { setSmsInfoEmptyMessage() }
            invalidUserIdEventLiveData.observe(viewLifecycleOwner) { setIdErrorMessage(it) }
            validUserIdEventLiveData.observe(viewLifecycleOwner){ setIdEmptyMessage() }
            findInfoEventLiveData.observe(viewLifecycleOwner){
                viewmodel.sendFindInfo(getFindInfoType(), getUserName(), getUserBirthday(),
                    getUserSmsInfo(), getUserName2(), getUserId())
            }
            onSuccessFindInfo.observe(viewLifecycleOwner){
                if (it==""){
                    when (findInfoType){
                        FindInfoType.ID -> makeDialog("아이디 찾기에 실패했습니다.\n입력 내용을 확인해주세요.")
                        FindInfoType.PWD -> makeDialog("비밀번호 찾기에 실패했습니다.\n입력 내용을 확인해주세요.")
                    }
                }else {
                    when (findInfoType){
                        FindInfoType.ID -> {
                            setBundleInfo(getUserName(), it)
                            findNavController().navigate(R.id.action_signInFindInfoFragment_to_signInFindInfoResultFragment)
                        }
                        FindInfoType.PWD -> {
                            setBundleInfo(getUserName2(), it)
                            findNavController().navigate(R.id.action_signInFindInfoFragment_to_signInFindInfoResultFragment)
                        }
                    }
                }
            }
        }
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.run {
            editTextName.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus && (getUserName().isEmpty()|| getUserName().isBlank())) {
                    setNameErrorMessage("이름을 입력해주세요.")
                    setBirthInfoEmptyMessage()
                    setSmsInfoEmptyMessage()
                }
            }
            editTextBirthInfo.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus && (getUserBirthday().isEmpty()|| getUserBirthday().isBlank())) {
                    setBirthInfoErrorMessage("8자리 숫자를 입력해주세요.(ex.20000101)")
                    setNameEmptyMessage()
                    setSmsInfoEmptyMessage()
                }
            }
            editTextSmsInfo.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus && (getUserSmsInfo().isEmpty()|| getUserSmsInfo().isBlank())) {
                    setSmsInfoErrorMessage("'-'없이 휴대폰번호 11자리를 입력해주세요.")
                    setNameEmptyMessage()
                    setBirthInfoEmptyMessage()
                }
            }
            editTextName2.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus && (getUserName2().isEmpty()|| getUserName2().isBlank())) {
                    setName2ErrorMessage("이름을 입력해주세요.")
                    setIdEmptyMessage()
                }
            }
            editTextId.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus && (getUserId().isEmpty()|| getUserId().isBlank())) {
                    setIdErrorMessage("아이디를 입력해주세요.")
                    setBirthInfoEmptyMessage()
                    setName2EmptyMessage()
                }
            }
            findInfoBtn.setOnClickListener {
                if (checkServiceState()){ viewmodel.checkForFindInfo(getFindInfoType(), getUserName(), getUserBirthday(),
                    getUserSmsInfo(), getUserName2(), getUserId())  }
                else{ showToast("인터넷 연결이 불안정합니다.\nWifi 상태를 체킹해주세요.") }
            }
        }
    }

    private fun checkServiceState() : Boolean { return connectionManager.activeNetwork != null }

    private fun setPwdView(){
        viewbinding.run {
            title.text = "비밀번호 찾기"
            findInfoBtn.text = "비밀번호 찾기"
            idLayout.visibility = View.GONE
            pwdLayout.visibility = View.VISIBLE
        }
    }

    private fun setNameErrorMessage(message: String) {
        viewbinding.editTextName.setBackgroundResource(R.drawable.edittext_underline_pinkishorange)
        viewbinding.textNameWarning.apply {
            text = message
            visibility = View.VISIBLE
        }
    }
    private fun setNameEmptyMessage() {
        viewbinding.editTextName.setBackgroundResource(R.drawable.edittext_underline_black20)
        viewbinding.textNameWarning.text = "" }
    private fun setBirthInfoErrorMessage(message: String) {
        viewbinding.editTextBirthInfo.setBackgroundResource(R.drawable.edittext_underline_pinkishorange)
        viewbinding.textBirthInfoWarning.apply {
            text = message
            visibility = View.VISIBLE
        }
    }
    private fun setBirthInfoEmptyMessage() {
        viewbinding.editTextBirthInfo.setBackgroundResource(R.drawable.edittext_underline_black20)
        viewbinding.textBirthInfoWarning.text = "" }
    private fun setSmsInfoErrorMessage(message: String) {
        viewbinding.editTextSmsInfo.setBackgroundResource(R.drawable.edittext_underline_pinkishorange)
        viewbinding.textSmsInfoWarning.apply {
            text = message
            visibility = View.VISIBLE
        }
    }
    private fun setSmsInfoEmptyMessage() {
        viewbinding.editTextSmsInfo.setBackgroundResource(R.drawable.edittext_underline_black20)
        viewbinding.textSmsInfoWarning.text = "" }
    private fun setName2ErrorMessage(message: String) {
        viewbinding.editTextName2.setBackgroundResource(R.drawable.edittext_underline_pinkishorange)
        viewbinding.textNameWarning2.apply {
            text = message
            visibility = View.VISIBLE
        }
    }
    private fun setName2EmptyMessage() {
        viewbinding.editTextName2.setBackgroundResource(R.drawable.edittext_underline_black20)
        viewbinding.textNameWarning2.text = "" }
    private fun setIdErrorMessage(message: String) {
        viewbinding.editTextId.setBackgroundResource(R.drawable.edittext_underline_pinkishorange)
        viewbinding.textIdWarning.apply {
            text = message
            visibility = View.VISIBLE
        }
    }
    private fun setIdEmptyMessage() {
        viewbinding.editTextId.setBackgroundResource(R.drawable.edittext_underline_black20)
        viewbinding.textIdWarning.text = "" }

    private fun getFindInfoType() = type
    private fun getUserName() = viewbinding.editTextName.text.toString().trim()
    private fun getUserBirthday() = viewbinding.editTextBirthInfo.text.toString().trim()
    private fun getUserSmsInfo() = viewbinding.editTextSmsInfo.text.toString().trim()
    private fun getUserName2() = viewbinding.editTextName2.text.toString().trim()
    private fun getUserId() = viewbinding.editTextId.text.toString().trim()

    private fun makeDialog(msg : String){
        val dialog = WrapedDialogBasicOneButton(requireContext(), msg).apply {
            clickListener = object : WrapedDialogBasicOneButton.DialogButtonClickListener{
                override fun dialogClickListener() {
                    dismiss()
                }
            }
        }
        showDialog(dialog, viewLifecycleOwner)
    }

    private fun setBundleInfo(userName : String, resultData : String){
        viewmodel.findInfoType = getFindInfoType()
        viewmodel.findInfoUserName = userName
        viewmodel.findInfoResultData = resultData
    }

}