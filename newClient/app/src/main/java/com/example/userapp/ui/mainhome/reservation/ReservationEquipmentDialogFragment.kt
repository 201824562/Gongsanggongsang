package com.example.userapp.ui.mainhome.reservation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.userapp.R
import com.example.userapp.base.BaseFragment
import com.example.userapp.databinding.FragmentMainhomeReservationEquipmentDialogBinding
import com.example.userapp.databinding.FragmentMainhomeReservationFacilityBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.FirebaseFirestore

class ReservationEquipmentDialogFragment : BottomSheetDialogFragment() {
    private lateinit var viewbinding: FragmentMainhomeReservationEquipmentDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewbinding =
            FragmentMainhomeReservationEquipmentDialogBinding.inflate(inflater, container, false)
        return viewbinding.root
    }
}
