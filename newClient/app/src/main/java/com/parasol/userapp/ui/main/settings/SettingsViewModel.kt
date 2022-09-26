package com.parasol.userapp.ui.main.settings

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.parasol.userapp.ui.base.BaseSessionViewModel
import com.parasol.userapp.data.dto.UserModel
import com.parasol.userapp.data.entity.DayTimeSlot
import com.parasol.userapp.data.entity.ReservationTimeData
import com.parasol.userapp.data.model.*
import com.parasol.userapp.data.repository.CommunityDataRepository
import com.parasol.userapp.ui.main.reservation.CategoryResources
import com.parasol.userapp.utils.RegularExpressionUtils
import com.parasol.userapp.utils.SingleLiveEvent
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

class SettingsViewModel(application: Application) : BaseSessionViewModel(application) {
    private val communityDataRepository : CommunityDataRepository = CommunityDataRepository.getInstance()
    private val _onSuccessDeleteUserInfoFromServer = SingleLiveEvent<Any>()
    val onSuccessDeleteUserInfoFromServer : LiveData<Any> get() = _onSuccessDeleteUserInfoFromServer
    private val _onSuccessDeleteFcmTokenFromServer = SingleLiveEvent<Any>()
    val onSuccessDeleteFcmTokenFromServer : LiveData<Any> get() = _onSuccessDeleteFcmTokenFromServer
    private val _onSuccessDeleteUserInfoFromApp = SingleLiveEvent<Any>()
    val onSuccessDeleteUserInfoFromApp : LiveData<Any> get() = _onSuccessDeleteUserInfoFromApp
    private val _invalidUserIdEventLiveData = SingleLiveEvent<String>()
    val invalidUserIdEventLiveData: LiveData<String> get() = _invalidUserIdEventLiveData
    private val _validUserIdEventLiveData = SingleLiveEvent<Any>()
    val validUserIdEventLiveData: LiveData<Any> get() = _validUserIdEventLiveData
    private val _invalidUserPwdEventLiveData = SingleLiveEvent<String>()
    val invalidUserPwdEventLiveData: LiveData<String> get() = _invalidUserPwdEventLiveData
    private val _validUserPwdEventLiveData = SingleLiveEvent<Any>()
    val validUserPwdEventLiveData: LiveData<Any> get() = _validUserPwdEventLiveData
    private val _invalidUserPwd2EventLiveData = SingleLiveEvent<String>()
    val invalidUserPwd2EventLiveData: LiveData<String> get() = _invalidUserPwd2EventLiveData
    private val _validUserPwd2EventLiveData = SingleLiveEvent<Any>()
    val validUserPwd2EventLiveData: LiveData<Any> get() = _validUserPwd2EventLiveData
    private val _invalidUserNicknameEventLiveData = SingleLiveEvent<String>()
    val invalidUserNicknameEventLiveData: LiveData<String> get() = _invalidUserNicknameEventLiveData
    private val _validUserNicknameEventLiveData = SingleLiveEvent<Any>()
    val validUserNicknameEventLiveData: LiveData<Any> get() = _validUserNicknameEventLiveData
    private val _checkNicknameEventLiveData = SingleLiveEvent<Boolean>()
    val checkNicknameEventLiveData: LiveData<Boolean> get() = _checkNicknameEventLiveData
    private val _checkIdEventLiveData = SingleLiveEvent<Boolean>()
    val checkIdEventLiveData: LiveData<Boolean> get() = _checkIdEventLiveData
    private val _sendChangeInfoEventLiveData = SingleLiveEvent<Any>()
    val sendChangeInfoEventLiveData: LiveData<Any> get() = _sendChangeInfoEventLiveData
    private val _onSuccessChangeInfoEvent = SingleLiveEvent<Any>()
    val onSuccessChangeInfoEvent : LiveData<Any> get() = _onSuccessChangeInfoEvent

