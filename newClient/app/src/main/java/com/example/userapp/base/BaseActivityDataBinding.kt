
package com.example.userapp.base

/*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseActivityDataBinding<DB : ViewDataBinding, VM : ViewModel>: AppCompatActivity() {

    abstract val viewbinding : DB
    abstract val viewmodel : VM

    private val compositeDisposable = CompositeDisposable()

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    private fun addDisposable(disposable: Disposable){
        compositeDisposable.add(disposable)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewbinding()
        initViewStart(savedInstanceState)
        initDataBinding(savedInstanceState)
        initViewFinal(savedInstanceState)
    }

    abstract fun initViewbinding()
    abstract fun initViewStart(savedInstanceState: Bundle?)
    abstract fun initDataBinding(savedInstanceState: Bundle?)
    abstract fun initViewFinal(savedInstanceState: Bundle?)

}*/
