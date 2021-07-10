package com.example.userapp.ui.signup

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.userapp.R
import com.example.userapp.base.BaseFragment
import com.example.userapp.data.model.Agency
import com.example.userapp.databinding.FragmentSignupAgencyBinding
import com.example.userapp.utils.setupKeyboardHide

class SignUpAgencyFragment : BaseFragment<FragmentSignupAgencyBinding, SignUpViewModel>() {
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

    override fun initViewStart(savedInstanceState: Bundle?) {
        setupKeyboardHide(viewbinding.fragmentRootLayout, activity)
        clearVariables()
        setRecyclerView()
        setSearchEditTextView()
        getCheckedInfo()
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.run {
            agencyDataList.observe(viewLifecycleOwner){
                if (it.isEmpty()){ showEmptyView() }
                else {
                    showRecyclerView()
                    searchListAdapter.submitList(it) }
            }
            invalidSearchResultEventLiveData.observe(viewLifecycleOwner) { setSearchResultErrorMessage(it) }
            validSearchResultEventLiveData.observe(viewLifecycleOwner){ setSearchResultEmptyMessage() }
            checkAgencyClickedState.observe(viewLifecycleOwner){
                if (selectedAgency == null) {
                    clickedAgencyBtn = false
                    makeNextButtonNotAvailable()
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
                viewmodel.clearAgencyResult()
            }else{
                submitQuery(query.lines().first().trim()) }
        }
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.run {
            signupNextbtn.setOnClickListener {
                if (nextBtnAvailable) findNavController().navigate(R.id.action_signUpAgencyFragment_to_signUpFirstFragment)
            }
        }
    }

    private fun clearVariables(){ query = "" }

    private fun setRecyclerView() {
        searchListAdapter = SearchAgencyListAdapter(object : SearchAgencyListAdapter.OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                viewmodel.selectedAgency = if (searchListAdapter.currentList[position].clicked){
                    searchListAdapter.currentList[position]
                } else null
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
                    if(query.isNullOrEmpty()){
                        showSnackbar("검색어를 입력해주세요.")
                    }else{
                        signupSearchEdit.clearFocus()
                        submitQuery(query.lines().first().trim())
                    }
                    return true
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    return true }
            })
        }
    }

    private fun submitQuery(query:String){
        //TODO : 쿼리하는 함수 만들기. (영우오빠한테 물어보기?)
        viewmodel.loadSearchAgencyResult(query)
    }

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