    //
    val firestore = FirebaseFirestore.getInstance()
    val FacilityDayInfoLiveData = MutableLiveData<ReservationFacilityDayInfo>()
    private var FacilityDayInfoData = ReservationFacilityDayInfo(
        "default",
        0,
        0L,
        "default",
        mutableListOf()
    )
    val FacilitySelectedTimeSlotLiveData = MutableLiveData<ReservationFacilityDayInfo>()
    private var FacilitySelectedTimeSlotData = ReservationFacilityDayInfo(
        "default",
        0,
        0L,
        "default",
        mutableListOf()
    )
    val InitRefTimeLiveData = MutableLiveData<Calendar>()
    private val InitRefTimeData = Calendar.getInstance()
    val FacilityReserveLiveData = MutableLiveData<List<ReservationReserveFacility>>()
    private val FacilityReserveData = arrayListOf<ReservationReserveFacility>()

    var checkedId : String ?= null
    var checkedNickname : String ?= null
    fun clearCheckedId() {checkedId = null}
    fun clearCheckedNickname() {checkedNickname = null}

    fun deleteUserInfoFromServerDatabase() {
        apiCall(userRepository.withdrawalUserInfo(authToken),
            { _onSuccessDeleteUserInfoFromServer.value = true },
            { showSnackbar("회원탈퇴에 실패했습니다. 네트워크 상태를 체크해주세요.")})
    }
    fun deleteDeviceTokenFromServerDatabase(){
        apiCall(userRepository.deleteUsersDeviceToken(authToken, fcmToken),
            { _onSuccessDeleteFcmTokenFromServer.value = true },
            { showSnackbar("디바이스 로그아웃에 실패했습니다. 네트워크 상태를 체크해주세요.")})
    }
    fun deleteUserInfoFromAppDatabase() {
        val context = getApplication<Application>().applicationContext
        apiCall(userRepository.deleteUserInfo(authToken),
            {   userRepository.removeAgencyInfo(context)
                userRepository.removeFCMToken(context)
                userRepository.removeUserToken(context)
                _onSuccessDeleteUserInfoFromApp.value = true },
            {showSnackbar("로그아웃에 실패했습니다. 잠시후에 시도해주세요.")})
    }
    private fun checkForUserPwd(userPwd: String, userPwd2: String) : Boolean {
        return if (userPwd.isBlank() || userPwd.isEmpty() ) {
            _invalidUserPwdEventLiveData.postValue("비밀번호를 입력해주세요.")
            _validUserPwd2EventLiveData.call()
            false
        }else if (userPwd.length < 8 || userPwd.length > 22) {
            _invalidUserPwdEventLiveData.postValue("비밀번호는 8~22자로 입력해야 합니다.")
            _validUserPwd2EventLiveData.call()
            false
        }else if (!RegularExpressionUtils.validCheck(RegularExpressionUtils.Regex.PWD, userPwd)) {
            _invalidUserPwdEventLiveData.postValue("비밀번호는 영문 또는 숫자만 가능합니다.")
            _validUserPwd2EventLiveData.call()
            false
        }else if (userPwd2.isBlank() || userPwd2.isEmpty()){
            _invalidUserPwd2EventLiveData.postValue("비밀번호 확인을 입력해주세요.")
            _validUserPwdEventLiveData.call()
            false
        }else if (userPwd != userPwd2){
            _invalidUserPwd2EventLiveData.postValue("비밀번호 확인이 일치하지 않습니다.")
            _validUserPwdEventLiveData.call()
            false
        } else {
            _validUserPwdEventLiveData.call()
            _validUserPwd2EventLiveData.call()
            true
        }
    }

    fun checkNicknameFromServer(nickname: String) {
        apiCall(Single.zip(userRepository.checkNicknameFromWaitingUser(nickname),userRepository.checkNicknameFromAllowedUser(nickname),
            BiFunction<Boolean, Boolean, Boolean> { waitingNicknameExist, allowedNicknameExist ->
                if (waitingNicknameExist) return@BiFunction false
                else if (allowedNicknameExist) return@BiFunction false
                return@BiFunction true
            }),
            {   if (it) checkedNickname = nickname
                _checkNicknameEventLiveData.postValue(it)
            })
    }

    fun checkForUserNickname(userNickname: String) : Boolean {
        return if (userNickname.isBlank() || userNickname.isEmpty()) {
            _invalidUserNicknameEventLiveData.postValue("닉네임을 입력해주세요.")
            false
        }else if (userNickname.length < 5) {
            _invalidUserNicknameEventLiveData.postValue("닉네임은 다섯글자 이상이어야 합니다.")
            false
        } else if (!RegularExpressionUtils.validCheck(RegularExpressionUtils.Regex.NICKNAME, userNickname)) {
            _invalidUserNicknameEventLiveData.postValue("닉네임은 10글자 이내의 한글만 입력가능합니다.")
            false
        } else {
            _validUserNicknameEventLiveData.call()
            true
        }
    }

