package com.example.userapp.ui.signup

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.example.userapp.R
import com.example.userapp.base.BaseFragment
import com.example.userapp.base.BaseSessionFragment
import com.example.userapp.data.model.Agency
import com.example.userapp.databinding.FragmentSignupAgencyBinding
import com.example.userapp.utils.setupKeyboardHide

class SignUpAgencyFragment : BaseSessionFragment<FragmentSignupAgencyBinding, SignUpViewModel>() {
    override lateinit var viewbinding: FragmentSignupAgencyBinding
    override val viewmodel: SignUpViewModel by navGraphViewModels(R.id.signUpGraph)

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentSignupAgencyBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    private lateinit var searchListAdapter: SearchAgencyListAdapter
    private var query : String = ""
    private var nextBtnAvailable : Boolean = false

    private fun clearVariables(){
        query = ""
        if (viewmodel.cameFromPermission) viewmodel.clearAgencyResult(false)
        //if (args.emptyStateAgencyFrag) viewmodel.clearAgencyResult(false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initViewStart(savedInstanceState: Bundle?) {
        setupKeyboardHide(viewbinding.fragmentRootLayout, activity)
        setRecyclerView()
        setSearchEditTextView()
        getCheckedInfo()
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.run {
            agencyDataList.observe(viewLifecycleOwner){
                val agencyList = it
                searchListAdapter.clearCheckedVariables()
                if (it.isEmpty()){
                    clearAgencyVars()
                    observeAgencyBtnState()
                    showEmptyView() }
                else {
                    if (selectedAgency != null){
                        agencyList.forEach { agency -> if (agency.name == selectedAgency!!.name) agency.clicked = true }
                        searchListAdapter.submitList(agencyList)
                    }else searchListAdapter.submitList(agencyList)
                    showRecyclerView()
                }
            }
            invalidSearchResultEventLiveData.observe(viewLifecycleOwner) { setSearchResultErrorMessage(it) }
            validSearchResultEventLiveData.observe(viewLifecycleOwner){ setSearchResultEmptyMessage() }
            checkAgencyClickedState.observe(viewLifecycleOwner){
                if (selectedAgency == null) {
                    clickedAgencyBtn = false
                    makeNextButtonNotAvailable() }
                else {
                    clickedAgencyBtn = true
                    makeNextButtonAvailable()
                }
            }
        }

        viewbinding.signupSearchBtn.setOnClickListener {
            query = viewbinding.signupSearchEdit.query.toString()
            if(query.isEmpty()) {
                viewmodel.loadSearchAgencyResult(null, true)
                //viewmodel.clearAgencyResult(true)
            }else{
                submitQuery(query.lines().first().trim()) }
        }
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewmodel.observeAgencyBtnState()
        viewbinding.run {
            signupNextbtn.setOnClickListener {
                if (nextBtnAvailable) findNavController().navigate(R.id.action_signUpAgencyFragment_to_signUpFirstFragment)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        clearVariables()
    }

    private fun setRecyclerView() {
        searchListAdapter = SearchAgencyListAdapter(object : SearchAgencyListAdapter.OnItemClickListener {
            override fun onItemClick(v: View, position: Int, agencyInfo: Agency?, checkBox: CheckBox?, checkPos: Int?) {
                viewmodel.selectedAgency = agencyInfo
                viewmodel.observeAgencyBtnState() } })
        viewbinding.signupSearchResultRv.adapter = searchListAdapter
        viewbinding.signupSearchResultRv.setHasFixedSize(true)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setSearchEditTextView(){
        viewbinding.run {
            signupSearchEdit.setIconifiedByDefault(false)
            signupSearchEdit.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)?.let{ editText ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    editText.setHintTextColor(resources.getColor(R.color.black_60, null))
                    editText.setTextColor(resources.getColor(R.color.black, null))
                    editText.typeface = resources.getFont(R.font.notosan_font_family)
                } else {
                    editText.setHintTextColor(resources.getColor(R.color.black_60))
                    editText.setTextColor(resources.getColor(R.color.black))
                    editText.typeface = resources.getFont(R.font.notosan_font_family)
                }
            }
            signupSearchEdit.setOnQueryTextListener( object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if(query.isNullOrEmpty()){ viewmodel.loadSearchAgencyResult(null, true) }
                    else{
                        signupSearchEdit.clearFocus()
                        submitQuery(query.lines().first().trim()) }
                    return true
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    return true }
            })
        }
    }

    private fun submitQuery(query:String){ viewmodel.loadSearchAgencyResult(query, false) }

    private fun getCheckedInfo() {
        if (viewmodel.selectedAgency == null) viewmodel.loadSearchAgencyResult(null, true)
        when (viewmodel.clickedAgencyBtn) {
            true -> makeNextButtonAvailable()
            false -> makeNextButtonNotAvailable()
        }
    }

    private fun showEmptyView(){
        viewbinding.apply {
            signupSearchResultEmptyView.visibility = View.VISIBLE
            signupSearchResultRv.visibility = View.GONE
        }
    }

    private fun showRecyclerView(){
        viewbinding.apply {
            signupSearchResultEmptyView.visibility = View.GONE
            signupSearchResultRv.visibility = View.VISIBLE
        }
    }

    private fun setSearchResultErrorMessage(message: String) {
        viewbinding.signupSearchWarning.apply {
            text = message
            visibility = View.VISIBLE
        }
    }

    private fun setSearchResultEmptyMessage() {
        viewbinding.signupSearchWarning.text = "" }


    private fun makeNextButtonAvailable() {
        nextBtnAvailable = true
        viewbinding.signupNextbtn.setBackgroundResource(R.drawable.button_5dp_rectangle_bluegreen)
    }

    private fun makeNextButtonNotAvailable() {
        nextBtnAvailable = false
        viewbinding.signupNextbtn.setBackgroundResource(R.drawable.button_5dp_rectangle_black20)
    }

}