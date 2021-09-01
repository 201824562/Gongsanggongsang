package com.example.userapp.ui.main.alarm

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.userapp.R
import com.example.userapp.base.BaseSessionFragment
import com.example.userapp.data.model.*
import com.example.userapp.databinding.FragmentAlarmChildBinding
import com.example.userapp.utils.CustomedAlarmDialog

class AlarmCommunityFragment() : BaseSessionFragment<FragmentAlarmChildBinding, AlarmViewModel>(){
    override lateinit var viewbinding: FragmentAlarmChildBinding
    override val viewmodel: AlarmViewModel by viewModels()
    private lateinit var alarmRVAdapter: AlarmRVAdapter
    private var postDataBundle : PostDataInfo? = null

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewbinding = FragmentAlarmChildBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) { setRecyclerView() }

    override fun initDataBinding(savedInstanceState: Bundle?) { }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewmodel.getAlarmCommunityList().observe(viewLifecycleOwner){
            if (it.isEmpty()){ showEmptyView() }
            else showRV(it)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun makeDialog(reserveData: ReservationAlarmData?, signData: SignUpAlarmData?){
        val dialog = CustomedAlarmDialog(requireContext(), reserveData, signData).apply {
            clickListener = object : CustomedAlarmDialog.DialogButtonClickListener{
                override fun dialogCloseClickListener() {
                    reserveData?.let { viewmodel.makeReservationLogCancel(it.documentId) }
                    dismiss()
                }
                override fun dialogClickListener() {
                    reserveData?.let { viewmodel.makeReservationLogUsing(it.documentId) }
                    dismiss()
                }
            }
        }
        showDialog(dialog, viewLifecycleOwner)
    }


    private fun setRecyclerView() {
        alarmRVAdapter = AlarmRVAdapter(object : AlarmRVAdapter.OnItemClickListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onItemClick(position: Int, alarmData: AlarmItem) {
                when(alarmData.type){
                    AlarmType.makeEnumDataToString(AlarmType.RESERVATION) -> {
                        if (alarmData.reservationData != null) makeDialog(alarmData.reservationData, null)
                    }
                    AlarmType.makeEnumDataToString(AlarmType.MARKET) -> {
                        postDataBundle = alarmData.postData!!.makeToPostDataInfo()
                        val bundle = bundleOf("post_data_info" to postDataBundle)
                        findNavController().navigate(R.id.action_mainFragment_to_communityPostMarket, bundle)
                    }
                    else -> {
                        postDataBundle = alarmData.postData!!.makeToPostDataInfo()
                        val bundle = bundleOf("post_data_info" to postDataBundle)
                        findNavController().navigate(R.id.action_mainFragment_to_communityPost, bundle)
                    }
                }
            }
        })
        viewbinding.alarmRv.adapter = alarmRVAdapter
    }

    private fun showEmptyView(){
        viewbinding.apply {
            alarmEmptyView.visibility = View.VISIBLE
            alarmRv.visibility = View.GONE
        }
    }
    private fun showRV(list : List<AlarmItem>){
        viewbinding.run{
            alarmEmptyView.visibility  = View.GONE
            alarmRv.visibility = View.VISIBLE
            alarmRVAdapter.submitList(list)
        }
    }

}