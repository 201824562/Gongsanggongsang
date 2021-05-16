package com.example.adminapp.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.adminapp.R
import com.example.adminapp.base.BaseFragment
import com.example.adminapp.data.model.SignUpInfo
import com.example.adminapp.databinding.FragmentSignupBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SignUpFragment : BaseFragment<FragmentSignupBinding, SignUpViewModel>(){
    override lateinit var viewbinding: FragmentSignupBinding
    override val viewmodel: SignUpViewModel by viewModels()

    lateinit var userId : String
    lateinit var userPwd : String
    lateinit var userPwdcheck : String
    lateinit var userName : String
    lateinit var userNickname : String
    lateinit var userBirthday : String
    lateinit var userSmsinfo : String


    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentSignupBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {

        viewbinding.run{
            signupBtn.setOnClickListener {
                userId = editTextId.text.toString()
                userPwd = editTextPwd.text.toString()
                userPwdcheck = editTextPwd2.text.toString()
                userName = editTextName.text.toString()
                userNickname = editTextNickname.text.toString()
                userBirthday = editTextBirthinfo.text.toString()
                userSmsinfo = editTextSmsinfo.text.toString()

                //네트워크 연결 설정 처리 필요 & 아이디*닉네임 중복여부(서버 뒤져서 확인 )-> 이미 있는 유저 경우 덮어쓰기.
                if (userId.isNullOrBlank() || userPwd.isNullOrBlank() || userPwdcheck.isNullOrBlank() || userName.isNullOrBlank() || userNickname.isNullOrBlank() || userBirthday.isNullOrBlank()) { //하나라도 null인 값이 있으면
                    Toast.makeText(context, "정보를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
                } else {
                    if (userPwd != userPwdcheck) Toast.makeText(context, "재입력한 비밀번호가 일치하지 않습니다. 확인해주세요.", Toast.LENGTH_SHORT).show()
                    else if (userSmsinfo.length != 11) Toast.makeText(context, "전화번호를 제대로 입력해주세요.", Toast.LENGTH_SHORT).show()
                    else if (!userSmsinfo.contains("010")) Toast.makeText(context, "전화번호를 제대로 입력해주세요.", Toast.LENGTH_SHORT).show()
                    //else if (id.length < 6) Toast.makeText(context, "입력한 아이디가 너무 짧습니다. 6자리 넘겨주세요.", Toast.LENGTH_SHORT).show()
                    else {
                        //네트워크 연결 설정 처리 필요.
                        //닉네임 중복 여부 서버 뒤져서 확인 필요.
                        //이미 등록한 유저의 경우 덮어쓰게.
                        //입력정보 형식 지정해 주기. (Ex. 전화번호 입력 형식.)
                        //이미 있는 아이디나 닉네임일 경우 중복체킹해야함.(일단 다 제낌)
                        // -> 나중에 고려해야되는 거 ID 중복체킹.이전에 이미 등록을 한경우?(아몰랑)

                        //다 Ok 되었다. -> 그러면 기다려 주세요 화면으로 가야함 (수정 필요)
                        viewmodel.insert(SignUpInfo(userId, userPwd, userName, userNickname, userBirthday, userSmsinfo, false))

                        val dialog = SignUpWaitDialog(it.context)

                        CoroutineScope(Dispatchers.Main).launch {
                            dialog.show()
                            delay(2000)
                            dialog.dismiss()
                            findNavController().navigate(R.id.action_signUpFragment_pop) }
                    } } }


        }
    }


}