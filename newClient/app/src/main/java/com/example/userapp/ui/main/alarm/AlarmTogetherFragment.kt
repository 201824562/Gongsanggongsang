package com.example.userapp.ui.main.alarm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.userapp.base.BaseSessionFragment
import com.example.userapp.databinding.FragmentAlarmChildBinding

class AlarmTogetherFragment() : BaseSessionFragment<FragmentAlarmChildBinding, AlarmViewModel>(){
    override lateinit var viewbinding: FragmentAlarmChildBinding
    override val viewmodel: AlarmViewModel by viewModels()

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewbinding = FragmentAlarmChildBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {

    }

    override fun initDataBinding(savedInstanceState: Bundle?) {

    }

    override fun initViewFinal(savedInstanceState: Bundle?) {

    }

}