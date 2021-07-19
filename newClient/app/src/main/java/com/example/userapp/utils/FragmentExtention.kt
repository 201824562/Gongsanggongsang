package com.example.userapp.utils

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController


/*
inline fun <reified VM : ViewModel> Fragment.sharedGraphViewModel(
    @IdRes navGraphId: Int,
    bundle: Bundle = Bundle(),
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
) = lazy {
    getKoin().getViewModel(ViewModelParameter(VM::class,  qualifier, parameters, bundle, findNavController().getViewModelStoreOwner(navGraphId).viewModelStore))
}
*/

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
