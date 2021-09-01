package com.example.userapp.utils

import android.app.Application
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import com.example.userapp.databinding.DialogBasicOneButtonBinding
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.userapp.data.model.ReservationEquipment
import com.example.userapp.data.model.ReservationFacility
import com.example.userapp.databinding.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import androidx.core.content.ContextCompat
import com.example.userapp.R
import com.example.userapp.data.model.ReservationAlarmData
import com.example.userapp.data.model.SignUpAlarmData
import com.example.userapp.databinding.*
import com.example.userapp.ui.main.alarm.getHourMinuteString
import com.example.userapp.ui.main.alarm.getMonthDayString
import com.example.userapp.ui.main.alarm.getMonthString
import kotlin.system.exitProcess


class WrapedDialogBasicTwoButton (context: Context, content: String, closeBtnText: String, customBtnText: String) : Dialog(context){

    var clickListener : DialogButtonClickListener ? = null
    interface DialogButtonClickListener {
        fun dialogCloseClickListener()
        fun dialogCustomClickListener()
    }

    init {
        //setCanceledOnTouchOutside(false)
        val binding : DialogBasicTwobuttonBinding = DialogBasicTwobuttonBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        window?.run {
            setBackgroundDrawable(InsetDrawable(ColorDrawable(Color.TRANSPARENT), 50))
            attributes.width = ViewGroup.LayoutParams.MATCH_PARENT
            attributes.height = ViewGroup.LayoutParams.WRAP_CONTENT
        } ?: exitProcess(0)

        binding.run {
            dialogContent.text = content
            dialogCloseBtn.text = closeBtnText
            dialogCustomBtn.text = customBtnText
            dialogCloseBtn.setOnClickListener { clickListener?.dialogCloseClickListener()}
            dialogCustomBtn.setOnClickListener { clickListener?.dialogCustomClickListener() }
        }
    }
}

class MatchedDialogAccentTwoButton (context: Context, content: String, closeBtnText: String, customBtnText: String) : Dialog(context){

    var clickListener : DialogButtonClickListener ? = null
    interface DialogButtonClickListener {
        fun dialogCloseClickListener()
        fun dialogCustomClickListener()
    }

    init {
        //setCanceledOnTouchOutside(false)
        val binding : DialogAccentTwobuttonBinding = DialogAccentTwobuttonBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        window?.run {
            setBackgroundDrawable(InsetDrawable(ColorDrawable(Color.TRANSPARENT), 50))
            attributes.width = ViewGroup.LayoutParams.MATCH_PARENT
            attributes.height = ViewGroup.LayoutParams.WRAP_CONTENT
        } ?: exitProcess(0)

        binding.run {
            dialogContent.text = content
            dialogCloseBtn.text = closeBtnText
            dialogCustomBtn.text = customBtnText
            dialogCloseBtn.setOnClickListener { clickListener?.dialogCloseClickListener()}
            dialogCustomBtn.setOnClickListener { clickListener?.dialogCustomClickListener() }
        }
    }
}


class MatchedFullDialogBasicOneButton (context: Context, title: String, content: String) : Dialog(context){
    var clickListener : DialogButtonClickListener ? = null
    interface DialogButtonClickListener {
        fun dialogCloseClickListener()
        fun dialogClickListener()
    }
    init {
        val binding : DialogFullBasicOneButtonBinding = DialogFullBasicOneButtonBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        window?.run {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes.width = ViewGroup.LayoutParams.MATCH_PARENT
            attributes.height = ViewGroup.LayoutParams.MATCH_PARENT
        } ?: exitProcess(0)

        binding.dialogTitle.text = title
        binding.dialogContent.text = content
        binding.dialogCloseBtn.setOnClickListener { clickListener?.dialogCloseClickListener()}
        binding.dialogBtn.setOnClickListener { clickListener?.dialogClickListener() }
    }
}
//
class WrapedCommunityDialogBasicOneButton (context: Context, content: String) : Dialog(context){
    var clickListener : DialogButtonClickListener ? = null
    interface DialogButtonClickListener {
        fun dialogClickListener()
    }
    init {
        val binding: DialogCommunityBasicOneButtonBinding =
            DialogCommunityBasicOneButtonBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        window?.run {
            setBackgroundDrawable(InsetDrawable(ColorDrawable(Color.TRANSPARENT), 30))
            attributes.width = ViewGroup.LayoutParams.MATCH_PARENT
            attributes.height = ViewGroup.LayoutParams.WRAP_CONTENT
        } ?: exitProcess(0)
    }
}

