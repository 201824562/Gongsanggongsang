package com.example.userapp.ui.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.userapp.R
import com.example.userapp.base.BaseSessionFragment
import com.example.userapp.databinding.FragmentSigninBinding
import com.example.userapp.utils.hideKeyboard

class SignInFragment : BaseSessionFragment<FragmentSigninBinding, SignInViewModel>() {
    override lateinit var viewbinding: FragmentSigninBinding
    override val viewmodel: SignInViewModel by viewModels()

    private var idInfoExist : Boolean = false
    private var pwdInfoExist : Boolean = false

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
        viewbinding.run {
            loginBtn.setOnClickListener {
                if (viewmodel.checkForSignInInfo(getUsersId(), getUsersPwd())) {
                    viewmodel.sendSignInInfo(getUsersId(), getUsersPwd())
                }
            }
            editTextId.addTextChangedListener {
                idInfoExist = it.toString().isEmpty() || it.toString().isBlank()
                if (checkInfoExistState()) makeNextButtonAvailable()
                else makeNextButtonNotAvailable()
            }
            editTextPwd.addTextChangedListener {
                pwdInfoExist = it.toString().isEmpty() || it.toString().isBlank()
                if (checkInfoExistState()) makeNextButtonAvailable()
                else makeNextButtonNotAvailable()
            }
        }
    }

    private fun getUsersId() = viewbinding.editTextId.text.toString()
    private fun getUsersPwd() = viewbinding.editTextPwd.text.toString()
    private fun checkInfoExistState() : Boolean { return idInfoExist && pwdInfoExist }
    private fun makeNextButtonAvailable() { viewbinding.loginBtn.setBackgroundResource(R.drawable.button_5dp_rectangle_bluegreen) }
    private fun makeNextButtonNotAvailable() { viewbinding.loginBtn.setBackgroundResource(R.drawable.button_5dp_rectangle_black20) }


}