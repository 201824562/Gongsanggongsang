package com.parasol.userapp.ui.base

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.parasol.userapp.R
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel> : Fragment() , BaseView {

    abstract val viewbinding : VB
    abstract val viewmodel : VM


    private val compositeDisposable = CompositeDisposable()

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    private fun addDisposable(disposable: Disposable){
        compositeDisposable.add(disposable)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return initViewbinding(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewStart(savedInstanceState)
        initDataBinding(savedInstanceState)
        initViewFinal(savedInstanceState)

        snackbarObserving()

    }

    abstract fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View?
    abstract fun initViewStart(savedInstanceState: Bundle?)
    abstract fun initDataBinding(savedInstanceState: Bundle?)
    abstract fun initViewFinal(savedInstanceState: Bundle?)



    fun snackbarObserving() {
        viewmodel.observeSnackbarMessageString(viewLifecycleOwner) { str ->
            if (isDetached)
                return@observeSnackbarMessageString
            activity?.let { activity ->
                val snackbar : Snackbar = Snackbar.make(activity.findViewById(android.R.id.content), str, Snackbar.LENGTH_SHORT)
                snackbar.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.black_20))
                snackbar.show() }
        }
    }

    @Throws(IllegalArgumentException::class)
    override fun showSnackbar(message: String) {
        if (isDetached) return
        activity?.let { activity ->
            val snackbar : Snackbar = Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT)
            snackbar.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.black_20))
            snackbar.show() } }


    override fun showToast(message: String) { Toast.makeText(activity, message, Toast.LENGTH_SHORT).show() }
    override fun showToast(stringRes: Int) { Toast.makeText(activity, stringRes, Toast.LENGTH_SHORT).show() }

    fun showDialog(dialog: Dialog, lifecycleOwner: LifecycleOwner?, cancelable: Boolean = true, dismissHandler: (() -> Unit)? = null) {
        val targetEvent = if (cancelable) Lifecycle.Event.ON_STOP else Lifecycle.Event.ON_DESTROY
        val observer = LifecycleEventObserver { _: LifecycleOwner, event: Lifecycle.Event ->
            if (event == targetEvent && dialog.isShowing) {
                dialog.dismiss()
                dismissHandler?.invoke()
            }
        }
        dialog.show()
        lifecycleOwner?.lifecycle?.addObserver(observer)
        dialog.setOnDismissListener { lifecycleOwner?.lifecycle?.removeObserver(observer) }

    }

    fun requestPermission(
        requestPermissionLauncher: ActivityResultLauncher<String>,
        permission: String,
        rationaleMessage: String,
        onGranted: () -> Unit
    ) {
        when {
            ContextCompat.checkSelfPermission(requireContext(), permission) == PERMISSION_GRANTED -> {
                onGranted()
            }
            shouldShowRequestPermissionRationale(permission) -> {
                showPermissionRationale(rationaleMessage)
            }
            else -> requestPermissionLauncher.launch(permission)
        }
    }

    private fun showPermissionRationale(message: String) {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("권한 요청")
            .setMessage(message)
            .setPositiveButton("확인") { _, _ ->
                goToSettings()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun goToSettings() {
        val uri = Uri.parse("package:${requireActivity().packageName}")
        startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri))
    }
}