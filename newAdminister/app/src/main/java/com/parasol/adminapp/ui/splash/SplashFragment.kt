package com.parasol.adminapp.ui.splash

import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.parasol.adminapp.R
import com.parasol.adminapp.ui.base.BaseSessionFragment
import com.parasol.adminapp.databinding.FragmentSplashBinding
import com.parasol.adminapp.restartActivity
import kotlinx.coroutines.*

class SplashFragment : BaseSessionFragment<FragmentSplashBinding, SplashViewModel>(){

    override lateinit var viewbinding: FragmentSplashBinding
    override val viewmodel: SplashViewModel by viewModels()
    private lateinit var connectionManager : ConnectivityManager

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewbinding =  FragmentSplashBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        when (context){
            null -> {
                showToast("에러가 발생했습니다.\n앱을 재부팅합니다.")
                restartActivity()
            }else ->{ connectionManager = requireContext().getSystemService()!! }
        }
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

    private fun checkServiceState() : Boolean { return connectionManager.activeNetwork != null }

    private fun showIntro() {
        findNavController().navigate(R.id.action_splashFragment_to_signInGraph)
    }

    private fun showMainActivity() {
        findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
    }

    private fun notUserEvent() {
        try { } catch (e: Exception) {
            findNavController().navigate(R.id.action_global_signInFragment)
        } finally {
            showIntro()
        }
    }

    override fun onResume() {
        super.onResume()
        if (checkServiceState()){
            if (viewmodel.isTokenAvailable) {
                viewmodel.getAdminStatus()
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    showIntro()
                }, 1000L)
            }
        }else {
            if (viewmodel.isTokenAvailable) {
                viewmodel.getAdminStatus()
                showToast("인터넷 연결이 불안정합니다.\nWifi 상태를 체킹해주세요.")
            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    showToast("인터넷 연결이 불안정합니다.\nWifi 상태를 체킹해주세요.")
                    withContext(Dispatchers.IO){
                        delay(5000L)
                        Handler(Looper.getMainLooper()).postDelayed({
                            showIntro()
                        }, 1000L)
                    }
                }
            }
        }
    }

}