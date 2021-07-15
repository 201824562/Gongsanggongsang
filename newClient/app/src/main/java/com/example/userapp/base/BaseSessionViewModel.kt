package com.example.userapp.base

import android.app.Application
import androidx.lifecycle.*
import com.example.userapp.data.AppDatabase
import com.example.userapp.data.repository.UserRepository
import com.example.userapp.utils.SingleLiveEvent
import com.example.userapp.utils.SnackbarMessageString
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


abstract class BaseSessionViewModel(application: Application)  : AndroidViewModel(application) {

    protected val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    private fun addDisposable(disposable: Disposable){
        compositeDisposable.add(disposable)
    }

    private val snackbarMessageString = SnackbarMessageString()
/*    private val toastMessage = ToastMessage()
    private val toastMessageString = ToastMessageString()*/

    fun showSnackbar(str: String){
        snackbarMessageString.postValue(str)
    }

    fun observeSnackbarMessageString(lifecycleOwner: LifecycleOwner, ob: (String) -> Unit){
        snackbarMessageString.observe(lifecycleOwner, ob)
    }
/*

    fun observeToastMessage(lifecycleOwner: LifecycleOwner, ob: (Int) -> Unit){
        toastMessage.observe(lifecycleOwner, ob)
    }

    fun observeToastMessageStr(lifecycleOwner: LifecycleOwner, ob: (String) -> Unit){
        toastMessageString.observe(lifecycleOwner, ob)
    }
*/


    private val _apiCallErrorEvent:SingleLiveEvent<String> = SingleLiveEvent()
    val apiCallErrorEvent: LiveData<String> get() = _apiCallErrorEvent

    open fun <T> apiCall(single: Single<T>, onSuccess: Consumer<in T>,
                         onError: Consumer<in Throwable> = Consumer {
                             _apiCallErrorEvent.postValue(it.message)
                             showSnackbar("오류가 발생했습니다. ${it.message}")
                         }, timeout: Long = 10){
        addDisposable(single.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .timeout(timeout, TimeUnit.SECONDS)
            .subscribe(onSuccess, onError))
    }

    open fun apiCall(
        completable: Completable,
        onComplete: Action = Action{
                    // default do nothing
                },
        onError: Consumer<Throwable> = Consumer {
                    _apiCallErrorEvent.postValue(it.message)
                    showSnackbar("오류가 발생했습니다. ${it.message}")
                },
        timeout: Long = 5){
        addDisposable(completable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .timeout(timeout, TimeUnit.SECONDS)
            .subscribe(onComplete, onError))
    }

    //--------------------------------------------------------------------------------------------------------------------------

    val userRepository: UserRepository = UserRepository.getInstance(AppDatabase.getDatabase(application, viewModelScope))

    private var _authToken: String? = null
    val authToken: String get() = _authToken!!
    val isTokenAvailable: Boolean get() = _authToken != null

    init { _authToken = userRepository.getUserToken(application) }


    private val _sessionInvalidEvent: SingleLiveEvent<Any> = SingleLiveEvent()
    val sessionInvalidEvent: LiveData<Any> get() = _sessionInvalidEvent

    //이거 필요한지?
    fun callSessionInvalidEvent() {
        _sessionInvalidEvent.call()
    }



}