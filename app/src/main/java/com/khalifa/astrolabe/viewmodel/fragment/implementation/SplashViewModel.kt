package com.khalifa.astrolabe.viewmodel.fragment.implementation

import android.arch.lifecycle.ViewModelProviders
import com.khalifa.astrolabe.ui.fragment.SplashFragment
import com.khalifa.astrolabe.viewmodel.BaseRxViewModel
import com.khalifa.astrolabe.viewmodel.fragment.ISplashViewModel

/**
 * @author Ahmad Khalifa
 */

class SplashViewModel : BaseRxViewModel(), ISplashViewModel {

    companion object {

        fun getInstance(splashFragment: SplashFragment) =
                ViewModelProviders.of(splashFragment).get(SplashViewModel::class.java)
    }
}