    private fun checkForUserNicknameDuplicated(userNickname: String): Boolean {
        return if (userNickname != checkedNickname) {
            _invalidUserNicknameEventLiveData.postValue("닉네임 중복확인을 해주세요.")
            false
        }else true
    }


    fun checkIdFromServer(userId: String) {
        apiCall(
            Single.zip(userRepository.checkIdFromWaitingUser(userId), userRepository.checkIdFromAllowedUser(userId),
            BiFunction<Boolean, Boolean, Boolean> { waitingIdExist, allowedIdExist ->
                if (waitingIdExist) return@BiFunction false
                else if (allowedIdExist) return@BiFunction false
                return@BiFunction true
            }),
            {   if (it) checkedId = userId
                _checkIdEventLiveData.postValue(it)
            })
    }

    fun checkForUserId(userId: String) : Boolean {
        return if (userId.isBlank() || userId.isEmpty()) {
            _invalidUserIdEventLiveData.postValue("아이디를 입력해주세요.")
            false
        } else if (!RegularExpressionUtils.validCheck(RegularExpressionUtils.Regex.ID, userId)) {
            _invalidUserIdEventLiveData.postValue("아이디는 4~12자의 영문, 숫자만 가능합니다.")
            false
        } else {
            _validUserIdEventLiveData.call()
            true
        }
    }

    private fun checkForUserIdDuplicated(userId: String): Boolean {
        return if (userId!=checkedId) {
            _invalidUserIdEventLiveData.postValue("아이디 중복확인을 해주세요.")
            false
        }else true
    }

    fun checkForSendChangeInfo(type : ChangeInfoType, userId : String, userPwd : String, userPwd2 : String,userNickname : String) {
        when(type){
            ChangeInfoType.NICKNAME -> if(checkForUserNicknameDuplicated(userNickname)) _sendChangeInfoEventLiveData.call()
            ChangeInfoType.ID -> if(checkForUserIdDuplicated(userId)) _sendChangeInfoEventLiveData.call()
            ChangeInfoType.PWD -> if(checkForUserPwd(userPwd, userPwd2)) _sendChangeInfoEventLiveData.call()
        }
    }

    fun sendChangeInfoToServer(type : ChangeInfoType, userId : String, userPwd : String, userNickname : String){
        when(type){
            ChangeInfoType.NICKNAME -> apiCall(userRepository.changeUserInfoNickname(authToken, userNickname), { _onSuccessChangeInfoEvent.call() })
            ChangeInfoType.ID -> apiCall(userRepository.changeUserInfoId(authToken, userId), { _onSuccessChangeInfoEvent.call() })
            ChangeInfoType.PWD -> apiCall(userRepository.changeUserInfoPwd(authToken, userPwd), { _onSuccessChangeInfoEvent.call() })
        }
    }

    fun checkForSendChangePwd(userPwd : String, userPwd2 : String) {
        if(checkForUserPwd(userPwd, userPwd2)) _sendChangeInfoEventLiveData.call()
    }

    fun sendChangePwdToServer(userPwd : String){
        apiCall(userRepository.changeUserInfoPwd(authToken, userPwd), { _onSuccessChangeInfoEvent.call() })
    }


