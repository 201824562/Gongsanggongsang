package com.example.userapp.ui.base

import android.app.Application
import androidx.lifecycle.*
import com.example.userapp.data.AppDatabase
import com.example.userapp.data.dto.UserModel
import com.example.userapp.data.model.AlarmItem
import com.example.userapp.data.repository.AlarmRepository
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
    val fcmToken: String get() = _fcmToken!!
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


    data class AdminNotifyInfo(val id : String, val fcmTokenList : List<String>)

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
        onComplete: Action = Action{},
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


    private val alarmRepository: AlarmRepository = AlarmRepository.getInstance(AppDatabase.getDatabase(application, viewModelScope))

    private val _onSuccessGettingAdminToken = SingleLiveEvent<List<String>>()
    val onSuccessGettingAdminToken : LiveData<List<String>> get() = _onSuccessGettingAdminToken
    private val _onSuccessGettingAdminNotifyInfo = SingleLiveEvent<List<AdminNotifyInfo>>()
    val onSuccessGettingAdminNotifyInfo : LiveData<List<AdminNotifyInfo>> get() = _onSuccessGettingAdminNotifyInfo
    private val _onSuccessRegisterAlarmData = SingleLiveEvent<AlarmItem>()
    val onSuccessRegisterAlarmData : LiveData<AlarmItem> get() = _onSuccessRegisterAlarmData

    fun getAdminTokens(agency : String) {
        apiCall(userRepository.getAdminTokens(agency), { _onSuccessGettingAdminToken.postValue(it) })
    }
    fun getAdminNotifyInfo(agency: String) {
        apiCall(userRepository.getAdminNotifyInfo(agency) , { _onSuccessGettingAdminNotifyInfo.postValue(it)})
    }
    fun registerAlarmData(toOther : String, documentId : String, alarmData : AlarmItem, agency: String = "")  {
        if (agency != "") apiCall(alarmRepository.registerAlarmData(agency, toOther, documentId, alarmData), { _onSuccessRegisterAlarmData.call() })
        else apiCall(alarmRepository.registerAlarmData(agencyInfo, toOther, documentId, alarmData), { _onSuccessRegisterAlarmData.call() })
    }
    fun registerNotificationToFireStore(title : String, content : String, toToken : String){
        sendFireStoreNotification(title, content, toToken)
    }



}