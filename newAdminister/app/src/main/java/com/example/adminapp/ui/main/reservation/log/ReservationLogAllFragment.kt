package com.example.adminapp.ui.main.reservation.log

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.adminapp.base.BaseSessionFragment
import com.example.adminapp.data.model.ReservationLogItem
import com.example.adminapp.databinding.FragmentReservationChildLogChildBinding
import com.example.adminapp.ui.main.reservation.log.ReservationLogFragment.Companion.TAB_INDEX_ALL
import com.example.adminapp.ui.main.reservation.ReservationViewModel

class ReservationLogAllFragment : BaseSessionFragment<FragmentReservationChildLogChildBinding, ReservationViewModel>(){
    override lateinit var viewbinding: FragmentReservationChildLogChildBinding
    override val viewmodel: ReservationViewModel by viewModels()
    private lateinit var reservationLogRVAdapter: ReservationLogRVAdapter

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewbinding = FragmentReservationChildLogChildBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) { setRecyclerView() }

    override fun initDataBinding(savedInstanceState: Bundle?) { }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewmodel.getReservationLogDataList(TAB_INDEX_ALL).observe(viewLifecycleOwner){
            if (it.isEmpty()){ showEmptyView() }
            else showRV(it)
        }
    }

    private fun setRecyclerView() {
        reservationLogRVAdapter = ReservationLogRVAdapter(viewmodel)
        viewbinding.reservationLogRv.adapter =  reservationLogRVAdapter
    }

    private fun showEmptyView(){
        viewbinding.apply {
            reservationChildEmptyView.visibility = View.VISIBLE
            reservationLogRv.visibility = View.GONE
        }
    }
    private fun showRV(list : List<ReservationLogItem>){
        viewbinding.run{
            reservationChildEmptyView.visibility  = View.GONE
            reservationLogRv.visibility = View.VISIBLE
            reservationLogRVAdapter.submitList(list)
        }
    }
}