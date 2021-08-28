package com.example.userapp.ui.main.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.userapp.R
import com.example.userapp.base.BaseSessionFragment
import com.example.userapp.databinding.FragmentSettingsChangeInfoBinding
import com.example.userapp.databinding.FragmentSettingsChangeInfoDetailBinding
import com.example.userapp.utils.hideKeyboard

//Not Using
class SettingsChangeInfoDetailFragment : BaseSessionFragment<FragmentSettingsChangeInfoDetailBinding, SettingsViewModel>() {

    override lateinit var viewbinding: FragmentSettingsChangeInfoDetailBinding
    override val viewmodel: SettingsViewModel by viewModels()
    private val args : SettingsChangeInfoDetailFragmentArgs by navArgs()
    private var changeInfoType : ChangeInfoType = ChangeInfoType.NICKNAME

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewbinding = FragmentSettingsChangeInfoDetailBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        viewbinding.fragmentContent.setOnClickListener { hideKeyboard(it) }
        when (args.changeInfoType){
            ChangeInfoType.NICKNAME -> changeInfoType = ChangeInfoType.NICKNAME
            ChangeInfoType.ID -> changeInfoType = ChangeInfoType.ID
            ChangeInfoType.PWD -> changeInfoType = ChangeInfoType.PWD
            else -> makeErrorEvent()
        }
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewTypeSetting(changeInfoType)
        viewmodel.run {
            invalidUserIdEventLiveData.observe(viewLifecycleOwner) { setIdErrorMessage(it) }
            validUserIdEventLiveData.observe(viewLifecycleOwner){ setIdEmptyMessage() }
            invalidUserPwdEventLiveData.observe(viewLifecycleOwner) { setPwdErrorMessage(it) }
            validUserPwdEventLiveData.observe(viewLifecycleOwner){ setPwdEmptyMessage() }
            invalidUserPwd2EventLiveData.observe(viewLifecycleOwner) { setPwd2ErrorMessage(it) }
            validUserPwd2EventLiveData.observe(viewLifecycleOwner){ setPwd2EmptyMessage() }
            invalidUserNicknameEventLiveData.observe(viewLifecycleOwner) { setNicknameErrorMessage(it) }
            validUserNicknameEventLiveData.observe(viewLifecycleOwner){ setNicknameEmptyMessage() }
            checkNicknameEventLiveData.observe(viewLifecycleOwner){ result ->
                when(result){
                    true -> setNicknameChecked()
                    false -> setNicknameNotChecked() }
            }
            checkIdEventLiveData.observe(viewLifecycleOwner){ result ->
                when(result){
                    true -> setIdChecked()
                    false -> setIdNotChecked() }
            }
            sendChangeInfoEventLiveData.observe(viewLifecycleOwner){
                viewmodel.sendChangeInfoToServer(getChangeInfoType(), getUserId(), getUserPwd(), getUserNickname())
            }
            onSuccessChangeInfoEvent.observe(viewLifecycleOwner){ findNavController().navigate(R.id.action_settingsChangeInfoDetailFragment_pop) }

        }
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.run {
            editTextId.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus && (getUserId().isEmpty()|| getUserId().isBlank())) {
                    setIdErrorMessage("4~13자리로 입력해주세요.")
                    setPwdEmptyMessage()
                    setPwd2EmptyMessage()
                    setNicknameEmptyMessage()
                }
            }
            editTextPwd.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus && (getUserPwd().isEmpty()||getUserPwd().isBlank())) {
                    setPwdErrorMessage("8~22자리의 영문/숫자로 입력해주세요.")
                    setIdEmptyMessage()
                    setPwd2EmptyMessage()
                    setNicknameEmptyMessage()
                }
            }
            editTextPwd2.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus && (getUserPwd().isEmpty()|| getUserPwd().isBlank())) {
                    setPwd2ErrorMessage("비밀번호를 먼저 입력해주세요.")
                    setIdEmptyMessage()
                    setPwdEmptyMessage()
                    setNicknameEmptyMessage()
                }else if (hasFocus && getUserPwd().isNotEmpty() && getUserPwd().isNotBlank()) setPwdEmptyMessage()
            }
            editTextNickname.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus && (getUserNickname().isEmpty()|| getUserNickname().isBlank())) {
                    setNicknameErrorMessage("5~10자리의 한글로 입력해주세요.")
                    setIdEmptyMessage()
                    setPwdEmptyMessage()
                    setPwd2EmptyMessage()
                }
            }
            editTextId.addTextChangedListener {
                viewmodel.clearCheckedId()
                checkIdBtn.isSelected = false
                if (it.toString().isEmpty() || it.toString().isBlank()) setIdErrorMessage("4~13자리로 입력해주세요.")
                else setIdEmptyMessage()
            }
            editTextNickname.addTextChangedListener {
                viewmodel.clearCheckedNickname()
                checkNicknameBtn.isSelected = false
                if (it.toString().isEmpty() || it.toString().isBlank()) setIdErrorMessage("5~10자리의 한글로 입력해주세요.")
                else setNicknameEmptyMessage()
            }

            showingPwdBtn.setOnClickListener {
                showingPwdBtn.isSelected = !showingPwdBtn.isSelected
                if (showingPwdBtn.isSelected) {
                    editTextPwd.inputType = 0x00000001
                    editTextPwd.typeface = resources.getFont(R.font.notosan_font_family) }
                else editTextPwd.inputType = 0x00000081
            }
            showingPwd2Btn.setOnClickListener {
                showingPwd2Btn.isSelected = !showingPwd2Btn.isSelected
                if (showingPwd2Btn.isSelected) {
                    editTextPwd2.inputType = 0x00000001
                    editTextPwd.typeface = resources.getFont(R.font.notosan_font_family) }
                else editTextPwd2.inputType = 0x00000081
            }

            checkIdBtn.setOnClickListener { if(viewmodel.checkForUserId(getUserId())) viewmodel.checkIdFromServer(getUserId()) }
            checkNicknameBtn.setOnClickListener { if(viewmodel.checkForUserNickname(getUserNickname())) viewmodel.checkNicknameFromServer(getUserNickname()) }
            changeInfoBtn.setOnClickListener { viewmodel.checkForSendChangeInfo(getChangeInfoType(),getUserId(), getUserPwd(), getUserPwd2(), getUserNickname()) }
        }
    }



    private fun makeErrorEvent(){
        showToast("알수 없는 에러가 발생했습니다. 다시 시도해주세요.")
        findNavController().navigate(R.id.action_settingsChangeInfoDetailFragment_pop)
    }

    private fun viewTypeSetting(type : ChangeInfoType){
        viewbinding.run {
            when (type){
                ChangeInfoType.NICKNAME -> {
                    title.text = "닉네임 변경"
                    nicknameLayout.visibility = View.VISIBLE
                }
                ChangeInfoType.ID -> {
                    title.text = "아이디 변경"
                    idLayout.visibility = View.VISIBLE
                }
                ChangeInfoType.PWD -> {
                    title.text = "비밀번호 변경"
                    pwdLayout.visibility = View.VISIBLE
                }
            }
        }
    }
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

    private fun setPwdErrorMessage(message: String) {
        viewbinding.editTextPwd.setBackgroundResource(R.drawable.edittext_underline_pinkishorange)
        viewbinding.textPwdWarning.apply {
            text = message
            visibility = View.VISIBLE
        }
    }
    private fun setPwdEmptyMessage() {
        viewbinding.editTextPwd.setBackgroundResource(R.drawable.edittext_underline_black20)
        viewbinding.textPwdWarning.text = "" }

    private fun setPwd2ErrorMessage(message: String) {
        viewbinding.editTextPwd2.setBackgroundResource(R.drawable.edittext_underline_pinkishorange)
        viewbinding.textPwd2Warning.apply {
            text = message
            visibility = View.VISIBLE
        }
    }
    private fun setPwd2EmptyMessage() {
        viewbinding.editTextPwd2.setBackgroundResource(R.drawable.edittext_underline_black20)
        viewbinding.textPwd2Warning.text = "" }

    private fun setNicknameErrorMessage(message: String) {
        viewbinding.editTextNickname.setBackgroundResource(R.drawable.edittext_underline_pinkishorange)
        viewbinding.textNicknameWarning.apply {
            text = message
            visibility = View.VISIBLE
        }
    }
    private fun setNicknameEmptyMessage() {
        viewbinding.editTextNickname.setBackgroundResource(R.drawable.edittext_underline_black20)
        viewbinding.textNicknameWarning.text = "" }

    private fun setIdChecked() {
        viewbinding.apply {
            textIdWarning.apply {
                text = "사용할 수 있는 아이디입니다."
                setTextColor(ContextCompat.getColor(requireContext(), R.color.applemint))
                visibility = View.VISIBLE
            }
            checkIdBtn.isSelected = true
        }
    }

    private fun setIdNotChecked() {
        viewbinding.apply {
            textIdWarning.apply {
                text = "이미 존재하는 아이디입니다."
                setTextColor(ContextCompat.getColor(requireContext(), R.color.pinkish_orange))
            }
            checkNicknameBtn.isSelected = false
        }
    }

    private fun setNicknameChecked() {
        viewbinding.apply {
            textNicknameWarning.apply {
                text = "사용할 수 있는 닉네임입니다."
                setTextColor(ContextCompat.getColor(requireContext(), R.color.applemint))
                visibility = View.VISIBLE
            }
            checkNicknameBtn.isSelected = true
        }
    }

    private fun setNicknameNotChecked() {
        viewbinding.apply {
            textNicknameWarning.apply {
                text = "이미 존재하는 닉네임입니다."
                setTextColor(ContextCompat.getColor(requireContext(), R.color.pinkish_orange))
            }
            checkNicknameBtn.isSelected = false
        }
    }

    private fun getChangeInfoType() = changeInfoType
    private fun getUserId() = viewbinding.editTextId.text.toString().trim()
    private fun getUserPwd() = viewbinding.editTextPwd.text.toString().trim()
    private fun getUserPwd2() = viewbinding.editTextPwd2.text.toString().trim()
    private fun getUserNickname() = viewbinding.editTextNickname.text.toString().trim()


}