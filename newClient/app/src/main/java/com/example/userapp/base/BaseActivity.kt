package com.example.userapp.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseActivity<VB : ViewBinding, VM : BaseViewModel>: AppCompatActivity() {

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
    abstract fun initViewStart(savedInstanceState: Bundle?)     //첫번째, 레이아웃 초기 설정- 뷰&액티비티 (ex.리사이클러뷰, 툴바, 드로어뷰)
    abstract fun initDataBinding(savedInstanceState: Bundle?)   //두번째, 데이터바인딩& RxJava 설정 (ex.RxJava observe, Databinding observe)
    abstract fun initViewFinal(savedInstanceState: Bundle?)     //세번째, 마무리 커스텀 (ex. 클릭리스너 이벤트)

    abstract fun initToolbar()



    fun snackbarObserving() {
        viewmodel.observeSnackbarMessageString(this) { message ->
            showSnackbar( message)
        } }

    @Throws(IllegalArgumentException::class)
    fun showSnackbar(message: String) {
            Snackbar.make(viewbinding.root.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show() }

    //네트워크 콜처리 & 서비스 관련 추가해주기.

}