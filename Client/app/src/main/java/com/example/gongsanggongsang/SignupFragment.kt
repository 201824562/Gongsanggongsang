        package com.example.gongsanggongsang

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.gongsanggongsang.data.UserDataClass
import com.example.gongsanggongsang.data.UserDataViewmodel
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SignupFragment : Fragment() {

    val viewModel: UserDataViewmodel by viewModels()

    lateinit var id : String
    lateinit var pwd : String
    lateinit var pwdcheck : String
    lateinit var name : String
    lateinit var nickname : String
    lateinit var birthday : String
    lateinit var smsinfo : String


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


        signup_btn.setOnClickListener {
            id = edit_text_id.text.toString()
            pwd = edit_text_pwd.text.toString()
            pwdcheck = edit_text_pwd2.text.toString()
            name = edit_text_name.text.toString()
            nickname = edit_text_nickname.text.toString()
            birthday = edit_text_birthinfo.text.toString()
            smsinfo = edit_text_smsinfo.text.toString()

            Log.d("SignupFragment", "dfdfdf")
            Log.d("SignupFragment", "${id}")
            Log.d("Tag", "${id}")
            Log.d("Tag", "${pwd}")

            //네트워크 연결 설정 처리 필요 & 아이디*닉네임 중복여부(서버 뒤져서 확인 )-> 이미 있는 유저 경우 덮어쓰기.
            if (id.isNullOrBlank() || pwd.isNullOrBlank() || pwdcheck.isNullOrBlank() || name.isNullOrBlank() || nickname.isNullOrBlank() || birthday.isNullOrBlank()) { //하나라도 null인 값이 있으면
                Toast.makeText(context, "정보를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                if (pwd != pwdcheck) Toast.makeText(context, "재입력한 비밀번호가 일치하지 않습니다. 확인해주세요.", Toast.LENGTH_SHORT).show()
                else if (smsinfo.length != 11) Toast.makeText(context, "전화번호를 제대로 입력해주세요.", Toast.LENGTH_SHORT).show()
                else if (!smsinfo.contains("010")) Toast.makeText(context, "전화번호를 제대로 입력해주세요.", Toast.LENGTH_SHORT).show()
                //else if (id.length < 6) Toast.makeText(context, "입력한 아이디가 너무 짧습니다. 6자리 넘겨주세요.", Toast.LENGTH_SHORT).show()
                else {
                    //네트워크 연결 설정 처리 필요.
                    //닉네임 중복 여부 서버 뒤져서 확인 필요.
                    //이미 등록한 유저의 경우 덮어쓰게.
                    //입력정보 형식 지정해 주기. (Ex. 전화번호 입력 형식.)
                    //이미 있는 아이디나 닉네임일 경우 중복체킹해야함.(일단 다 제낌)
                    // -> 나중에 고려해야되는 거 ID 중복체킹.이전에 이미 등록을 한경우?(아몰랑)

                    //다 Ok 되었다. -> 그러면 기다려 주세요 화면으로 가야함 (수정 필요)
                    viewModel.insert(UserDataClass(id, pwd, name, nickname, birthday, smsinfo, false))

                    val dialog = Signup_wait_Dialog(it.context)

                    CoroutineScope(Dispatchers.Main).launch {
                        dialog.show()
                        delay(2000)
                        dialog.dismiss()
                        findNavController().navigate(R.id.action_signupFragment_to_loginFragment)

                    }
                }


            }
        }
    }
}











