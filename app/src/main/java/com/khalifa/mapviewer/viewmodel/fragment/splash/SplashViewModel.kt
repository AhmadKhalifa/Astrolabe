package com.khalifa.mapviewer.viewmodel.fragment.splash

import android.arch.lifecycle.ViewModelProviders
import com.khalifa.mapviewer.ui.fragment.SplashFragment
import com.khalifa.mapviewer.viewmodel.BaseRxViewModel

/**
 * @author Ahmad Khalifa
 */

class SplashViewModel : BaseRxViewModel(), ISplashViewModel {

    companion object {
        @JvmStatic
        fun getInstance(splashFragment: SplashFragment): SplashViewModel =
                ViewModelProviders
                        .of(splashFragment)
                        .get(SplashViewModel::class.java)
    }
}