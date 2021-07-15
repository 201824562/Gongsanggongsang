package com.example.userapp.ui.signup

import android.R.attr.typeface
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.TypefaceSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.userapp.R
import com.example.userapp.base.BaseFragment
import com.example.userapp.databinding.FragmentSignupSecondBinding
import com.example.userapp.utils.hideKeyboard


class SignUpSecondFragment : BaseFragment<FragmentSignupSecondBinding, SignUpViewModel>() {

    override lateinit var viewbinding: FragmentSignupSecondBinding
    override val viewmodel: SignUpViewModel by navGraphViewModels(R.id.signUpGraph)

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewbinding = FragmentSignupSecondBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        viewbinding.fragmentContent.setOnClickListener { hideKeyboard(it) }
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            viewmodel.clearSecondFragmentVar()
            findNavController().navigate(R.id.action_signUpSecondFragment_pop)
        }
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.run {
            invalidUserIdEventLiveData.observe(viewLifecycleOwner) { setIdErrorMessage(it) }
            validUserIdEventLiveData.observe(viewLifecycleOwner){ setIdEmptyMessage() }
            invalidUserPwdEventLiveData.observe(viewLifecycleOwner) { setPwdErrorMessage(it) }
            validUserPwdEventLiveData.observe(viewLifecycleOwner){ setPwdEmptyMessage() }
            invalidUserPwd2EventLiveData.observe(viewLifecycleOwner) { setPwd2ErrorMessage(it) }
            validUserPwd2EventLiveData.observe(viewLifecycleOwner){ setPwd2EmptyMessage() }
            invalidUserNicknameEventLiveData.observe(viewLifecycleOwner) { setNicknameErrorMessage(it) }
            validUserNicknameEventLiveData.observe(viewLifecycleOwner){ setNicknameEmptyMessage() }
            checkIdEventLiveData.observe(viewLifecycleOwner){ result ->
                when(result){
                    true -> setIdChecked()
                    false -> setIdNotChecked() }
            }
            checkNicknameEventLiveData.observe(viewLifecycleOwner){ result ->
                when(result){
                    true -> setNicknameChecked()
                    false -> setNicknameNotChecked() }
            }
            sendSignUpInfoEventLiveData.observe(viewLifecycleOwner){
                viewmodel.sendSignUpInfoToServer(getUsersId(), getUsersPwd(), getUsersNickname())
            }
            onSuccessSignUpEvent.observe(viewLifecycleOwner){ findNavController().navigate(R.id.action_signUpSecondFragment_to_signUpWaitFragment) }
        }
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {

        viewbinding.run {
            editTextId.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus && (getUsersId().isEmpty()|| getUsersId().isBlank())) {
                    setIdErrorMessage("4~13자리로 입력해주세요.")
                    setPwdEmptyMessage()
                    setPwd2EmptyMessage()
                    setNicknameEmptyMessage()
                }
            }
            editTextPwd.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus && (getUsersPwd().isEmpty()||getUsersPwd().isBlank())) {
                    setPwdErrorMessage("8~22자리의 영문/숫자로 입력해주세요.")
                    setIdEmptyMessage()
                    setPwd2EmptyMessage()
                    setNicknameEmptyMessage()
                }
            }
            editTextPwd2.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus && (getUsersPwd().isEmpty()|| getUsersPwd().isBlank())) {
                    setPwd2ErrorMessage("비밀번호를 먼저 입력해주세요.")
                    setIdEmptyMessage()
                    setPwdEmptyMessage()
                    setNicknameEmptyMessage()
                }else if (hasFocus && getUsersPwd().isNotEmpty() && getUsersPwd().isNotBlank()) setPwdEmptyMessage()
            }
            editTextNickname.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus && (getUsersNickname().isEmpty()|| getUsersNickname().isBlank())) {
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

            checkIdBtn.setOnClickListener { if(viewmodel.checkForUserId(getUsersId())) viewmodel.checkIdFromServer(getUsersId()) }
            checkNicknameBtn.setOnClickListener { if(viewmodel.checkForUserNickname(getUsersNickname())) viewmodel.checkNicknameFromServer(getUsersNickname()) }
            signupBtn.setOnClickListener { viewmodel.checkForSendSignUpInfo(getUsersId(), getUsersPwd(), getUsersPwd2(), getUsersNickname()) }
        }
    }

    private fun getUsersId() = viewbinding.editTextId.text.toString().trim()
    private fun getUsersPwd() = viewbinding.editTextPwd.text.toString().trim()
    private fun getUsersPwd2() = viewbinding.editTextPwd2.text.toString().trim()
    private fun getUsersNickname() = viewbinding.editTextNickname.text.toString().trim()


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


}