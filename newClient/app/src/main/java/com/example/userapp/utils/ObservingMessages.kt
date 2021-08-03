package com.example.userapp.utils

import androidx.lifecycle.LifecycleOwner


class SnackbarMessageString: SingleLiveEvent<String>(){
    fun observe(owner: LifecycleOwner, observer: (String) -> Unit){
        super.observe(owner, { it?.run { observer(it) } })
    }
}
