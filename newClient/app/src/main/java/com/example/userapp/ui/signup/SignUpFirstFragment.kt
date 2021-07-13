package com.example.userapp.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.userapp.R
import com.example.userapp.base.BaseFragment
import com.example.userapp.databinding.FragmentSignupFirstBinding
import com.example.userapp.utils.hideKeyboard

//TODO : 1.2 frag -> button state
class SignUpFirstFragment : BaseFragment<FragmentSignupFirstBinding, SignUpViewModel>() {
    override lateinit var viewbinding: FragmentSignupFirstBinding
    override val viewmodel: SignUpViewModel by navGraphViewModels(R.id.signUpGraph)

    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentSignupFirstBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        viewmodel.cameFromPermission = false
        viewbinding.fragmentContent.setOnClickListener { hideKeyboard(it) }
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.run {
            invalidUserNameEventLiveData.observe(viewLifecycleOwner) { setNameErrorMessage(it) }
            validUserNameEventLiveData.observe(viewLifecycleOwner){ setNameEmptyMessage() }
            invalidBirthInfoEventLiveData.observe(viewLifecycleOwner) { setBirthInfoErrorMessage(it) }
            validBirthInfoEventLiveData.observe(viewLifecycleOwner) { setBirthInfoEmptyMessage() }
            invalidSmsInfoEventLiveData.observe(viewLifecycleOwner) { setSmsInfoErrorMessage(it) }
            validSmsInfoEventLiveData.observe(viewLifecycleOwner) { setSmsInfoEmptyMessage() }
            saveSignUpInfoEventLiveData.observe(viewLifecycleOwner){
                viewmodel.saveSignUpInfo(getUsersName(), getUsersBirthday(), getUsersSmsInfo())
                findNavController().navigate(R.id.action_signUpFirstFragment_to_signUpSecondFragment)
            }
        }
    }


    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.run { 
            editTextName.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus && (getUsersName().isEmpty()|| getUsersName().isBlank())) {
                    setNameErrorMessage("이름을 입력해주세요.")
                    setBirthInfoEmptyMessage()
                    setSmsInfoEmptyMessage()
                }
            }
            editTextBirthInfo.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus && (getUsersBirthday().isEmpty()|| getUsersBirthday().isBlank())) {
                    setBirthInfoErrorMessage("8자리 숫자를 입력해주세요.(ex.20000101)")
                    setNameEmptyMessage()
                    setSmsInfoEmptyMessage()
                }
            }
            editTextSmsInfo.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus && (getUsersSmsInfo().isEmpty()|| getUsersSmsInfo().isBlank())) {
                    setSmsInfoErrorMessage("'-'없이 휴대폰번호 11자리를 입력해주세요.")
                    setNameEmptyMessage()
                    setBirthInfoEmptyMessage()
                }
            }
            signupNextbtn.setOnClickListener { viewmodel.checkForSaveSignUpInfo(getUsersName(), getUsersBirthday(), getUsersSmsInfo()) }
        }
    }

    private fun getUsersName() = viewbinding.editTextName.text.toString()
    private fun getUsersBirthday() = viewbinding.editTextBirthInfo.text.toString().trim()
    private fun getUsersSmsInfo() = viewbinding.editTextSmsInfo.text.toString().trim()


    private fun setNameErrorMessage(message: String) {
        viewbinding.editTextName.setBackgroundResource(R.drawable.edittext_underline_pinkishorange)
        viewbinding.textNameWarning.apply {
            text = message
            visibility = View.VISIBLE
        }
    }
    private fun setBirthInfoErrorMessage(message: String) {
        viewbinding.editTextBirthInfo.setBackgroundResource(R.drawable.edittext_underline_pinkishorange)
        viewbinding.textBirthInfoWarning.apply {
            text = message
            visibility = View.VISIBLE
        }
    }
    private fun setSmsInfoErrorMessage(message: String) {
        viewbinding.editTextSmsInfo.setBackgroundResource(R.drawable.edittext_underline_pinkishorange)
        viewbinding.textSmsInfoWarning.apply {
            text = message
            visibility = View.VISIBLE
        }
    }
    private fun setNameEmptyMessage() {
        viewbinding.editTextName.setBackgroundResource(R.drawable.edittext_underline_black20)
        viewbinding.textNameWarning.text = "" }
    private fun setBirthInfoEmptyMessage() {
        viewbinding.editTextBirthInfo.setBackgroundResource(R.drawable.edittext_underline_black20)
        viewbinding.textBirthInfoWarning.text = "" }
    private fun setSmsInfoEmptyMessage() {
        viewbinding.editTextSmsInfo.setBackgroundResource(R.drawable.edittext_underline_black20)
        viewbinding.textSmsInfoWarning.text = "" }



}