    //----------------------------------------------------------------------------------------------------------------
    fun add_select_time_slot(dayTimeSlot: DayTimeSlot, maxSlot: Long): Boolean {
        if (FacilitySelectedTimeSlotData.day_time_slot_list.isEmpty()) {
            dayTimeSlot.buttonSelected = true
            dayTimeSlot.user = authToken
            FacilitySelectedTimeSlotData.day_time_slot_list.add(dayTimeSlot)
            FacilitySelectedTimeSlotLiveData.value = FacilitySelectedTimeSlotData
            Log.e(
                "FacilityDayInfoData.day_time_slot_list333",
                FacilityDayInfoData.day_time_slot_list.toString()
            )
            return true
        } else {
            if (maxSlot > FacilitySelectedTimeSlotData.day_time_slot_list.size.toLong()) {
                for (timeslot in FacilitySelectedTimeSlotData.day_time_slot_list) {
                    if (timeslot.index - dayTimeSlot.index == 1L || timeslot.index - dayTimeSlot.index == -1L) {
                        dayTimeSlot.buttonSelected = true
                        dayTimeSlot.user = authToken
                        FacilitySelectedTimeSlotData.day_time_slot_list.add(dayTimeSlot)
                        FacilitySelectedTimeSlotData.day_time_slot_list.sortBy { it.index }
                        FacilitySelectedTimeSlotLiveData.value = FacilitySelectedTimeSlotData
                        Log.e(
                            "FacilityDayInfoData.day_time_slot_list44",
                            FacilityDayInfoData.day_time_slot_list.toString()
                        )
                        return true
                    }
                }
            }
        }
        Log.e(
            "FacilityDayInfoData.day_time_slot_list5555",
            FacilityDayInfoData.day_time_slot_list.toString()
        )

        return false
        //최대 갯수제한 추가해줘야함!!
    }

    fun delete_select_time_slot(dayTimeSlot: DayTimeSlot): Boolean {
        var cnt: Int = 0
        for (timeslot in FacilitySelectedTimeSlotData.day_time_slot_list) {
            if (timeslot.index - dayTimeSlot.index == 1L || timeslot.index - dayTimeSlot.index == -1L) {
                cnt++
            }
        }
        if (cnt < 2) {
            FacilitySelectedTimeSlotData.day_time_slot_list.remove(dayTimeSlot)
            //선택취소해도 기존 데이터베이스에 남아있는 현상
            FacilitySelectedTimeSlotData.day_time_slot_list.sortBy { it.index }
            Log.e(
                "FacilitySelectedTimeSlotData.day_time_slot_list",
                FacilitySelectedTimeSlotData.day_time_slot_list.toString()
            )
            Log.e(
                "FacilityDayInfoData.day_time_slot_list666",
                FacilityDayInfoData.day_time_slot_list.toString()
            )
            FacilitySelectedTimeSlotLiveData.value = FacilitySelectedTimeSlotData
            return true
        }
        Log.e(
            "FacilityDayInfoData.day_time_slot_list7777",
            FacilityDayInfoData.day_time_slot_list.toString()
        )

        return false

    }

    fun clear_select_time_slot() {
        FacilitySelectedTimeSlotData.day_time_slot_list.clear()
        FacilitySelectedTimeSlotLiveData.value = FacilitySelectedTimeSlotData
    }

    fun getDayTimeSlotListSize(): Long {
        return FacilitySelectedTimeSlotData.day_time_slot_list.size.toLong()
    }

    fun getReserveFacilityStartTime(): Calendar {
        var startCal: Calendar = Calendar.getInstance()
        startCal.set(Calendar.DAY_OF_WEEK, weekdayStringToInt(FacilitySelectedTimeSlotData.weekday))
        startCal.set(
            Calendar.HOUR_OF_DAY,
            Math.toIntExact(FacilitySelectedTimeSlotData.day_time_slot_list.first().data?.hour!!)
        )
        startCal.set(
            Calendar.MINUTE,
            Math.toIntExact(FacilitySelectedTimeSlotData.day_time_slot_list.first().data?.min!!)
        )

        return startCal
    }
    fun getReserveFacilityEndTime(): Calendar {
        var endCal: Calendar = Calendar.getInstance()
        endCal.set(Calendar.DAY_OF_WEEK, weekdayStringToInt(FacilitySelectedTimeSlotData.weekday))
        endCal.set(
            Calendar.HOUR_OF_DAY,
            Math.toIntExact(FacilitySelectedTimeSlotData.day_time_slot_list.last().data?.hour!!)
        )
        endCal.set(
            Calendar.MINUTE,
            Math.toIntExact(FacilitySelectedTimeSlotData.day_time_slot_list.last().data?.min!!)
        )
        endCal.add(Calendar.MINUTE, Math.toIntExact(FacilitySelectedTimeSlotData.interval_time))

        return endCal
    }

    fun weekdayStringToInt(weekdayString: String): Int {
        return when (weekdayString) {
            "monday" -> 2
            "tuesday" -> 3
            "wednesday" -> 4
            "thursday" -> 5
            "friday" -> 6
            "saturday" -> 7
            "sunday" -> 1
            else -> 1
        }
    }

