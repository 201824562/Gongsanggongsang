package com.example.adminapp.ui.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.example.adminapp.R
import com.example.adminapp.base.BaseSessionFragment
import com.example.adminapp.databinding.FragmentSigninFindResultBinding


class SignInFindInfoResultFragment : BaseSessionFragment<FragmentSigninFindResultBinding, SignInViewModel>() {

    override lateinit var viewbinding : FragmentSigninFindResultBinding
    override val viewmodel: SignInViewModel by navGraphViewModels(R.id.signInGraph)
    private var type : FindInfoType = FindInfoType.ID
    private var userName : String = ""
    private var resultInfo : String = ""


    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewbinding = FragmentSigninFindResultBinding.inflate(inflater, container, false)
        return viewbinding.root
    }
    private fun setBundleVar(){
        viewmodel.run {
            type = findInfoType
            userName = findInfoUserName
            resultInfo = findInfoResultData
        }
    }
    override fun initViewStart(savedInstanceState: Bundle?) {
        setBundleVar()
        makeTypeView(type)
    }

    override fun initDataBinding(savedInstanceState: Bundle?) { }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.run {
            findAdditionalBtn.setOnClickListener {
                setBundleInfo(type)
                findNavController().navigate(R.id.action_signInFindInfoResultFragment_to_signInFindInfoFragment) }
            signinBtn.setOnClickListener { findNavController().navigate(R.id.action_signInFindInfoResultFragment_pop) }
        }
    }

    private fun makeTypeView(type : FindInfoType){
        viewbinding.run {
            when(type){
                FindInfoType.ID -> {
                    findInfoType.text = "아이디"
                    findInfoUserName.text = userName
                    findInfoResult.text = resultInfo
                    findAdditionalBtn.text = "비밀번호 찾기"
                }
                FindInfoType.PWD -> {
                    findInfoType.text = "비밀번호"
                    findInfoUserName.text = userName
                    findInfoResult.text = resultInfo
                    findAdditionalBtn.text = "아이디 찾기"
                }
            }
        }
    }

    private fun setBundleInfo(type : FindInfoType){
        when(type){
            FindInfoType.ID -> viewmodel.findInfoType = FindInfoType.PWD
            FindInfoType.PWD -> viewmodel.findInfoType = FindInfoType.ID
        }
    }

}