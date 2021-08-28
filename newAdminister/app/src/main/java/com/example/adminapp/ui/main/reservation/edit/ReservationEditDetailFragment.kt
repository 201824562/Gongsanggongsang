package com.example.adminapp.ui.main.reservation.edit

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.adminapp.R
import com.example.adminapp.base.BaseSessionFragment
import com.example.adminapp.data.model.*
import com.example.adminapp.databinding.DialogReservationEditCategoryBinding
import com.example.adminapp.databinding.FragmentReservationEditDetailBinding
import com.example.adminapp.ui.main.reservation.add.ReservationAddCategoryRVAdapter
import com.example.adminapp.ui.main.reservation.add.ReservationUnableTimeRVAdapter
import com.example.adminapp.utils.WrapedDialogBasicTwoButton
import com.example.adminapp.utils.hideKeyboard
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ReservationEditDetailFragment: BaseSessionFragment<FragmentReservationEditDetailBinding, ReservationEditDetailViewModel>() {
    override lateinit var viewbinding: FragmentReservationEditDetailBinding
    override val viewmodel: ReservationEditDetailViewModel by viewModels()
    private val args : ReservationEditDetailFragmentArgs by navArgs()

    private var reservationType : ReservationType = ReservationType.EQUIPMENT
    private lateinit var reservationItemData : ReservationItem
    private var reservationUnableTimeType : ReservationUnableTimeType? = null
    private var selectedUnableTimeList : List<ReservationUnableTimeItem>? = null
    private lateinit var unableReserveTimeRVAdapter: ReservationUnableTimeRVAdapter
    private lateinit var unableRVlinearLayoutManager : LinearLayoutManager

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewbinding = FragmentReservationEditDetailBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        viewbinding.fragmentContent.setOnClickListener { hideKeyboard(it) }
        viewbinding.backBtn.setOnClickListener { findNavController().navigate(R.id.action_reservationEditDetailFragment_pop) }

        when (args.reservationItem){
            null -> makeErrorEvent()
            else -> {
                reservationItemData = args.reservationItem!!
                reservationType = reservationItemData.type
                reservationUnableTimeType = when(reservationItemData.data.intervalTime){
                    30L -> ReservationUnableTimeType.HALF_HOUR
                    60L -> ReservationUnableTimeType.HOUR
                    else -> null
                }
            }
        }
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        when (reservationType){
            ReservationType.EQUIPMENT -> setEquipmentMode()
            ReservationType.FACILITY -> setFacilityMode()
        }
        setRecyclerView()
        setSpinnerIntervalTimeView()
        setTimeTabView()

        viewmodel.run {
            onSuccessDeleteReserveData.observe(viewLifecycleOwner){
                findNavController().navigate(R.id.action_reservationEditDetailFragment_pop)
            }
            onSuccessUpdateReservationItem.observe(viewLifecycleOwner){
                findNavController().navigate(R.id.action_reservationEditDetailFragment_pop)
            }
        }
    }


    override fun initViewFinal(savedInstanceState: Bundle?) {

        viewbinding.run {
            reserveEditDetailIcon.setOnClickListener {
                showCategoryDialog()
            }
            unableReserveTimeButton.setOnClickListener {
                when (it.isSelected) {
                    true -> setUnableModeOff()
                    false -> {
                        if (checkIntervalTimeSelected()) { setUnableModeOn() }
                        else showSnackbar("사용 단위 시간 설정 후, 설정 가능합니다.")
                    }
                }
            }
            reservationEditDeleteBtn.setOnClickListener {
                deleteDialog()
            }
            reserveEditBtn.setOnClickListener {
                if (checkDataUpdate()) {
                    when (reservationType){
                        ReservationType.EQUIPMENT -> {
                            val nameChanged = reservationItemData.data.name != getEditItemName()
                            val oldName : String = getEditItemName()
                            showEditDialog(nameChanged,  oldName , false,"해당 정보로 변경하시겠습니까?\n" + "변경후에는 되돌릴 수 없습니다.\n")
                        }
                        ReservationType.FACILITY -> {
                            val nameChanged : Boolean = reservationItemData.data.name != getEditItemName()
                            val oldName : String = getEditItemName()
                            if (reservationItemData.unableTimeList != selectedUnableTimeList){ showEditDialog(nameChanged, oldName, true,
                                "사용 불가능 시간 설정을 변경하셨습니다.\n현재 예약자들의 예약내역이 모두 삭제되며 되돌릴 수 없습니다.\n" +
                                        "계속하시겠습니까?")
                            }else showEditDialog(nameChanged, oldName, false, "해당 정보로 변경하시겠습니까?\n" + "변경후에는 되돌릴 수 없습니다.\n")
                        }
                    }
                }
            }
        }
    }

    private fun makeErrorEvent(){
        showToast("알수 없는 에러가 발생했습니다. 다시 시도해주세요.")
        findNavController().navigate(R.id.action_reservationEditDetailFragment_pop)
    }

    private fun setEquipmentMode(){
        viewbinding.run {
            toolbarText.text = "바로 사용 편집"
            reserveEquipmentLayout.visibility = View.VISIBLE
            reserveLaterLayout.visibility = View.GONE
            setCommonItemData()
            editTextReserveMaxTime.setText(reservationItemData.data.maxTime.toString())
        }
    }

    private fun setFacilityMode(){
        viewbinding.run {
            toolbarText.text = "예약 사용 편집"
            reserveEquipmentLayout.visibility = View.GONE
            reserveLaterLayout.visibility = View.VISIBLE
            setCommonItemData()
            //setSpinnerIntervalTimeView()
        }
    }

    private fun setCommonItemData(){
        viewbinding.run {
            reserveEditDetailIcon.load(reservationItemData.data.icon)
            editTextReserveName.setText(reservationItemData.data.name)
        }
    }

    private fun showCategoryDialog(){
        val bottomSheetFragment = BottomDialogReservationEditCategory(){
            viewbinding.reserveEditDetailIcon.load(it)
            reservationItemData.data.icon = it
        }
        bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
    }

    private fun setRecyclerView(){
        unableRVlinearLayoutManager = LinearLayoutManager(context)
        unableRVlinearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        unableReserveTimeRVAdapter = ReservationUnableTimeRVAdapter(object : ReservationUnableTimeRVAdapter.OnItemClickListener {
            override fun onItemClick(currentItemList: List<ReservationUnableTimeItem>) {
                selectedUnableTimeList = currentItemList
            }
        })
        viewbinding.unableReserveTimeRV.apply {
            layoutManager = unableRVlinearLayoutManager
            adapter = unableReserveTimeRVAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun setSpinnerIntervalTimeView(){
        viewbinding.spinnerReserveTime.apply {
            ArrayAdapter.createFromResource(requireContext(), R.array.reserve_time, R.layout.spinner_reserve_selected
            ).also { adapter ->
                adapter.setDropDownViewResource(R.layout.spinner_reserve_list_item)
                this.adapter = adapter
            }
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    setSpinnerMaxTimeView(position)
                    when(getSpinnerIntervalTime()){
                        0L -> setUnableModeOff()
                        else -> setRVUnableTimeSettings(getSpinnerIntervalTime())
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) { }
            }
            setSelection(getReserveTimeIndex(reservationUnableTimeType), false)
        }
    }
    private fun getReserveTimeIndex(reserveType : ReservationUnableTimeType?) : Int {
        return when (reserveType){
            ReservationUnableTimeType.HALF_HOUR -> 1
            ReservationUnableTimeType.HOUR -> 2
            else -> 0
        }
    }
    private fun getMaxTimeIndex(reserveType : ReservationUnableTimeType?, maxTime: Long): Int {
        return when (reserveType){
            ReservationUnableTimeType.HALF_HOUR -> {
                when (maxTime){
                    30L -> 1
                    60L -> 2
                    90L -> 3
                    120L -> 4
                    150L -> 5
                    180L -> 6
                    else -> 0
                }
            }
            ReservationUnableTimeType.HOUR -> {
                when (maxTime){
                    60L -> 1
                    120L -> 2
                    180L -> 3
                    else -> 0
                }
            }
            else -> 0
        }
    }
    private fun getMaxTimeTypeStringArrayResId(position: Int): Int {
        return when (position) {
            0 -> R.array.reserve_max_time
            1 -> R.array.reserve_max_time30
            2 -> R.array.reserve_max_time60
            else -> R.array.reserve_max_time
        }
    }
    private fun setSpinnerMaxTimeView(position: Int){
        val maxTimeTypeResId = getMaxTimeTypeStringArrayResId(position)
        viewbinding.spinnerReserveMaxTime.apply {
            isEnabled = when (maxTimeTypeResId) {
                R.array.reserve_max_time -> false
                else -> true
            }
            ArrayAdapter.createFromResource(requireContext(), maxTimeTypeResId, R.layout.spinner_reserve_selected
            ).also { adapter ->
                adapter.setDropDownViewResource(R.layout.spinner_reserve_list_item)
                this.adapter = adapter
            }
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) { }
                override fun onNothingSelected(parent: AdapterView<*>?) { }
            }
            if (selectedUnableTimeList == null) setSelection(getMaxTimeIndex(reservationUnableTimeType, reservationItemData.data.maxTime))
        }
    }

    private fun setTimeTextView(time : Int){
        when (time){
            0 -> {
                viewbinding.timeText0.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                viewbinding.timeText6.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_50))
                viewbinding.timeText12.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_50))
                viewbinding.timeText18.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_50)) }
            6 -> {
                viewbinding.timeText6.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                viewbinding.timeText0.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_50))
                viewbinding.timeText12.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_50))
                viewbinding.timeText18.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_50)) }
            12 -> {
                viewbinding.timeText12.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                viewbinding.timeText0.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_50))
                viewbinding.timeText6.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_50))
                viewbinding.timeText18.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_50)) }
            18 -> {
                viewbinding.timeText18.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                viewbinding.timeText0.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_50))
                viewbinding.timeText6.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_50))
                viewbinding.timeText12.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_50)) }
        }
    }
    private fun setTimeTabView(){
        viewbinding.run {
            unableReserveTimeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
                val timeTabValue = when (checkedId){
                    timeText0.id -> 0
                    timeText6.id -> 6
                    timeText12.id -> 12
                    timeText18.id -> 18
                    else -> 0 }
                setTimeTextView(timeTabValue)
                scrollRVToTabTime(timeTabValue)
            }
        }
    }
    private fun scrollRVToTabTime(time : Int){
        val position = when (reservationUnableTimeType){
            ReservationUnableTimeType.HALF_HOUR -> {
                if (time==0) 0
                else time*2
            }
            ReservationUnableTimeType.HOUR -> {
                if (time==0) 0
                else time
            }
            else -> 0
        }
        unableRVlinearLayoutManager.scrollToPositionWithOffset(position, 0)
    }
    private fun setRVUnableTimeSettings(intervalTime : Long) {
        viewbinding.unableReserveTimeRadioGroup.clearCheck()
        when {
            selectedUnableTimeList == null -> {
                selectedUnableTimeList = (reservationItemData.unableTimeList).map { it.copy() }
                unableReserveTimeRVAdapter.submitList(selectedUnableTimeList)
            }
            intervalTime == 30L -> {
                reservationUnableTimeType = ReservationUnableTimeType.HALF_HOUR
                selectedUnableTimeList = ReservationUnableTimeType.getReservationUnableTimeList(ReservationUnableTimeType.HALF_HOUR).toList()
                unableReserveTimeRVAdapter.submitList(selectedUnableTimeList)
            }
            else -> {
                reservationUnableTimeType = ReservationUnableTimeType.HOUR
                selectedUnableTimeList = ReservationUnableTimeType.getReservationUnableTimeList(ReservationUnableTimeType.HOUR).toList()
                unableReserveTimeRVAdapter.submitList(selectedUnableTimeList)
            }
        }
    }

    private fun setUnableModeOff(){
        reservationUnableTimeType = null
        viewbinding.unableReserveTimeButton.isSelected = false
        viewbinding.unableReserveTimeButton.background = ContextCompat.getDrawable(requireContext(), R.drawable.button_toggle_off)
        viewbinding.unableReserveTimeLayout.visibility = View.GONE  }

    private fun setUnableModeOn(){
        viewbinding.unableReserveTimeButton.isSelected = true
        viewbinding.unableReserveTimeButton.background = ContextCompat.getDrawable(requireContext(),R.drawable.button_toggle_on)
        viewbinding.unableReserveTimeLayout.visibility = View.VISIBLE }

    private fun deleteDialog(){
        val dialog = WrapedDialogBasicTwoButton(requireContext(), "카테고리를 삭제하면,\n해당 카테고리로 예약 된\n"+
                "모든 예약정보가 삭제됩니다.\n정말 삭제하시겠어요?", "취소", "삭제").apply {
            clickListener = object : WrapedDialogBasicTwoButton.DialogButtonClickListener{
                override fun dialogCloseClickListener() { dismiss() }
                override fun dialogCustomClickListener() {
                    viewmodel.deleteReservationData(reservationType, reservationItemData.data.name)
                    dismiss()
                }
            }
        }
        showDialog(dialog, viewLifecycleOwner)
    }

    private fun getEditItemName() : String = viewbinding.editTextReserveName.text.toString().trim()
    private fun getSpinnerIntervalTime() : Long {
        return when {
            reservationType == ReservationType.EQUIPMENT -> 0L
            viewbinding.spinnerReserveTime.selectedItemPosition == 0 -> { 0L }
            else -> { makeSpinnerTimeIntFromString(viewbinding.spinnerReserveTime.selectedItem.toString()) }
        }
    }
    private fun getEditMaxTime() : String = viewbinding.editTextReserveMaxTime.text.toString().trim()
    private fun getSpinnerMaxTime() : Long {
        return if (viewbinding.spinnerReserveMaxTime.selectedItemPosition == 0){ 0L }
        else { makeSpinnerTimeIntFromString(viewbinding.spinnerReserveMaxTime.selectedItem.toString()) }
    }
    private fun getMaxTime() : Long {
        return when (reservationType){
            ReservationType.EQUIPMENT -> getEditMaxTime().toLong()
            ReservationType.FACILITY -> getSpinnerMaxTime()
        }
    }
    private fun makeSpinnerTimeIntFromString(spinnerValue : String) : Long { return spinnerValue.replace("분", "").toLong() }

    private fun checkIntervalTimeSelected() : Boolean {
        if (getSpinnerIntervalTime()==0L) return false
        return true
    }

    private fun checkDataUpdate() : Boolean {
        if (getEditItemName().isBlank() || getEditItemName().isEmpty()) {
            showSnackbar("물품 이름을 입력해주세요.")
            return false
        }else {
            when (reservationType){
                ReservationType.EQUIPMENT -> {
                    return if (getEditMaxTime().isBlank() || getEditMaxTime().isEmpty() ) {
                        showSnackbar("최대 시간을 입력해주세요.")
                        false
                    } else true
                }
                ReservationType.FACILITY ->{
                    return when {
                        getSpinnerIntervalTime() == 0L -> {
                            showSnackbar("사용 단위 시간을 선택해주세요.")
                            false }
                        getSpinnerMaxTime() == 0L -> {
                            showSnackbar("최대 사용가능 시간을 선택해주세요.")
                            false }
                        else -> true
                    }
                }
            }
        }
    }

    private fun updateReservationData(reservationType: ReservationType){
        when (reservationType){
            ReservationType.EQUIPMENT -> {
                reservationItemData.data.name = getEditItemName()
                reservationItemData.data.maxTime = getMaxTime()
            }
            ReservationType.FACILITY -> {
                reservationItemData.data.name = getEditItemName()
                reservationItemData.data.intervalTime = getSpinnerIntervalTime()
                reservationItemData.data.maxTime = getMaxTime()
                reservationItemData.unableTimeList = selectedUnableTimeList!!
            }
        }
    }
    private fun showEditDialog(nameChanged : Boolean, oldName : String, timeSetChanged : Boolean,  msg : String){
        val dialog = WrapedDialogBasicTwoButton(requireContext(), msg, "취소", "변경하기").apply {
            clickListener = object : WrapedDialogBasicTwoButton.DialogButtonClickListener{
                override fun dialogCloseClickListener() { dismiss() }
                override fun dialogCustomClickListener() {
                    //TODO : 알람기능이랑 연결하기 ((reservationItemData.unableTimeList != selectedUnableTimeList)의 경우)
                    updateReservationData(reservationType)
                    viewmodel.updateReservationData(nameChanged, oldName, timeSetChanged, reservationItemData)
                    dismiss()
                }
            }
        }
        showDialog(dialog, viewLifecycleOwner)
    }

}

