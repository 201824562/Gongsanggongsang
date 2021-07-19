package com.example.adminapp.ui.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.adminapp.R
import com.example.adminapp.base.BaseFragment
import com.example.adminapp.databinding.FragmentIntroBinding


class IntroFragment : BaseFragment<FragmentIntroBinding, IntroViewModel>(){
    override lateinit var viewbinding: FragmentIntroBinding
    override val viewmodel: IntroViewModel by viewModels()

    private lateinit var userId : String
    private lateinit var userPwd : String


    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentIntroBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        setupKeyboardHide(viewbinding.fragmentRootLayout, activity)
    }

    override fun initDataBinding(savedInstanceState: Bundle?) { }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.run {
            loginBtn.setOnClickListener {
                userId = editTextId.text.toString()
                userPwd = editTextPwd.text.toString()

                if (userId.isBlank() || userPwd.isBlank()){ showToast("정보를 모두 입력해주세요.") }
                else findNavController().navigate(R.id.action_introFragment_to_mainFragment) }
        }
    }

}