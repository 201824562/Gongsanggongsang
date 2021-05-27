package com.example.userapp.ui.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.userapp.R
import com.example.userapp.base.BaseFragment
import com.example.userapp.databinding.FragmentSigninBinding

class SignInFragment : BaseFragment<FragmentSigninBinding, SignInViewModel>() {
    override lateinit var viewbinding: FragmentSigninBinding
    override val viewmodel: SignInViewModel by viewModels()


    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentSigninBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        setupKeyboardHide(viewbinding.fragmentRootLayout, activity)
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {

    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.loginBtn.setOnClickListener {

            if (viewmodel.checkForSignInInfo(getUsersId(), getUsersPwd())) {
                viewmodel.sendSignInInfo(getUsersId(), getUsersPwd()).observe(viewLifecycleOwner){ result ->
                    if (result) findNavController().navigate(R.id.action_signInFragment_to_mainFragment)
                    else showSnackbar("입력하신 정보와 일치하는 아이디/비밀번호가 없습니다.")
               }
            }
        }
    }

    private fun getUsersId() = viewbinding.editTextId.text.toString()
    private fun getUsersPwd() = viewbinding.editTextPwd.text.toString()


}