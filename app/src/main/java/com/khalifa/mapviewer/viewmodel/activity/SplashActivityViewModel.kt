package com.khalifa.mapViewer.viewmodel.activity

import android.arch.lifecycle.ViewModelProviders
import android.os.Handler
import com.khalifa.mapViewer.ui.activity.SplashActivity
import com.khalifa.mapViewer.viewmodel.BaseRxViewModel
import com.khalifa.mapViewer.viewmodel.Event

/**
 * @author Ahmad Khalifa
 */

class SplashActivityViewModel : BaseRxViewModel() {

    companion object {

        private const val SPLASH_DELAY_IN_MS = 0L // 2000L

        @JvmStatic
        fun getInstance(splashActivity: SplashActivity): SplashActivityViewModel =
                ViewModelProviders
                        .of(splashActivity)
                        .get(SplashActivityViewModel::class.java)
    }

    fun initialize() {
        Handler().postDelayed(
                { notify(Event.EXIT_SPLASH_SCREEN) },
                SPLASH_DELAY_IN_MS
        )
    }
}