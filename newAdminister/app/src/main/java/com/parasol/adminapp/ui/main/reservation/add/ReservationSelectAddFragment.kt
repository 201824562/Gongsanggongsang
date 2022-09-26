package com.parasol.adminapp.ui.main.reservation.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.parasol.adminapp.R
import com.parasol.adminapp.data.model.ReservationType
import com.parasol.adminapp.databinding.FragmentReservationSelectAddBinding

class ReservationSelectAddFragment : Fragment() {

    lateinit var viewbinding : FragmentReservationSelectAddBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        viewbinding = FragmentReservationSelectAddBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewbinding.run {
            backBtn.setOnClickListener { findNavController().navigate(R.id.action_reservationSelectAddFragment_pop) }
            itemAddEquipment.setOnClickListener {
                findNavController().navigate(ReservationSelectAddFragmentDirections.actionReservationSelectAddFragmentToReservationAddFragment(ReservationType.EQUIPMENT)) }
            itemAddReservation.setOnClickListener {
                findNavController().navigate(ReservationSelectAddFragmentDirections.actionReservationSelectAddFragmentToReservationAddFragment(ReservationType.FACILITY)) }
        }
    }
}