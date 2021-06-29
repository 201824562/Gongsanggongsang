package com.example.adminapp.utils

import androidx.lifecycle.LifecycleOwner


class SnackbarMessage: SingleLiveEvent<Int>(){
    fun observe(owner: LifecycleOwner, observer: (Int) -> Unit){
        super.observe(owner, { it?.run { observer(it) } })
    }
}

class SnackbarMessageString: SingleLiveEvent<String>(){
    fun observe(owner: LifecycleOwner, observer: (String) -> Unit){
        super.observe(owner, { it?.run { observer(it) } })
    }
}

class ToastMessage: SingleLiveEvent<Int>(){
    fun observe(owner: LifecycleOwner, observer: (Int) -> Unit){
        super.observe(owner, { it?.run{ observer(it) } })
    }
}

class ToastMessageString: SingleLiveEvent<String>(){
    fun observe(owner: LifecycleOwner, observer: (String) -> Unit){
        super.observe(owner, {  it?.run{ observer(it) }})
    }
}
