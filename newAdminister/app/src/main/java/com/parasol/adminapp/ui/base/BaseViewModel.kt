package com.parasol.adminapp.ui.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.parasol.adminapp.utils.SnackbarMessageString
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel  : ViewModel(){

    private val snackbarMessageString = SnackbarMessageString()

    protected val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun observeSnackbarMessageString(lifecycleOwner: LifecycleOwner, ob: (String) -> Unit){
        snackbarMessageString.observe(lifecycleOwner, ob)
    }

    fun showSnackbar(str: String){
        snackbarMessageString.postValue(str)
    }

}