package com.parasol.userapp.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.parasol.userapp.R
import com.parasol.userapp.ui.base.BaseFragment
import com.parasol.userapp.databinding.FragmentSignupWaitBinding

class SignUpWaitFragment : Fragment() {
    lateinit var binding : FragmentSignupWaitBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding= FragmentSignupWaitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signupWaitMainBtn.setOnClickListener { findNavController().navigate(R.id.action_signUpWaitFragment_pop) }
    }
}