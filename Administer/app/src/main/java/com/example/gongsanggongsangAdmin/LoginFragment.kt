package com.example.gongsanggongsangAdmin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.edit_text_pwd
import kotlinx.android.synthetic.main.fragment_login.signup_btn

class LoginFragment : Fragment() {

    lateinit var id : String
    lateinit var pwd : String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_login, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signup_btn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        login_btn.setOnClickListener {
            id = edit_text_id.text.toString()
            pwd = edit_text_pwd.text.toString()

            if (id.isBlank() || pwd.isBlank() ){
                Toast.makeText(context, "정보를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
            else {
                findNavController().navigate(R.id.action_loginFragment_to_baseFragment)
            }
        }

    }
}