package com.khalifa.mapViewer.viewmodel.fragment.implementation

import android.arch.lifecycle.ViewModelProviders
import com.khalifa.mapViewer.ui.fragment.SplashFragment
import com.khalifa.mapViewer.viewmodel.BaseRxViewModel
import com.khalifa.mapViewer.viewmodel.fragment.ISplashViewModel

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