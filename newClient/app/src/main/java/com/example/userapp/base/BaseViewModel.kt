package com.example.userapp.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.userapp.utils.*
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import java.util.concurrent.TimeUnit

abstract class BaseViewModel  : ViewModel(){

    protected val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    private fun addDisposable(disposable: Disposable){
        compositeDisposable.add(disposable)
    }


    private val snackbarMessageString = SnackbarMessageString()

    private val _apiCallErrorEvent:SingleLiveEvent<String> = SingleLiveEvent()
    val apiCallErrorEvent: LiveData<String> get() = _apiCallErrorEvent

    open fun <T> apiCall(single: Single<T>, onSuccess: Consumer<in T>,
                         onError: Consumer<in Throwable> = Consumer {
                             //FirebaseAnalytics.getInstance()
                             _apiCallErrorEvent.postValue(it.message)
                             showSnackbar("오류가 발생했습니다. ${it.message}")
                         },
                         indicator : Boolean = false, timeout: Long = 10){
        addDisposable(single.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .timeout(timeout, TimeUnit.SECONDS)
/*            .doOnSubscribe{ if(indicator) startLoadingIndicator() }
            .doAfterTerminate { stopLoadingIndicator() }*/
            .subscribe(onSuccess, onError))
    }

    fun apiCall(completable: Completable,
                onComplete: Action = Action{
                    // default do nothing
                },
                onError: Consumer<Throwable> = Consumer {
                    _apiCallErrorEvent.postValue(it.message)
                    showSnackbar("오류가 발생했습니다. ${it.message}")
                },
                indicator: Boolean = false,
                timeout: Long = 5){
        addDisposable(completable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .timeout(timeout, TimeUnit.SECONDS)
            .subscribe(onComplete, onError))
    }

    fun observeSnackbarMessageString(lifecycleOwner: LifecycleOwner, ob: (String) -> Unit){
        snackbarMessageString.observe(lifecycleOwner, ob)
    }

    fun showSnackbar(str: String){
        snackbarMessageString.postValue(str)
    }

}