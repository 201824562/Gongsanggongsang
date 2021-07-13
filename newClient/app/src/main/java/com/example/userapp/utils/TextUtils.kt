package com.example.userapp.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

object TextUtils {
    @JvmStatic
    fun isEmpty(str: CharSequence?): Boolean = str.isNullOrEmpty()
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> (Unit)) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}