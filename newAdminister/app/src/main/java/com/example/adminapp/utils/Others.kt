package com.example.adminapp.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import java.util.regex.Pattern

class SnackbarMessageString: SingleLiveEvent<String>(){
    fun observe(owner: LifecycleOwner, observer: (String) -> Unit){
        super.observe(owner, { it?.run { observer(it) } })
    }
}
fun Fragment.hideKeyboard(view: View) {
    val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
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
            Regex.NICKNAME -> "^[가-힣]{5,10}|[a-zA-Z]{5,10}\\s[a-zA-Z]{5,10}$"
        }
}