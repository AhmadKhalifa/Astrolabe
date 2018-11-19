package com.khalifa.astrolabe.ui.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.khalifa.astrolabe.R
import com.khalifa.astrolabe.ui.base.BaseFragment
import com.khalifa.astrolabe.viewmodel.Error
import com.khalifa.astrolabe.viewmodel.Event
import com.khalifa.astrolabe.viewmodel.fragment.implementation.SplashViewModel

/**
 * @author Ahmad Khalifa
 */

class SplashFragment : BaseFragment<SplashViewModel>() {

    companion object {
        val TAG: String = SplashFragment::class.java.simpleName

        fun newInstance() = SplashFragment()
    }

    interface OnFragmentInteractionListener

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_splash, container, false)

    override fun getViewModelInstance() = SplashViewModel.getInstance(this)

    override fun onEvent(event: Event) {}

    override fun onError(error: Error) {}

    override fun registerLiveDataObservers() {}
}