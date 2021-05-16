package com.example.userapp.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.userapp.base.BaseFragment
import com.example.userapp.databinding.FragmentSignupBinding

class SignUpFragment : BaseFragment<FragmentSignupBinding, SignUpViewModel>(){
    override lateinit var viewbinding: FragmentSignupBinding
    override val viewmodel: SignUpViewModel by viewModels()

    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentSignupBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
    }

}