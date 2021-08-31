package com.example.userapp.base

import android.app.Application
import androidx.lifecycle.*
import com.example.userapp.data.AppDatabase
import com.example.userapp.data.dto.UserModel
import com.example.userapp.data.repository.UserRepository
import com.example.userapp.service.sendFireStoreNotification
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

    fun showSnackbar(str: String){
        snackbarMessageString.postValue(str)
    }

    fun observeSnackbarMessageString(lifecycleOwner: LifecycleOwner, ob: (String) -> Unit){
        snackbarMessageString.observe(lifecycleOwner, ob)
    }



    val userRepository: UserRepository = UserRepository.getInstance(AppDatabase.getDatabase(application, viewModelScope))

    private var _agencyInfo: String? = null
    val agencyInfo: String get() = _agencyInfo!!
    private var _fcmToken: String? = null
    private val fcmToken: String get() = _fcmToken!!
    private var _authToken: String? = null
    val authToken: String get() = _authToken!!
    val isTokenAvailable: Boolean get() = _authToken != null

    init {
        _agencyInfo = userRepository.getAgencyInfo(application)
        _authToken = userRepository.getUserToken(application)
        _fcmToken = userRepository.getFCMToken(application)
    }

    private val _onSuccessGettingUserInfo = SingleLiveEvent<UserModel>()
    val onSuccessGettingUserInfo : LiveData<UserModel> get() = _onSuccessGettingUserInfo
    private val _onSuccessGettingNullUserInfo = SingleLiveEvent<UserModel>()
    val onSuccessGettingNullUserInfo : LiveData<UserModel> get() = _onSuccessGettingNullUserInfo

    fun getUserInfo()  {
        apiCall(userRepository.getUserInfo(), {
            _onSuccessGettingUserInfo.postValue(it) },
            { _onSuccessGettingNullUserInfo.call() })
    }

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



    private val _sessionInvalidEvent: SingleLiveEvent<Any> = SingleLiveEvent()
    val sessionInvalidEvent: LiveData<Any> get() = _sessionInvalidEvent

    fun callSessionInvalidEvent() {
        _sessionInvalidEvent.call()
    }

    fun registerNotificationToFireStore(title : String, content : String){
        sendFireStoreNotification(title, content, fcmToken )
    }



}