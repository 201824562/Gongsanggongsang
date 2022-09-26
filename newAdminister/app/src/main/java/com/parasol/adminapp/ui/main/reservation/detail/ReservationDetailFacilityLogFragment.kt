package com.parasol.adminapp.ui.main.reservation.detail

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.parasol.adminapp.R
import com.parasol.adminapp.ui.base.BaseSessionFragment
import com.parasol.adminapp.data.model.*
import com.parasol.adminapp.databinding.FragmentReservationDetailFacilityLogBinding
import com.parasol.adminapp.databinding.ItemReservationDetail2Binding
import com.parasol.adminapp.ui.main.reservation.*
import com.parasol.adminapp.utils.WrapedDialogBasicTwoButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReservationDetailFacilityLogFragment : BaseSessionFragment<FragmentReservationDetailFacilityLogBinding, ReservationDetailFacilityViewModel>() {
    override lateinit var viewbinding: FragmentReservationDetailFacilityLogBinding
    override val viewmodel: ReservationDetailFacilityViewModel by viewModels()
    private lateinit var reservationDetailFacilityLogRVAdapter : ReservationDetailFacilityLogRVAdapter
    private var observer : Observer<List<ReservationFacilityLog>>?= null
    private lateinit var facilityBundleData : ReservationFacilityBundle

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewbinding = FragmentReservationDetailFacilityLogBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        setRecyclerView()
        when {
            arguments == null -> makeErrorEvent()
            requireArguments().getParcelable<ReservationFacilityBundle>("facilityItemInfo" )== null -> makeErrorEvent()
            else -> facilityBundleData = requireArguments().getParcelable("facilityItemInfo")!!
        }
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.run {
            observer = Observer {
                Log.e("checking", "$it")
                if (it.isEmpty()) showEmptyView() else showRV(it) }
            observer?.let {observer -> getReservationFacilityNotDoneLogDataList("예약 사용", facilityBundleData.name).observeForever(observer) }
        }
    }

    override fun initViewFinal(savedInstanceState: Bundle?) { }

    private fun makeErrorEvent(){
        showToast("알수 없는 에러가 발생했습니다. 다시 시도해주세요.")
        findNavController().navigate(R.id.action_reservationDetailFacilityFragment_pop)
    }

    private fun setRecyclerView() {
        reservationDetailFacilityLogRVAdapter = ReservationDetailFacilityLogRVAdapter(requireContext(),
            object : ReservationDetailFacilityLogRVAdapter.OnItemClickListener{
            override fun onItemClick(position: Int, facilityLogData: ReservationFacilityLog) {
                showCancelWarningDialog(facilityLogData.documentId) }
        })
        viewbinding.reservationDetailRv.adapter = reservationDetailFacilityLogRVAdapter
    }

    private fun showEmptyView(){
        viewbinding.apply {
            reservationDetailEmptyView.visibility = View.VISIBLE
            reservationDetailRv.visibility = View.GONE
        }
    }
    private fun showRV(list : List<ReservationFacilityLog>){
        viewbinding.run{
            reservationDetailEmptyView.visibility  = View.GONE
            reservationDetailRv.visibility = View.VISIBLE
            reservationDetailFacilityLogRVAdapter.submitList(list)
        }
    }
    private fun showCancelWarningDialog(documentId : String){
        //TODO : 알람 처리 해주기.
        val dialog = WrapedDialogBasicTwoButton(requireContext(), "강제취소시 복구할 수 없습니다.\n"+
                "정말 해당 정보를 취소하시겠습니까?\n", "닫기", "강제취소").apply {
            clickListener = object : WrapedDialogBasicTwoButton.DialogButtonClickListener{
                override fun dialogCloseClickListener() { dismiss() }
                override fun dialogCustomClickListener() {
                    viewmodel.cancelReservationFacilityLogData(documentId)
                    dismiss()
                }
            }
        }
        showDialog(dialog, viewLifecycleOwner)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        observer?.let {observer ->
            viewmodel.getReservationFacilityNotDoneLogDataList("예약 사용", facilityBundleData.name).removeObserver(observer) }
    }
}

class ReservationDetailFacilityLogRVAdapter(private val context : Context,  private val listener : OnItemClickListener) : ListAdapter<ReservationFacilityLog, ReservationDetailFacilityLogRVAdapter.ViewHolder>(
    AddressDiffCallback
) {
    companion object {
        val AddressDiffCallback = object : DiffUtil.ItemCallback<ReservationFacilityLog>() {
            override fun areItemsTheSame(oldItem: ReservationFacilityLog, newItem: ReservationFacilityLog): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ReservationFacilityLog, newItem: ReservationFacilityLog): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface OnItemClickListener { fun onItemClick(position: Int, facilityLogData : ReservationFacilityLog) }

    inner class ViewHolder(val binding: ItemReservationDetail2Binding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemReservationDetail2Binding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { item ->
            CoroutineScope(Dispatchers.Main).launch {
                if (item.reservationState == "사용중"){
                    holder.binding.itemReservationDeleteBtn.text = "사용취소"
                    holder.binding.itemReservationDeleteBtn.background = ContextCompat.getDrawable(context, R.drawable.button_10dp_rectangle_pinkish_orange) }
                else { holder.binding.itemReservationDeleteBtn.background = ContextCompat.getDrawable(context, R.drawable.button_10dp_rectangle_bluegreen)  }
                holder.binding.itemReservationDetailMonth.text = withContext(Dispatchers.IO){ getMonthString(item.startTime) }
                holder.binding.itemReservationDetailDay.text = withContext(Dispatchers.IO){ getMonthDayString(item.startTime) }
                holder.binding.itemReservationDetailStartTime.text = withContext(Dispatchers.IO) { getHourMinuteString(item.startTime) }
                holder.binding.itemReservationDetailEndTime.text = withContext(Dispatchers.IO) { getHourMinuteString(item.endTime) }
                holder.binding.itemReservationDetailIntervalTime.text = withContext(Dispatchers.IO) { getIntervalMinuteString(item.startTime, item.endTime)}
                holder.binding.itemReservationDetailWeekDay.text = withContext(Dispatchers.IO){ getWeekDayString(item.startTime) }
                holder.binding.itemReservationDetailUserName.text = item.userName
                holder.binding.itemReservationDeleteBtn.setOnClickListener { listener.onItemClick(position, item) }
            }
        }
    }
}