package com.example.userapp.ui.main.alarm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.userapp.base.BaseSessionFragment
import com.example.userapp.data.model.*
import com.example.userapp.databinding.FragmentAlarmChildBinding
import com.example.userapp.utils.CustomedAlarmDialog
import kotlin.math.sign


class AlarmAllFragment() : BaseSessionFragment<FragmentAlarmChildBinding, AlarmViewModel>(){
    override lateinit var viewbinding: FragmentAlarmChildBinding
    override val viewmodel: AlarmViewModel by viewModels()
    private lateinit var alarmRVAdapter: AlarmRVAdapter
    private var postAlarmDataBundle : PostAlarmData? = null

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewbinding = FragmentAlarmChildBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) { setRecyclerView() }

    override fun initDataBinding(savedInstanceState: Bundle?) { }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewmodel.getAlarmAllList().observe(viewLifecycleOwner){
            if (it.isEmpty()){ showEmptyView() }
            else showRV(it)
        }
    }

    private fun makeDialog(reserveData: ReservationAlarmData?, signData: SignUpAlarmData?){
        val dialog = CustomedAlarmDialog(requireContext(), reserveData, signData).apply {
            clickListener = object : CustomedAlarmDialog.DialogButtonClickListener{
                override fun dialogCloseClickListener() {
                    //TODO : 로그 -> 사용취소로 바꾸기.(시간 남게 하나?)
                }
                override fun dialogClickListener() {
                    //TODO : 로그 -> 사용중으로 바꾸기.
                }
            }
        }
        showDialog(dialog, viewLifecycleOwner)
    }


    private fun setRecyclerView() {
        alarmRVAdapter = AlarmRVAdapter(object : AlarmRVAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, alarmData: AlarmItem) {
                when(alarmData.type){
                    AlarmType.RESERVATION -> {
                        if (alarmData.reservationData != null) makeDialog(alarmData.reservationData, null)
                    }
                    else -> {
                        postAlarmDataBundle = alarmData.postData
                        //글쓰기 세부페이지로 이동하기  //TODO!!
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