package com.example.userapp.ui.main.reservation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.userapp.databinding.FragmentMainhomeReservationFacilityBinding
import androidx.fragment.app.viewModels
import com.example.userapp.base.BaseFragment
import com.google.firebase.firestore.FirebaseFirestore

class ReservationFacilityFragment :
    BaseFragment<FragmentMainhomeReservationFacilityBinding, ReservationViewModel>() {
    override lateinit var viewbinding: FragmentMainhomeReservationFacilityBinding
    override val viewmodel: ReservationViewModel by viewModels()
    val database = FirebaseFirestore.getInstance()

    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentMainhomeReservationFacilityBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
    }

}