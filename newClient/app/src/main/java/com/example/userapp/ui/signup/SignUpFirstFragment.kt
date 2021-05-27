package com.example.userapp.ui.signup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.userapp.R
import com.example.userapp.base.BaseFragment
import com.example.userapp.databinding.FragmentSignupFirstBinding

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
        setupKeyboardHide(viewbinding.fragmentRootLayout, activity)
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        //네트워크 연결 설정 처리 필요 & 아이디*닉네임 중복여부(서버 뒤져서 확인 )-> 이미 있는 유저 경우 덮어쓰기.
        //아이디 중복체크 버튼이 필요하다.
        viewbinding.signupNextbtn.setOnClickListener {
            if (viewmodel.checkForSaveSignUpInfo(getUsersName(), getUsersBirthday(), getUsersSmsinfo())) {
                viewmodel.saveSignUpInfo(getUsersName(), getUsersBirthday(), getUsersSmsinfo())
                findNavController().navigate(R.id.action_signUpFirstFragment_to_signUpSecondFragment)
            }
        }
    }

    private fun getUsersName() = viewbinding.editTextName.text.toString()
    private fun getUsersBirthday() = viewbinding.editTextBirthinfo.text.toString()
    private fun getUsersSmsinfo() = viewbinding.editTextSmsinfo.text.toString()


}