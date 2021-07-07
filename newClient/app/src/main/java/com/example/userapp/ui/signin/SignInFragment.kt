package com.example.userapp.ui.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.userapp.R
import com.example.userapp.base.BaseSessionFragment
import com.example.userapp.databinding.FragmentSigninBinding
import com.example.userapp.utils.hideKeyboard
import com.example.userapp.utils.setupKeyboardHide

class SignInFragment : BaseSessionFragment<FragmentSigninBinding, SignInViewModel>() {
    override lateinit var viewbinding: FragmentSigninBinding
    override val viewmodel: SignInViewModel by viewModels()


    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentSigninBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        viewbinding.content.setOnClickListener { hideKeyboard(it) }
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.onSuccessLoginUserData.observe(viewLifecycleOwner){ userdata ->
            viewmodel.saveUserInfo(userdata)
        }
        viewmodel.onSuccessSaveUserInfo.observe(viewLifecycleOwner){
            findNavController().navigate(R.id.action_signInFragment_to_mainFragment)
        }
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.loginBtn.setOnClickListener {
            if (viewmodel.checkForSignInInfo(getUsersId(), getUsersPwd())) {
                viewmodel.sendSignInInfo(getUsersId(), getUsersPwd())
            }
        }

    }

    private fun getUsersId() = viewbinding.editTextId.text.toString()
    private fun getUsersPwd() = viewbinding.editTextPwd.text.toString()


}