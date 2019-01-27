package com.khalifa.astrolabe.viewmodel.activity

import android.arch.lifecycle.ViewModelProviders
import android.os.Handler
import com.khalifa.astrolabe.ui.activity.SplashActivity
import com.khalifa.astrolabe.viewmodel.BaseRxViewModel
import com.khalifa.astrolabe.viewmodel.Event
import com.khalifa.astrolabe.viewmodel.activity.implementation.ISplashActivityViewModel

/**
 * @author Ahmad Khalifa
 */

class SplashActivityViewModel : BaseRxViewModel(), ISplashActivityViewModel {

    companion object {

        private const val SPLASH_DELAY_IN_MS = 1000L

        fun getInstance(splashActivity: SplashActivity) =
                ViewModelProviders.of(splashActivity).get(SplashActivityViewModel::class.java)
    }

    override fun initialize() {
        Handler().postDelayed(
                { notify(Event.INITIALIZATION_DONE) },
                SPLASH_DELAY_IN_MS
        )
    }
}