class WrapedDialogBasicOneButton (context: Context, content: String) : Dialog(context){
    var clickListener : DialogButtonClickListener ? = null
    interface DialogButtonClickListener {
        fun dialogClickListener()
    }
    init {
        val binding : DialogBasicOneButtonBinding = DialogBasicOneButtonBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        window?.run {
            setBackgroundDrawable(InsetDrawable(ColorDrawable(Color.TRANSPARENT), 50))
            attributes.width = ViewGroup.LayoutParams.MATCH_PARENT
            attributes.height = ViewGroup.LayoutParams.WRAP_CONTENT
        } ?: exitProcess(0)

        binding.dialogContent.text = content
        binding.dialogBtn.setOnClickListener { clickListener?.dialogClickListener() }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
class CustomedAlarmDialog (context: Context, reserveData : ReservationAlarmData?, signData : SignUpAlarmData?) : Dialog(context){
    var clickListener : DialogButtonClickListener ? = null
    interface DialogButtonClickListener {
        fun dialogCloseClickListener()
        fun dialogClickListener()
    }
    init {
        val binding : DialogAlarmBinding = DialogAlarmBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        window?.run {
            setGravity(Gravity.BOTTOM)
            setBackgroundDrawable(InsetDrawable(ColorDrawable(Color.TRANSPARENT), 0))
            attributes.width = ViewGroup.LayoutParams.MATCH_PARENT
            attributes.height = ViewGroup.LayoutParams.WRAP_CONTENT
        } ?: exitProcess(0)

        binding.run {
            if (reserveData != null){
                val day : String = getMonthString(reserveData.startTime) + "/" + getMonthDayString(reserveData.startTime)
                val time : String = getHourMinuteString(reserveData.startTime) + " - " + getHourMinuteString(reserveData.endTime)
                val msg : String =  "메이트님의 예약 정보가 맞다면\n사용시작을 눌러주세요."
                dialogTitle.text = "예약 사용"
                dialogContentText1.text = "시설이름"
                dialogContentText2.text = "예약날짜"
                dialogContentText3.text = "예약시간"
                dialogContentVar1.text = reserveData.name
                dialogContentVar2.text = day
                dialogContentVar3.text = time
                dialogMsg.text = msg
                dialogLeftBtn.text = "사용 취소"
                dialogRightBtn.text = "사용 시작"
            }
            if (signData != null){
                val birth : String = signData.birthday.substring(0,4) + "/" + signData.birthday.substring(4,6) + "/" + signData.birthday.substring(6,8)
                val phone : String = signData.smsInfo.substring(0,3) + "-" + signData.smsInfo.substring(4,8) + signData.smsInfo.substring(9,13)
                val msg : String =  "관리하시는 공간의 회원님이 맞다면\n가입승인을 눌러주세요."
                dialogTitle.text = "가입 승인"
                dialogContentText1.text = "이름"
                dialogContentText2.text = "생년월일"
                dialogContentText3.text = "전화번호"
                dialogContentVar1.text = signData.name
                dialogContentVar2.text = birth
                dialogContentVar3.text = phone
                dialogMsg.text = msg
                dialogLeftBtn.text = "가입 거절"
                dialogRightBtn.text = "가입 승인"
            }
            dialogLeftBtn.setOnClickListener { clickListener?.dialogCloseClickListener() }
            dialogRightBtn.setOnClickListener { clickListener?.dialogClickListener() }
        }
    }
}

// 바로사용 시간 설정 dialog
class InputUsingTimeDialog(context: Context, var reservationEquipment: ReservationEquipment) : Dialog(context, R.style.DialogCustomTheme) { //도큐먼트 이름도 받아와야함
    var clickListener: DialogButtonClickListener? = null
    var usingTime = 0
    var maxTime : Int = Math.toIntExact(reservationEquipment.maxTime)

    interface DialogButtonClickListener {
        fun dialogCloseClickListener()
        fun usingClickListener(usingTime: Int)
    }

    init {
        val binding = DialogInputUsingTimeBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)    // 다이얼로그외에 다른 화면을 눌렀을 때 나가는 것을 방지
        setContentView(binding.root)
        window?.run {
            setGravity(Gravity.BOTTOM)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes.width = ViewGroup.LayoutParams.MATCH_PARENT
            attributes.height = ViewGroup.LayoutParams.WRAP_CONTENT
        } ?: exitProcess(0)

        // 다이얼로그 처리
        binding.backBtn.setOnClickListener { clickListener?.dialogCloseClickListener() }
        binding.useBtn.setOnClickListener {
            clickListener?.usingClickListener(usingTime)
        }
        // 뷰 + 버튼 처리
        binding.documentNameTextview.text = reservationEquipment.document_name
        binding.useableTime.text = maxTime.toString()
        binding.plus5miniteBtn.setOnClickListener {
            usingTime += 5
            usingTime = excessMax(usingTime)
            binding.usingTimeEdittext.text = usingTime.toString()
        }
        binding.plus10miniteBtn.setOnClickListener {
            usingTime += 10
            usingTime = excessMax(usingTime)
            binding.usingTimeEdittext.text = usingTime.toString()
        }
        binding.plus15miniteBtn.setOnClickListener {
            usingTime += 15
            usingTime = excessMax(usingTime)
            binding.usingTimeEdittext.text = usingTime.toString()
        }
        //키패드처리
        binding.oneBtn.setOnClickListener {
            usingTime = (usingTime.toString() + '1').toInt()
            usingTime = excessMax(usingTime)
            binding.usingTimeEdittext.text = usingTime.toString()
        }
        binding.twoBtn.setOnClickListener {
            usingTime = (usingTime.toString() + '2').toInt()
            usingTime = excessMax(usingTime)
            binding.usingTimeEdittext.text = usingTime.toString()
        }
        binding.threeBtn.setOnClickListener {
            usingTime = (usingTime.toString() + '3').toInt()
            usingTime = excessMax(usingTime)
            binding.usingTimeEdittext.text = usingTime.toString()
        }
        binding.fourBtn.setOnClickListener {
            usingTime = (usingTime.toString() + '4').toInt()
            usingTime = excessMax(usingTime)
            binding.usingTimeEdittext.text = usingTime.toString()
        }
        binding.fiveBtn.setOnClickListener {
            usingTime = (usingTime.toString() + '5').toInt()
            usingTime = excessMax(usingTime)
            binding.usingTimeEdittext.text = usingTime.toString()
        }
        binding.sixBtn.setOnClickListener {
            usingTime = (usingTime.toString() + '6').toInt()
            usingTime = excessMax(usingTime)
            binding.usingTimeEdittext.text = usingTime.toString()
        }
        binding.sevenBtn.setOnClickListener {
            usingTime = (usingTime.toString() + '7').toInt()
            usingTime = excessMax(usingTime)
            binding.usingTimeEdittext.text = usingTime.toString()
        }
        binding.eightBtn.setOnClickListener {
            usingTime = (usingTime.toString() + '8').toInt()
            usingTime = excessMax(usingTime)
            binding.usingTimeEdittext.text = usingTime.toString()
        }
        binding.nineBtn.setOnClickListener {
            usingTime = (usingTime.toString() + '9').toInt()
            usingTime = excessMax(usingTime)
            binding.usingTimeEdittext.text = usingTime.toString()
        }
        binding.zeroBtn.setOnClickListener {
            usingTime = (usingTime.toString() + '0').toInt()
            usingTime = excessMax(usingTime)
            binding.usingTimeEdittext.text = usingTime.toString()
        }
        binding.deleteBtn.setOnClickListener { // 한글자까지 줄어들면 익셉션나는거 처리
            var stringtime = usingTime.toString()
            if (stringtime.length == 1) {
                stringtime = "0"
            } else {
                stringtime = stringtime.substring(0, stringtime.length - 1)
            }
            usingTime = stringtime.toInt()
            binding.usingTimeEdittext.text = usingTime.toString()
        }
        binding.maxBtn.setOnClickListener {
            usingTime = maxTime
            binding.usingTimeEdittext.text = usingTime.toString()
        }
    }

    private fun excessMax(usingTime: Int): Int {
        return if (usingTime > maxTime) {
            Toast.makeText(context, "최대이용가능한 시간을 초과하였습니다!\n다시 입력해주세요.", Toast.LENGTH_SHORT).show()
            maxTime
        } else {
            usingTime
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
class ConfirmUsingDialog(context: Context, usingTime: Int, reservationEquipment: ReservationEquipment) : Dialog(context) { // 도큐먼트 이름도 받아와야 함

    var clickListener: DialogButtonClickListener? = null

    interface DialogButtonClickListener {
        fun dialogAgainClickListener()
        fun dialogUsingClickListener()
    }

    init {
        val binding = DialogConfirmUsingBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)    // 다이얼로그외에 다른 화면을 눌렀을 때 나가는 것을 방지
        setContentView(binding.root)
        window?.run {
            setGravity(Gravity.BOTTOM)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes.width = ViewGroup.LayoutParams.MATCH_PARENT
            attributes.height = ViewGroup.LayoutParams.WRAP_CONTENT
        } ?: exitProcess(0)

        binding.documentNameTextview.text = reservationEquipment.document_name
        binding.usingTimeTextview.text = usingTime.toString() + "분"
        binding.endUsingTimeTextview.text = LocalDateTime.now().plusMinutes(usingTime.toLong()).format(DateTimeFormatter.ofPattern("HH:mm"))
        binding.againInputTimeBtn.setOnClickListener { clickListener?.dialogAgainClickListener() }
        binding.startUsingBtn.setOnClickListener {
            clickListener?.dialogUsingClickListener()

            val finishUsingDialog = FinishUsingDialog(context, reservationEquipment).apply {
                clickListener = object : FinishUsingDialog.DialogButtonClickListener {
                    override fun dialogConfirmClickListener() {
                        dismiss()
                    }
                }
            }
            finishUsingDialog.show()
        }
    }
}

class ConfirmReserveDialog(context: Context, reservationFacility: ReservationFacility, startCal: Calendar, endCal: Calendar) : Dialog(context, R.style.DialogCustomTheme) { // 도큐먼트 이름도 받아와야 함

    var clickListener: DialogButtonClickListener? = null
    val dateFmt : SimpleDateFormat = SimpleDateFormat("MM월 dd일")
    val timeFmt : SimpleDateFormat = SimpleDateFormat("HH:mm")

    interface DialogButtonClickListener {
        fun dialogAgainClickListener()
        fun dialogReserveClickListener()
    }

    init {
        val binding = DialogConfirmUsingBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)    // 다이얼로그외에 다른 화면을 눌렀을 때 나가는 것을 방지
        setContentView(binding.root)
        window?.run {
            setGravity(Gravity.BOTTOM)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes.width = ViewGroup.LayoutParams.MATCH_PARENT
            attributes.height = ViewGroup.LayoutParams.WRAP_CONTENT
        } ?: exitProcess(0)

        binding.titleTextview.text = "예약확인"
        binding.targetTextview.text = "예약 대상"
        binding.documentNameTextview.text = reservationFacility.document_name
        binding.timeTextview.text = "예약 날짜"
        binding.usingTimeTextview.text = dateFmt.format(startCal.time)
        binding.endtimeTextview.text = "예약 시간"
        binding.endUsingTimeTextview.text = timeFmt.format(startCal.time) + "~" + timeFmt.format(endCal.time)
        binding.messageTextview.text = "이대로 예약하시겠어요?"
        binding.againInputTimeBtn.text = "다시 선택"
        binding.startUsingBtn.text = "예약"
        binding.againInputTimeBtn.setOnClickListener { clickListener?.dialogAgainClickListener() }
        binding.startUsingBtn.setOnClickListener {
            clickListener?.dialogReserveClickListener()

            val finishReserveDialog = FinishReserveDialog(context).apply {
                clickListener = object : FinishReserveDialog.DialogButtonClickListener {
                    override fun dialogConfirmClickListener() {
                        dismiss()
                    }
                }
            }
            finishReserveDialog.show()
        }
    }
}

class FinishUsingDialog(context: Context, reservationEquipment: ReservationEquipment) : Dialog(context) {

    var clickListener: DialogButtonClickListener? = null

    interface DialogButtonClickListener {
        fun dialogConfirmClickListener()
    }

    init {
        val binding = DialogFinishUsingBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)    // 다이얼로그외에 다른 화면을 눌렀을 때 나가는 것을 방지
        setContentView(binding.root)
        window?.run {
            setGravity(Gravity.BOTTOM)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes.width = ViewGroup.LayoutParams.MATCH_PARENT
            attributes.height = ViewGroup.LayoutParams.WRAP_CONTENT
        } ?: exitProcess(0)
        binding.message1Textview.text = reservationEquipment.document_name + " 사용이 시작됐어요"
        binding.confirmBtn.setOnClickListener { clickListener?.dialogConfirmClickListener() }
    }
}


class FinishReserveDialog(context: Context) : Dialog(context) {

