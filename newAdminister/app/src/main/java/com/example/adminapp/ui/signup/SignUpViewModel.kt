package com.example.adminapp.ui.signup

import com.example.adminapp.base.BaseViewModel
import com.example.adminapp.data.model.SignUpInfo
import com.example.adminapp.data.repository.UserRepository

class SignUpViewModel : BaseViewModel() {

    val repository: UserRepository = UserRepository.getInstance()
    //var allUserInfo: LiveData<List<SignUpInfo>> = repository.allUsers

    fun insert(it: SignUpInfo) {
        repository.insert(it)
    }

}