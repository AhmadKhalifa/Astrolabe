package com.khalifa.astrolabe.viewmodel.activity

import android.arch.lifecycle.ViewModelProviders
import android.os.Handler
import com.khalifa.astrolabe.ui.activity.SplashActivity
import com.khalifa.astrolabe.viewmodel.BaseRxViewModel
import com.khalifa.astrolabe.viewmodel.Event

/**
 * @author Ahmad Khalifa
 */

class SplashActivityViewModel : BaseRxViewModel() {

    companion object {

        private const val SPLASH_DELAY_IN_MS = 1000L

        @JvmStatic
        fun getInstance(splashActivity: SplashActivity): SplashActivityViewModel =
                ViewModelProviders
                        .of(splashActivity)
                        .get(SplashActivityViewModel::class.java)
    }

    fun initialize() {
        Handler().postDelayed(
                { notify(Event.INITIALIZATION_DONE) },
                SPLASH_DELAY_IN_MS
        )
    }
}