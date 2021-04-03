package com.example.gongsanggongsang

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_sms.*
import java.util.concurrent.TimeUnit

class SmsFragment : Fragment(), PhoneCallbacks.PhoneCallbacksListener {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_sms, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        get_sms_textbtn.setOnClickListener {
            val phoneCallbacks  = PhoneCallbacks(this)
            val phonenumber = edit_text_sms.text
            if (phonenumber == null){

            }
            //잘못된 형식 체크하기.
            //else if (phonenumber)
            else {
                val options = PhoneAuthOptions.newBuilder()
                        .setPhoneNumber("+82" + edit_text_sms.text.toString())       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(activity)                 // Activity (for callback binding)
                        .setCallbacks(phoneCallbacks)          // OnVerificationStateChangedCallbacks
                        .build()
                PhoneAuthProvider.verifyPhoneNumber(options)
            }
        }
        //재전송 버튼 생성하기.

        send_smsButton.setOnClickListener {

        }
    }

    override fun onVerificationCompleted(credential: PhoneAuthCredential?) {
        TODO("Not yet implemented")
    }

    override fun onVerificationFailed(exception: FirebaseException?) {
        TODO("Not yet implemented")

    }

    override fun onCodeSent(verificationId: String?, token: PhoneAuthProvider.ForceResendingToken?) {
        TODO("Not yet implemented")
    }
}

class PhoneCallbacks(private val listener : PhoneCallbacksListener) : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

    interface PhoneCallbacksListener {
        fun onVerificationCompleted(credential: PhoneAuthCredential?)
        fun onVerificationFailed(exception: FirebaseException?)
        fun onCodeSent(
                verificationId: String?,
                token: PhoneAuthProvider.ForceResendingToken?
        )
    }

    override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential?) {
        listener.onVerificationCompleted(phoneAuthCredential)
    }

    override fun onVerificationFailed(exception: FirebaseException?) {
        listener.onVerificationFailed(exception)
    }

    override fun onCodeSent(verificationId: String?, token: PhoneAuthProvider.ForceResendingToken?) {
        listener.onCodeSent(verificationId,token)
    }
}