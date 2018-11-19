package com.khalifa.astrolabe.util

import android.support.annotation.StringRes
import android.widget.Toast
import com.khalifa.astrolabe.AstrolabeApplication
import com.khalifa.astrolabe.AstrolabeApplication.Companion.getString

private var toast: Toast? = null

fun toast(@StringRes messageResId: Int) = showToast(getString(messageResId), Toast.LENGTH_SHORT)

fun toast(message: String?) = showToast(message, Toast.LENGTH_SHORT)

fun longToast(@StringRes messageResId: Int) = showToast(getString(messageResId), Toast.LENGTH_LONG)

fun longToast(message: String?) = showToast(message, Toast.LENGTH_LONG)

private fun showToast(message: String?, duration: Int) {
    toast?.cancel()
    toast = Toast.makeText(AstrolabeApplication.instance, "$message", duration)
    toast?.show()
}