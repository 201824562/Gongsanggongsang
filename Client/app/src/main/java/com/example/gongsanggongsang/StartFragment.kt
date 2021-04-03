package com.example.gongsanggongsang

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_start.*

class StartFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_start, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        manager_btn.setOnClickListener {
            //일단 아이디, 비번으로 입력해서 로그인 확인 후 화면 진입하는 거 빼고 구현하기.
            findNavController().navigate(R.id.action_startFragment_to_testAdministerFragment)
        }
        user_btn.setOnClickListener {
            findNavController().navigate(R.id.action_startFragment_to_loginFragment)
        }
    }
}
