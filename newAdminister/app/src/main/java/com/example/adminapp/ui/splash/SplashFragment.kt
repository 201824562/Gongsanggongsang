package com.example.adminapp.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.adminapp.R
import com.example.adminapp.base.BaseSessionFragment
import com.example.adminapp.databinding.FragmentSplashBinding

class SplashFragment : BaseSessionFragment<FragmentSplashBinding, SplashViewModel>(){

    override lateinit var viewbinding: FragmentSplashBinding
    override val viewmodel: SplashViewModel by viewModels()

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewbinding =  FragmentSplashBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.onSuccessGettingToken.observe(viewLifecycleOwner, {
            when (it) {
                true -> showMainActivity()
                false -> notUserEvent()
                else -> throw IllegalArgumentException("MemberStatus Error")
            }
        })
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {}

    private fun showIntro() {
        findNavController().navigate(R.id.action_splashFragment_to_introFragment)
    }

    private fun showMainActivity() {
        findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
    }

    private fun notUserEvent() {  // App 내 Token 은 유효하지만 서버 Token 이 유효하지 않을 때
        try { } catch (e: Exception) {
            findNavController().navigate(R.id.action_global_introFragment)
        } finally {
            showIntro()
        }
    }

    override fun onResume() {
        super.onResume()
        if (viewmodel.isTokenAvailable) {
            viewmodel.getAdminStatus()
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                showIntro()
            }, 1000L)
        }
    }

}