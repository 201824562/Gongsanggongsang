package com.example.adminapp.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Build
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.RequiresApi
import com.example.adminapp.data.model.ReservationAlarmData
import com.example.adminapp.data.model.SignUpAlarmData
import com.example.adminapp.databinding.DialogAccentTwobuttonBinding
import com.example.adminapp.databinding.DialogAlarmBinding
import com.example.adminapp.databinding.DialogBasicOnebuttonBinding
import com.example.adminapp.databinding.DialogBasicTwobuttonBinding
import com.example.adminapp.ui.main.reservation.getHourMinuteString
import com.example.adminapp.ui.main.reservation.getMonthDayString
import com.example.adminapp.ui.main.reservation.getMonthString

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

class WrapedDialogAccentTwoButton (context: Context, content: String, closeBtnText: String, customBtnText: String) : Dialog(context){

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


class WrapedDialogBasicOneButton (context: Context, content: String) : Dialog(context){
    var clickListener : DialogButtonClickListener ? = null
    interface DialogButtonClickListener { fun dialogClickListener() }
    init {
        val binding : DialogBasicOnebuttonBinding = DialogBasicOnebuttonBinding.inflate(layoutInflater)
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
                val phone : String = signData.smsInfo.substring(0,3) + "-" + signData.smsInfo.substring(3,7) + signData.smsInfo.substring(7,11)
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
