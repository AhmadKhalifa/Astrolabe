package com.khalifa.astrolabe.util

import android.content.Context
import android.net.ConnectivityManager

/**
 * @author Ahmad Khalifa
 */

class InternetConnectionChecker(private val applicationContext: Context?) : BaseConnectionChecker {

    override fun isNetworkAvailable() = applicationContext?.let {
        (it.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
                .activeNetworkInfo?.isConnected
    } ?: false
}