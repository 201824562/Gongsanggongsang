package com.example.userapp.utils

import android.app.Application
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import com.example.userapp.databinding.DialogBasicTwobuttonBinding
import com.example.userapp.databinding.DialogInputUsingTimeBinding
import com.example.userapp.databinding.FragmentMainhomeReservationEquipmentDialogBinding
import kotlin.system.exitProcess


class LogoutDialog(context: Context, title: String, content: String, btnText: String) :
    Dialog(context) {

    var clickListener: DialogButtonClickListener? = null

    interface DialogButtonClickListener {
        fun dialogCloseClickListener()
        fun dialogDeleteClickListener()
    }

    init {
        val binding: DialogBasicTwobuttonBinding =
            DialogBasicTwobuttonBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        window?.run {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes.width = ViewGroup.LayoutParams.WRAP_CONTENT
            attributes.height = ViewGroup.LayoutParams.WRAP_CONTENT
        } ?: exitProcess(0)

        binding.dialogTitle.text = title
        binding.dialogContent.text = content
        binding.dialogDeleteBtn.text = btnText
        binding.dialogCloseBtn.setOnClickListener { clickListener?.dialogCloseClickListener() }
        binding.dialogDeleteBtn.setOnClickListener { clickListener?.dialogDeleteClickListener() }
    }
}

// 바로사용 시간 설정 dialog
class InputUsingTimeDialog(context: Context) : Dialog(context) { //도큐먼트 이름도 받아와야함
    var clickListener: DialogButtonClickListener? = null
    var usingTime = 0
    val maxTime: Int = 50

    interface DialogButtonClickListener {
        fun dialogCloseClickListener()
        fun usingClickListener()
        fun plus5ClickListener()
        fun plus10ClickListener()
        fun plus15ClickListener()
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
        binding.useBtn.setOnClickListener { clickListener?.usingClickListener() }
        // + 버튼 처
        binding.plus5miniteBtn.setOnClickListener {
            usingTime += 5
            usingTime = excessMax(usingTime)
            binding.usingTimeEdittext.text = usingTime.toString()
            clickListener?.plus5ClickListener()
        }
        binding.plus10miniteBtn.setOnClickListener {
            usingTime += 10
            usingTime = excessMax(usingTime)
            binding.usingTimeEdittext.text = usingTime.toString()
            clickListener?.plus10ClickListener()
        }
        binding.plus15miniteBtn.setOnClickListener {
            usingTime += 15
            usingTime = excessMax(usingTime)
            binding.usingTimeEdittext.text = usingTime.toString()
            clickListener?.plus15ClickListener()
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
/*

class confirmUsingDialog(context: Context, usingTime: Int) : Dialog(context) { // 도큐먼트 이름도 받아와야 함

    var clickListener: DialogButtonClickListener? = null

    interface DialogButtonClickListener {
        fun dialogCloseClickListener()
        fun dialogDeleteClickListener()
    }

    init {
        //val binding: DialogBasicTwobuttonBinding = DialogBasicTwobuttonBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        window?.run {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes.width = ViewGroup.LayoutParams.WRAP_CONTENT
            attributes.height = ViewGroup.LayoutParams.WRAP_CONTENT
        } ?: exitProcess(0)
    }
}*/
