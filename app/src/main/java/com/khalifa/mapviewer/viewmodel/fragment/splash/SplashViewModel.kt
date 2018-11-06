package com.khalifa.mapViewer.viewmodel.fragment.splash

import android.arch.lifecycle.ViewModelProviders
import com.khalifa.mapViewer.ui.fragment.SplashFragment
import com.khalifa.mapViewer.viewmodel.BaseRxViewModel

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