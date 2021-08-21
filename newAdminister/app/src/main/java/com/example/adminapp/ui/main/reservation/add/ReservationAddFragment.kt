package com.example.adminapp.ui.main.reservation.add

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminapp.R
import com.example.adminapp.base.BaseSessionFragment
import com.example.adminapp.data.model.*
import com.example.adminapp.databinding.DialogReservationAddCategoryBinding
import com.example.adminapp.databinding.FragmentReservationAddBinding
import com.example.adminapp.utils.WrapedDialogBasicTwoButton
import com.example.adminapp.utils.hideKeyboard
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ReservationAddFragment : BaseSessionFragment<FragmentReservationAddBinding, ReservationAddViewModel>() {
    override lateinit var viewbinding: FragmentReservationAddBinding
    override val viewmodel: ReservationAddViewModel by viewModels()
    private val args : ReservationAddFragmentArgs by navArgs()

    private lateinit var categoryItemListRVAdapter: ReservationAddRVAdapter
    private lateinit var unableReserveTimeRVAdapter: ReservationUnableTimeRVAdapter
    private lateinit var unableRVlinearLayoutManager : LinearLayoutManager
    private var reservationType : ReservationType = ReservationType.EQUIPMENT
    private var reservationUnableTimeType : ReservationUnableTimeType? = null
    private var selectedCategoryData : CategoryData? = null
    private var selectedUnableTimeList : List<ReservationUnableTimeItem> = listOf()

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentReservationAddBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        viewbinding.fragmentContent.setOnClickListener { hideKeyboard(it) }
        viewbinding.backBtn.setOnClickListener { findNavController().navigate(R.id.action_reservationAddFragment_pop) }
        if (args.reservationType == ReservationType.FACILITY ) reservationType = ReservationType.FACILITY
        checkReservationType()
        setRecyclerView()
        setTimeTabView()
        viewmodel.loadCategoryItemViewList()
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.run {
            categoryItemItemList.observe(viewLifecycleOwner){
                val convertedList = it.toMutableList().apply { add(0, CategoryItem(RecyclerDataType.NONE, null)) }
                categoryItemListRVAdapter.run {
                    submitList(convertedList.toList())
                    clearSelectedVariables()
                }
            }
            onSuccessSaveCategoryItem.observe(viewLifecycleOwner){ viewmodel.loadCategoryItemViewList() }
            onSuccessDeleteCategoryItem.observe(viewLifecycleOwner){ viewmodel.loadCategoryItemViewList() }
            checkingSelectedUsingTime.observe(viewLifecycleOwner){
                when(it){
                    null -> setUnableModeOff()
                    else -> setRVUnableTimeSettings(it)
                }
            }
            onSuccessSaveReservationItem.observe(viewLifecycleOwner){   findNavController().navigate(R.id.action_reservationAddFragment_pop) }
        }
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.run {
            unableReserveTimeButton.setOnClickListener {
                when (it.isSelected) {
                    true -> setUnableModeOff()
                    false -> {
                        if (checkUsingTimeSelected()) { setUnableModeOn() }
                        else showSnackbar("사용 단위 시간 설정 후, 설정 가능합니다.")
                    }
                }
            }
            reserveAddBtn.setOnClickListener {
                if (checkDataSave()) {
                    showEditDialog("해당 정보로 추가하시겠습니까?\n" + "물품/시설 이름은 후에 수정할 수 없습니다.\n")
                }
            }
        }
    }


    private fun checkReservationType(){
        when(reservationType){
            ReservationType.EQUIPMENT -> setEquipmentMode()
            ReservationType.FACILITY -> setFacilityMode()
        }
    }

    private fun setEquipmentMode(){
        viewbinding.run {
            toolbarText.text = "바로 사용 추가"
            reserveEquipmentLayout.visibility = View.VISIBLE
            reserveLaterLayout.visibility = View.GONE
        }
    }

    private fun setFacilityMode(){
        viewbinding.run {
            toolbarText.text = "예약 사용 추가"
            reserveEquipmentLayout.visibility = View.GONE
            reserveLaterLayout.visibility = View.VISIBLE
            setSpinnerUsingTimeView()
        }
    }

    private fun setRecyclerView() {
        categoryItemListRVAdapter = ReservationAddRVAdapter(object : ReservationAddRVAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, categoryData: CategoryData?, askDelete : Boolean) {
                if (position == 0){ showCategoryDialog()}
                else{
                    when (askDelete){
                        false -> selectedCategoryData = categoryData
                        true -> {
                            AlertDialog.Builder(requireContext(), R.style.MyAlertDialogStyle)
                                .setTitle("해당 카테고리를 삭제하시겠습니까?")
                                .setPositiveButton("확인") { _, _ ->
                                    viewmodel.deleteCategoryItem(categoryData!!) }
                                .setNegativeButton("취소") { dialog, _ ->
                                    dialog.dismiss() }
                                .show()
                        }
                    }
                }
            }
        })
        viewbinding.addImageRv.apply {
            adapter = categoryItemListRVAdapter
            isNestedScrollingEnabled = false
        }

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

    private fun setSpinnerUsingTimeView(){
        viewbinding.spinnerReserveTime.apply {
            ArrayAdapter.createFromResource(requireContext(), R.array.reserve_time, R.layout.spinner_reserve_selected
            ).also { adapter ->
                adapter.setDropDownViewResource(R.layout.spinner_reserve_list_item)
                this.adapter = adapter
            }
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    setSpinnerMaxTimeView(position)
                    viewmodel.checkSelectedUsingTimeSetting(getSpinnerUsingTime())
                }
                override fun onNothingSelected(parent: AdapterView<*>?) { }
            }
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
    private fun setRVUnableTimeSettings(usingTime : Long) {
        viewbinding.unableReserveTimeRadioGroup.clearCheck()
        reservationUnableTimeType = if (usingTime == 30L){ ReservationUnableTimeType.HALF_HOUR }
        else { ReservationUnableTimeType.HOUR }
        makeNewUnableTimeList()
        unableReserveTimeRVAdapter.submitList(selectedUnableTimeList)
    }
    private fun makeNewUnableTimeList(){
        selectedUnableTimeList = if (reservationUnableTimeType == ReservationUnableTimeType.HALF_HOUR){
            ReservationUnableTimeType.getReservationUnableTimeList(ReservationUnableTimeType.HALF_HOUR).toList() }
        else {
            ReservationUnableTimeType.getReservationUnableTimeList(ReservationUnableTimeType.HOUR).toList()
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

    private fun showCategoryDialog(){
        val bottomSheetFragment = BottomDialogReservationAddCategory(){
            viewmodel.saveCategoryItem(it)
        }
        bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
    }

    private fun getEditItemName() : String = viewbinding.editTextReserveName.text.toString().trim()
    private fun getSpinnerUsingTime() : Long {
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

    private fun checkUsingTimeSelected() : Boolean {
        if (getSpinnerUsingTime()==0L) return false
        return true
    }
    private fun getUnableSelectedTimeList(): List<ReservationUnableTimeItem> { return selectedUnableTimeList }

    private fun checkDataSave() : Boolean {
        if (selectedCategoryData == null) {
            showSnackbar("카테고리룰 선택해주세요.")
            return false }
        else if (getEditItemName().isBlank() || getEditItemName().isEmpty()) {
            showSnackbar("물품 이름을 입력해주세요.")
            return false    }
        else {
            when (reservationType){
                ReservationType.EQUIPMENT -> {
                    return if (getEditMaxTime().isBlank() || getEditMaxTime().isEmpty() ) {
                        showSnackbar("최대 시간을 입력해주세요.")
                        false
                    } else { true }
                }
                ReservationType.FACILITY ->{
                    return when {
                        getSpinnerUsingTime() == 0L -> {
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

    private fun showEditDialog(msg : String){
        val dialog = WrapedDialogBasicTwoButton(requireContext(), msg, "취소", "추가하기").apply {
            clickListener = object : WrapedDialogBasicTwoButton.DialogButtonClickListener{
                override fun dialogCloseClickListener() { dismiss() }
                override fun dialogCustomClickListener() {
                    when (viewbinding.unableReserveTimeButton.isSelected){
                        true -> viewmodel.saveReservationData(ReservationItem(reservationType, ReservationData(selectedCategoryData!!.iconID,
                            getEditItemName(), getSpinnerUsingTime(), getMaxTime()), getUnableSelectedTimeList()))
                        false -> {
                            makeNewUnableTimeList()
                            viewmodel.saveReservationData(ReservationItem(reservationType, ReservationData(selectedCategoryData!!.iconID,
                                getEditItemName(), getSpinnerUsingTime(), getMaxTime()), getUnableSelectedTimeList()))
                        }
                    }
                    dismiss()
                }
            }
        }
        showDialog(dialog, viewLifecycleOwner)
    }

}


class BottomDialogReservationAddCategory(private val itemClick :(CategoryItem) -> Unit) : BottomSheetDialogFragment() {

    lateinit var viewbinding : DialogReservationAddCategoryBinding
    lateinit var categoryIconRVAdapter: ReservationAddCategoryRVAdapter
    private var selectedIconDrawableID : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogStyle)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewbinding = DialogReservationAddCategoryBinding.inflate(inflater, container, false)
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

        viewbinding.signupNextbtn.setOnClickListener {
            if (selectedIconDrawableID == null) setCategoryWarningMessage("아이콘을 선택해주세요.")
            else if (getCategoryName().isBlank() || getCategoryName().isEmpty()) setCategoryWarningMessage("카테고리 이름을 입력해주세요.")
            else {
                itemClick(CategoryItem(RecyclerDataType.DATA, CategoryData( selectedIconDrawableID!!, getCategoryName())))
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
    private fun getCategoryName() = viewbinding.editTextAddCategoryName.text.toString().trim()
    private fun setCategoryWarningMessage(errorMessage: String) { viewbinding.textAddCategoryWarning.text = errorMessage }

}


