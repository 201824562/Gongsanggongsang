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
    private lateinit var userId : String
    private lateinit var userPwd : String

    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentSigninBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {

    }

    override fun initDataBinding(savedInstanceState: Bundle?) {

    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.run {
            signupBtn.setOnClickListener {
                findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
            }

            loginBtn.setOnClickListener {
                userId = editTextId.text.toString()
                userPwd = editTextPwd.text.toString()

                if (userId.isBlank() || userPwd.isBlank()){ showToast("정보를 모두 입력해주세요.") }
                else findNavController().navigate(R.id.action_signInFragment_to_mainFragment)
            }
        }
    }

}