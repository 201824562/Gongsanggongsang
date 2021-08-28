package com.example.userapp.ui.signup

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.example.userapp.R
import com.example.userapp.base.BaseFragment
import com.example.userapp.base.BaseSessionFragment
import com.example.userapp.databinding.FragmentSignupAgencyBinding
import com.example.userapp.utils.setupKeyboardHide

//TODO Agency : 얘 손봐야함.
class SignUpAgencyFragment : BaseSessionFragment<FragmentSignupAgencyBinding, SignUpViewModel>() {
    override lateinit var viewbinding: FragmentSignupAgencyBinding
    override val viewmodel: SignUpViewModel by navGraphViewModels(R.id.signUpGraph)

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentSignupAgencyBinding.inflate(inflater, container, false)
        return viewbinding.root
    }


    private val args: SignUpAgencyFragmentArgs by navArgs()
    private lateinit var searchListAdapter: SearchAgencyListAdapter
    private var query : String = ""
    private var nextBtnAvailable : Boolean = false

    private fun clearVariables(){
        query = ""
        if (viewmodel.cameFromPermission) viewmodel.clearAgencyResult(false)
        //if (args.emptyStateAgencyFrag) viewmodel.clearAgencyResult(false)
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        setupKeyboardHide(viewbinding.fragmentRootLayout, activity)
        setRecyclerView()
        setSearchEditTextView()
        getCheckedInfo()
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.run {
            agencyDataList.observe(viewLifecycleOwner){
                if (it.isEmpty()){ showEmptyView() }
                else {
                    searchListAdapter.submitList(it)
                    showRecyclerView() }
            }
            invalidSearchResultEventLiveData.observe(viewLifecycleOwner) { setSearchResultErrorMessage(it) }
            validSearchResultEventLiveData.observe(viewLifecycleOwner){ setSearchResultEmptyMessage() }
            checkAgencyClickedState.observe(viewLifecycleOwner){
                if (selectedAgency == null) {
                    clickedAgencyBtn = false
                    makeNextButtonNotAvailable()
                    viewmodel.loadSearchAgencyResult(null, true)
                }
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
            override fun onItemClick(v: View, position: Int) {
                viewmodel.selectedAgency = if (searchListAdapter.currentList[position].clicked){
                    searchListAdapter.currentList[position]
                } else null
                Log.e("checking", "${searchListAdapter.currentList[position]}")
                Log.e("checking", "${viewmodel.selectedAgency}")
                viewmodel.observeAgencyBtnState()
            }
        })
        viewbinding.signupSearchResultRv.adapter = searchListAdapter
        viewbinding.signupSearchResultRv.setHasFixedSize(true)
    }

    private fun setSearchEditTextView(){
        viewbinding.run {
             // SearchView 입력 글자색과 힌트 색상 변경하기
            signupSearchEdit.setIconifiedByDefault(false)
            signupSearchEdit.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)?.let{ editText ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    editText.setHintTextColor(resources.getColor(R.color.black_60, null))
                    editText.setTextColor(resources.getColor(R.color.black, null))
                } else {
                    editText.setHintTextColor(resources.getColor(R.color.black_60))
                    editText.setTextColor(resources.getColor(R.color.black))
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