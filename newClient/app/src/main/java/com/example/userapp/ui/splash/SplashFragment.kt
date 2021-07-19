package com.example.userapp.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.userapp.R
import com.example.userapp.base.BaseSessionFragment
import com.example.userapp.data.model.UserStatus
import com.example.userapp.databinding.FragmentSplashBinding

class SplashFragment : BaseSessionFragment<FragmentSplashBinding, SplashViewModel> (){

    override lateinit var viewbinding: FragmentSplashBinding
    override val viewmodel: SplashViewModel by viewModels()

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewbinding =  FragmentSplashBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.userStatusEvent.observe(viewLifecycleOwner, { status ->
            when (status) {
                (UserStatus.USER) -> showMainActivity()
                (UserStatus.NOT_USER) -> notUserEvent()
                // (UserStatus.WAIT_APPROVE) -> showWaitForApproveActivity() //TODO : 승인 대기 상태 추가하기.
                else -> throw IllegalArgumentException("MemberStatus Error")
            }
        })
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {}

    private fun showIntro() {
        findNavController().navigate(R.id.action_splashFragment_to_introFragment)
    }

    //TODO : 승인 대기 상태 추가하기.
   /* private fun showWaitForApproveActivity() {
        findNavController().navigate(R.id.action_splashFragment_to_waitForApproveFragment)
    }*/

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
            viewmodel.getUserStatus()
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                showIntro()
            }, 1000L)
        }
    }

}