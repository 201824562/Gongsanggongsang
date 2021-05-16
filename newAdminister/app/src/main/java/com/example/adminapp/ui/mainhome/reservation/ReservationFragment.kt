package com.example.adminapp.ui.mainhome.reservation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.adminapp.base.BaseFragment
import com.example.adminapp.databinding.FragmentMainhomeReservationBinding

class ReservationFragment : BaseFragment<FragmentMainhomeReservationBinding, ReservationViewModel>() {
    override lateinit var viewbinding: FragmentMainhomeReservationBinding
    override val viewmodel: ReservationViewModel by viewModels()

    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentMainhomeReservationBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
    }

}