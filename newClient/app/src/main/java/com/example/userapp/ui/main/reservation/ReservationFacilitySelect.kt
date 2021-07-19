package com.example.userapp.ui.main.reservation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.userapp.base.BaseFragment
import com.example.userapp.databinding.FragmentMainhomeReservationFacilitySelectBinding
import com.google.firebase.firestore.FirebaseFirestore

class ReservationFacilitySelect : BaseFragment<FragmentMainhomeReservationFacilitySelectBinding, ReservationViewModel>() {
    override lateinit var viewbinding: FragmentMainhomeReservationFacilitySelectBinding
    override val viewmodel: ReservationViewModel by viewModels()
    val database = FirebaseFirestore.getInstance()
    val args: ReservationFacilitySelectArgs by navArgs()

    override fun initViewbinding(
        inflater: LayoutInflater,
         container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentMainhomeReservationFacilitySelectBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {

    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.documentNameTextview.text = args.myArg.document_name + " 예약"
        viewbinding.message2Textview.text = "최대 예약 가능한 시간:" + args.myArg.max_time.toString()+"분"

    }
}
