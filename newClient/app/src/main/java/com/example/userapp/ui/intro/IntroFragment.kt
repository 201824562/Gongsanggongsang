package com.example.userapp.ui.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.userapp.R
import com.example.userapp.base.BaseFragment
import com.example.userapp.databinding.FragmentIntroBinding

class IntroFragment : BaseFragment<FragmentIntroBinding, IntroViewModel>(){
    override lateinit var viewbinding: FragmentIntroBinding
    override val viewmodel: IntroViewModel by viewModels()

    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentIntroBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) { }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.onBackPressedEventLiveData.observe(viewLifecycleOwner){ requireActivity().finish() }
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.run {
            signupBtn.setOnClickListener {
                findNavController().navigate(R.id.action_introFragment_to_signUpFragment) }

            loginBtn.setOnClickListener {
                findNavController().navigate(R.id.action_introFragment_to_signInFragment) }
        }
    }

}