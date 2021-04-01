        package com.example.gongsanggongsang

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.gongsanggongsang.Data.UserDataClass
import com.example.gongsanggongsang.Data.UserDataViewmodel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_sign_up.*


class SignupFragment : Fragment() {

    val viewModel: UserDataViewmodel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_sign_up, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signupButton.setOnClickListener {
            val id = signup_iddata.text.toString().trim()
            val pwd = signup_pwddata.text.toString().trim()
            val pwdcheck = signup_pwdcheckdata.text.toString().trim()
            val name = signup_namedata.text.toString().trim()
            val nickname = signup_nicknamedata.text.toString().trim()
            val birthday = signup_birthdata.text.toString().trim()


            if (id==null || pwd==null || pwdcheck==null || name ==null || nickname == null ||birthday ==null){ //하나라도 null인 값이 있으면
                Toast.makeText(context, "정보를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
            else {
                if (pwd!=pwdcheck) Toast.makeText(context, "재입력한 비밀번호가 일치하지 않습니다. 확인해주세요.", Toast.LENGTH_SHORT).show()
                else {
                    //이미 있는 아이디나 닉네임일 경우 중복체킹해야함.(일단 다 제낌)
                    // -> 나중에 고려해야되는 거 ID 중복체킹.이전에 이미 등록을 한경우?(아몰랑)

                    //다 Ok 되었다. -> 그러면 기다려 주세요 화면으로 가야함 (수정 필요)
                    viewModel.insert(UserDataClass(id, pwd, name, nickname, birthday, false))
                    findNavController().navigate(R.id.action_signupFragment_to_baseFragment)


                }

            }


        }
    }


}