class BottomDialogReservationEditCategory(private val itemClick :(Int) -> Unit) : BottomSheetDialogFragment() {

    lateinit var viewbinding : DialogReservationEditCategoryBinding
    lateinit var categoryIconRVAdapter: ReservationAddCategoryRVAdapter
    private var selectedIconDrawableID : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogStyle)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewbinding = DialogReservationEditCategoryBinding.inflate(inflater, container, false)
        return viewbinding.root
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener {
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet!!)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED }
        return dialog
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()
        categoryIconRVAdapter.submitList(getDrawableList())

        viewbinding.saveEditCategoryBtn.setOnClickListener {
            if (selectedIconDrawableID == null) setCategoryWarningMessage()
            else {
                itemClick(selectedIconDrawableID!!)
                dismiss()
            }
        }
    }

    private fun setRecyclerView(){
        categoryIconRVAdapter = ReservationAddCategoryRVAdapter(object : ReservationAddCategoryRVAdapter.OnItemClickListener {
            override fun onItemClick(resourceID: Int?) { selectedIconDrawableID = resourceID }
        })
        viewbinding.addCategoryRv.apply {
            adapter = categoryIconRVAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun getDrawableList()  = CategoryResources.makeListToClass()
    private fun setCategoryWarningMessage() { viewbinding.textEditCategoryWarning.text = "아이콘을 선택해주세요."}

}