    var clickListener: DialogButtonClickListener? = null

    interface DialogButtonClickListener {
        fun dialogConfirmClickListener()
    }

    init {
        val binding = DialogFinishUsingBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)    // 다이얼로그외에 다른 화면을 눌렀을 때 나가는 것을 방지
        setContentView(binding.root)
        window?.run {
            setGravity(Gravity.BOTTOM)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes.width = ViewGroup.LayoutParams.MATCH_PARENT
            attributes.height = ViewGroup.LayoutParams.WRAP_CONTENT
        } ?: exitProcess(0)
        binding.message1Textview.text = "예약이 완료됐어요"
        binding.message2Textview.text = "시작 5분전에 알람으로 알려드릴게요:)"
        binding.confirmBtn.setOnClickListener { clickListener?.dialogConfirmClickListener() }
    }
}

class CautionMessageDialog(context: Context, message: String) : Dialog(context) {

    var clickListener: DialogButtonClickListener? = null

    interface DialogButtonClickListener {
        fun dialogConfirmClickListener()
    }

    init {
        val binding = DialogCautionMessageBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)    // 다이얼로그외에 다른 화면을 눌렀을 때 나가는 것을 방지
        setContentView(binding.root)
        window?.run {
            setGravity(Gravity.CENTER)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes.height = ViewGroup.LayoutParams.WRAP_CONTENT
        } ?: exitProcess(0)
        binding.messageTextview.text = message
        binding.confirmBtn.setOnClickListener { clickListener?.dialogConfirmClickListener() }
    }
}
