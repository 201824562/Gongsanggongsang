package com.example.userapp.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.userapp.R
import com.example.userapp.base.BaseFragment
import com.example.userapp.base.BaseSessionFragment
import com.example.userapp.databinding.FragmentSignupSecondBinding


class SignUpSecondFragment : BaseSessionFragment<FragmentSignupSecondBinding, SignUpViewModel>() {
    override lateinit var viewbinding: FragmentSignupSecondBinding
    override val viewmodel: SignUpViewModel by navGraphViewModels(R.id.signUpGraph)

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewbinding = FragmentSignupSecondBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        setupKeyboardHide(viewbinding.fragmentRootLayout, activity)
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        //네트워크 연결 설정 처리 필요 & 아이디*닉네임 중복여부(서버 뒤져서 확인 )-> 이미 있는 유저 경우 덮어쓰기.
        //닉네임 중복체크 버튼이 필요하다.
        viewbinding.signupBtn.setOnClickListener {
            if (viewmodel.checkForSendSignUpInfo(getUsersId(), getUsersPwd(), getUsersPwd2(), getUsersNickname())) {
                viewmodel.sendSignUpInfo(getUsersId(), getUsersPwd(), getUsersNickname()).observe(viewLifecycleOwner){ result ->
                    if (result) findNavController().navigate(R.id.action_signUpSecondFragment_to_signUpWaitFragment)
                    else showSnackbar("회원가입 등록에 실패하였습니다.")
                }
            }
        }
    }

    private fun getUsersId() = viewbinding.editTextId.text.toString()
    private fun getUsersPwd() = viewbinding.editTextPwd.text.toString()
    private fun getUsersPwd2() = viewbinding.editTextPwd2.text.toString()
    private fun getUsersNickname() = viewbinding.editTextNickname.text.toString()


}