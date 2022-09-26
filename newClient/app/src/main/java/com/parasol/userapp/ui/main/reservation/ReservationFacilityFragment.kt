package com.parasol.userapp.ui.main.reservation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.parasol.userapp.databinding.FragmentMainhomeReservationFacilityBinding
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.parasol.userapp.R
import com.parasol.userapp.ui.base.BaseFragment
import com.parasol.userapp.ui.base.BaseSessionFragment
import com.parasol.userapp.data.model.ReservationEquipment
import com.parasol.userapp.data.model.ReservationFacility
import com.parasol.userapp.databinding.FragmentMainhomeReservationEquipmentItemBinding
import com.parasol.userapp.databinding.FragmentMainhomeReservationFacilityItemBinding
import com.parasol.userapp.ui.main.MainFragment
import com.parasol.userapp.ui.main.MainFragmentDirections
import com.parasol.userapp.utils.ConfirmUsingDialog
import com.parasol.userapp.utils.InputUsingTimeDialog
import com.google.firebase.firestore.FirebaseFirestore

class ReservationFacilityFragment :
    BaseSessionFragment<FragmentMainhomeReservationFacilityBinding, ReservationViewModel>() {
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
        viewbinding.facilityRecyclerView.layoutManager = LinearLayoutManager(context)
        viewbinding.facilityRecyclerView.adapter = FacilityAdapter(
            emptyList(),
            onClickUsingIcon = {
                findNavController().navigate(MainFragmentDirections.actionMainFragmentToReservationFacility(it) )
            }
        )
    }
    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.FacilityLiveData.observe(viewLifecycleOwner, {
            (viewbinding.facilityRecyclerView.adapter as FacilityAdapter).setData(it)
        })
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewmodel.getFacilityData()
    }
}

class FacilityAdapter(
    private var dataSet: List<ReservationFacility>,
    val onClickUsingIcon: (ReservationFacility: ReservationFacility) -> Unit
) :
    RecyclerView.Adapter<FacilityAdapter.FacilityViewHolder>() {

    class FacilityViewHolder(val viewbinding: FragmentMainhomeReservationFacilityItemBinding) :
        RecyclerView.ViewHolder(viewbinding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): FacilityViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.fragment_mainhome_reservation_facility_item, viewGroup, false)
        return FacilityViewHolder(FragmentMainhomeReservationFacilityItemBinding.bind(view))
    }

    override fun onBindViewHolder(viewHolder: FacilityViewHolder, position: Int) {
        val data = dataSet[position]
//        viewHolder.viewbinding.icon.load(data.)
        viewHolder.viewbinding.documentNameTextview.text = data.document_name
        viewHolder.viewbinding.icon.load(data.category_icon)
        //사용하기 버튼
        viewHolder.viewbinding.reservationBtn.setOnClickListener() {
            onClickUsingIcon.invoke(data)
        }
    }

    //라이브데이터 값이 변경되었을 때 필요한 메소 - 데이터갱신
    fun setData(newData: List<ReservationFacility>) {
        dataSet = newData
        notifyDataSetChanged()
    }

    override fun getItemCount() = dataSet.size
}