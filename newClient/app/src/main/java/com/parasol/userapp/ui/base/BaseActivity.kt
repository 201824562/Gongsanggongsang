package com.parasol.userapp.ui.base

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.parasol.userapp.MainActivity
import com.parasol.userapp.R
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseActivity<VB : ViewBinding, VM : BaseActivityViewModel>: AppCompatActivity() {

    abstract val viewbinding : VB
    abstract val viewmodel : VM
    abstract val layoutResourceId: Int

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

        initToolbar()
    }

    abstract fun initViewbinding()
    abstract fun initViewStart(savedInstanceState: Bundle?)
    abstract fun initDataBinding(savedInstanceState: Bundle?)
    abstract fun initViewFinal(savedInstanceState: Bundle?)

    abstract fun initToolbar()



    @Throws(IllegalArgumentException::class)
    fun showSnackbar(message: String) {
            Snackbar.make(viewbinding.root.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show() }



}

