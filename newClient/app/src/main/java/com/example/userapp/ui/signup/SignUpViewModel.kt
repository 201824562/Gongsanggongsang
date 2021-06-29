package com.example.userapp.ui.signup

import androidx.lifecycle.LiveData
import com.example.userapp.base.BaseViewModel
import com.example.userapp.data.model.SignUpInfo
import com.example.userapp.data.repository.UserRepository

class SignUpViewModel : BaseViewModel() {

    val repository: UserRepository = UserRepository.getInstance()
    //var allUserInfo: LiveData<List<SignUpInfo>> = repository.allUsers

    fun insert(it: SignUpInfo) {
        repository.insert(it)
    }

}