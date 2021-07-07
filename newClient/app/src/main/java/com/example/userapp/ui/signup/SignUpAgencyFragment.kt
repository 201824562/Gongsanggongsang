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
    
    private var searchListAdapter: SearchAgencyListAdapter? = null
    private var selectedAgency : Agency?  = null

    override fun initViewStart(savedInstanceState: Bundle?) {
        setupKeyboardHide(viewbinding.fragmentRootLayout, activity)
        setRecyclerView()
        setSearchEditTextView()
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.run {
            signupNextbtn.setOnClickListener {
                findNavController().navigate(R.id.action_signUpAgencyFragment_to_signUpFirstFragment)
            }
        }
    }

    private fun setRecyclerView() {
        if(searchListAdapter == null) {
            searchListAdapter = SearchAgencyListAdapter(object : SearchAgencyListAdapter.OnItemClickListener {
                override fun onItemClick(v: View, position: Int) {
                    // TODO : 클릭시 함수 만들기.
                    selectedAgency = searchListAdapter?.currentList?.get(position)
                }
            })
        }
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
        //쿼리 결과.
    }

}