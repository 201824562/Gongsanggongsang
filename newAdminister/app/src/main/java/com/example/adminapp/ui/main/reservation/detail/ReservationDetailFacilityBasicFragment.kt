package com.example.adminapp.ui.main.reservation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.adminapp.base.BaseSessionFragment
import com.example.adminapp.databinding.FragmentReservationDetailFacilityBasicBinding

class ReservationDetailFacilityBasicFragment : BaseSessionFragment<FragmentReservationDetailFacilityBasicBinding, ReservationDetailFacilityViewModel>() {
    override lateinit var viewbinding: FragmentReservationDetailFacilityBasicBinding
    override val viewmodel: ReservationDetailFacilityViewModel by viewModels()

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewbinding = FragmentReservationDetailFacilityBasicBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
    }

}