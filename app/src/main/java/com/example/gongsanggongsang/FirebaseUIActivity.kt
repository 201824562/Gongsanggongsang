package com.example.gongsanggongsang

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth

//위에 액션바가 왜 안없어질까 -> noactionbar 적용해주었는ㄷ데ㅜ..
class FirebaseUIActivity : AppCompatActivity() {
    private val RC_SIGN_IN = 9001
    private var mAuth: FirebaseAuth? = null

    val providers = arrayListOf(    //인증종류(이메일,SMS,구글연동)
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_firebaseui) //처음으로 생성해줌.(xml)

        mAuth = FirebaseAuth.getInstance()

        //이미 로그인 되어 있는 경우와 아닌 경우해서 나눠주기.(밑 == 아닌 경우에 넣기)

    // Create and launch sign-in intent
        startActivityForResult( //새 액티비티를 열어줌.
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.Theme_AppCompat_Light_NoActionBar) // Set theme
                        .build(),
                RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }

    }
}