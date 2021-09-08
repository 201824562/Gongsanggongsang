package com.example.userapp.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import java.util.regex.Pattern

fun Fragment.hideKeyboard(view: View) {
    val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun setupKeyboardHide(view: View, activity: Activity?) {
    if (view !is EditText || view !is Button) {
        view.setOnTouchListener { _, _ ->
            activity?.let {
                val imm = it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(it.currentFocus?.windowToken, 0)
            }
            return@setOnTouchListener false
        }
    }
    if (view is ViewGroup) {
        for (i in 0 until view.childCount) {
            setupKeyboardHide(view.getChildAt(i), activity)
        }
    }
}

class SnackbarMessageString: SingleLiveEvent<String>(){
    fun observe(owner: LifecycleOwner, observer: (String) -> Unit){
        super.observe(owner, { it?.run { observer(it) } })
    }
}

object RegularExpressionUtils {

    enum class Regex {
        NAME, BIRTH, PHONE, ID, PWD, NICKNAME
    }

    fun validCheck(regex: Regex, value: String): Boolean {
        val p = Pattern.compile(getRegex(regex))
        val m = p.matcher(value)
        return m.matches()
    }

    private fun getRegex(regex: Regex): String =
        when (regex) {
            Regex.NAME -> "^[가-힣]{2,10}|[a-zA-Z]{2,10}\\s[a-zA-Z]{2,10}$"
            Regex.BIRTH -> "\\d{8}"
            Regex.PHONE -> "^\\s*(010)(-|\\)|\\s)*(\\d{4,4})(-|\\s)*(\\d{4})\\s*$"
            Regex.ID -> "^[a-zA-Z0-9]{4,12}$"
            Regex.PWD -> "^[a-zA-Z0-9]{8,22}$"
            Regex.NICKNAME -> "^[가-힣]{2,8}|[a-zA-Z]{2,8}\\s[a-zA-Z]{2,8}$"
        }
}