    //파이어베이스에서 가져옴
    @RequiresApi(Build.VERSION_CODES.O)
    fun getinitRefTimeData(){
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS", Locale.KOREA)

        firestore.collection("한국장학재단_부산").document("community")
            .collection("OUT_NOW")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                for (document in value!!) {
                    val tmp = document.get("initRefTime") as String
                    Log.e("tmp",tmp)
                    InitRefTimeData.setTime(sdf.parse(tmp))

                }
                InitRefTimeLiveData.value = InitRefTimeData
            }

    }

    fun getFacilityTimeSlotData(
        document_name: String,
        weekday: String,
        icon: Int,
        interval_time: Long
    ) {
        //파이어베이스에서 해쉬맵스타일로 받아서 클래스형식으로 바꿔서 코드에 이용하는 방식
        var tmpList = mutableListOf<DayTimeSlot>()
        lateinit var tmp: DayTimeSlot

        firestore.collection("한국장학재단_부산").document("community")
            .collection("OUT_NOW")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                FacilityDayInfoData.day_time_slot_list.clear()
                for (document in value!!) {
                    Log.e("print!!!!", document.id)
                    if (document.id == document_name) {
                        (document.get(weekday) as ArrayList<HashMap<String, DayTimeSlot>>).let {
                            for (element in it) {
                                tmp = DayTimeSlot(
                                    element["buttonSelected"] as Boolean,
                                    (element["data"] as HashMap<String, Long>)["hour"]?.let { it1 ->
                                        (element["data"] as HashMap<String, Long>)["min"]?.let { it2 ->
                                            ReservationTimeData(
                                                it1, it2
                                            )
                                        }
                                    },
                                    element["index"] as Long,
                                    element["user"] as String
//                                    ,
//                                    weekdayStringToInt(document.id)
                                )
                                tmpList.add(tmp)
                            }
                        }
                        FacilityDayInfoData = ReservationFacilityDayInfo(
                            document_name,
                            icon,
                            interval_time,
                            weekday,
                            tmpList
                        )
                    }
                }
                //선택 데이터 초기화
                FacilitySelectedTimeSlotData.document_name = document_name
                FacilitySelectedTimeSlotData.weekday = weekday
                FacilitySelectedTimeSlotData.icon = icon
                FacilitySelectedTimeSlotData.interval_time = interval_time
                FacilitySelectedTimeSlotData.day_time_slot_list.clear()

                FacilitySelectedTimeSlotLiveData.value = FacilitySelectedTimeSlotData
                FacilityDayInfoLiveData.value = FacilityDayInfoData

                Log.e("FacilityDayInfoData",FacilityDayInfoData.toString())
            }
    }

    fun getPostDataInCategory(collection_name: String) : LiveData<ArrayList<PostDataInfo>> {
        return communityDataRepository.getPostDataInCategory(agencyInfo, collection_name)
    }

    fun create_deliveryOutReserveData() : SettingDeliveryOutReserveData{
        var startCal: Calendar = getReserveFacilityStartTime()
        val dateFmt: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val timeFmt: SimpleDateFormat = SimpleDateFormat("HH:mm:00.000")

        for (timeslot in FacilityDayInfoData.day_time_slot_list) {
            if (timeslot !in FacilitySelectedTimeSlotData.day_time_slot_list) {
                FacilitySelectedTimeSlotData.day_time_slot_list.add(timeslot)
            }
        }

        FacilitySelectedTimeSlotData.day_time_slot_list.sortBy { it.index }
        for (timeslot in FacilityDayInfoData.day_time_slot_list) {
            timeslot.buttonSelected = false
        }

        var tmp_day_time_slot_list: MutableList<DayTimeSlot> = mutableListOf()
        tmp_day_time_slot_list.addAll(FacilitySelectedTimeSlotData.day_time_slot_list)

        val OutReserveData = SettingDeliveryOutReserveData(
            tmp_day_time_slot_list ,
            dateFmt.format(startCal.time) + "T" + timeFmt.format(startCal.time),
            FacilitySelectedTimeSlotData.weekday
        )


        clear_select_time_slot()

        return OutReserveData


    }


}