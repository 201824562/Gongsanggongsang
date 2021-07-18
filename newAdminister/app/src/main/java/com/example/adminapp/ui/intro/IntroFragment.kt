package com.example.adminapp.ui.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.adminapp.R
import com.example.adminapp.base.BaseSessionFragment
import com.example.adminapp.data.model.AdminStatus
import com.example.adminapp.databinding.FragmentIntroBinding
import com.example.adminapp.utils.MatchedDialogSignInOneButton
import com.example.adminapp.utils.hideKeyboard

//TODO : 아이디/비번 찾기 구현하기 -> Administer에 적용하기.
class IntroFragment : BaseSessionFragment<FragmentIntroBinding, IntroViewModel>(){
    override lateinit var viewbinding: FragmentIntroBinding
    override val viewmodel: IntroViewModel by viewModels()

    private var idInfoExist : Boolean = false
    private var pwdInfoExist : Boolean = false
    private var nextBtnAvailable : Boolean = false


    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentIntroBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        viewbinding.fragmentContent.setOnClickListener { hideKeyboard(it) }
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.run {
            onFailCheckingAdminData.observe(viewLifecycleOwner){ showErrorDialog(it) }
            adminStatusEvent.observe(viewLifecycleOwner) {
                when (it) {
                    AdminStatus.NOT_ADMIN -> showErrorDialog("로그인에 실패했습니다.\n아이디나 비밀번호를 확인해주세요.")
                    /*AdminStatus.WAIT_APPROVE -> showErrorDialog("가입 승인 대기중이에요.\n공간 관리자님께 문의해주세요.")*/
                    AdminStatus.ADMIN -> viewmodel.saveUserInfo()
                    else -> showErrorDialog("오류가 발생했습니다. 다시 시도하거나 문의해주세요.") }
            }
            onSuccessSaveUserInfo.observe(viewLifecycleOwner) { findNavController().navigate(R.id.action_introFragment_to_mainFragment) }
        }
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.loginBtn.setOnClickListener {
                if (viewmodel.checkForSignInInfo(getAdminId(), getAdminPwd())) viewmodel.sendSignInInfo(getAdminId(), getAdminPwd()) }

    }

    private fun getAdminId() = viewbinding.editTextId.text.toString().trim()
    private fun getAdminPwd() = viewbinding.editTextPwd.text.toString().trim()

    private fun showErrorDialog(errorMessage : String){
        val dialog = MatchedDialogSignInOneButton(requireContext(),  errorMessage).apply {
            clickListener = object : MatchedDialogSignInOneButton.DialogButtonClickListener {
                override fun dialogClickListener() { dismiss() }
            }
        }
        showDialog(dialog, viewLifecycleOwner)
    }


}