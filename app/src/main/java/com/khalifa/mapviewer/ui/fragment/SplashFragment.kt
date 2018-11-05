package com.khalifa.mapviewer.ui.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.khalifa.mapviewer.R
import com.khalifa.mapviewer.ui.base.BaseFragment
import com.khalifa.mapviewer.viewmodel.Error
import com.khalifa.mapviewer.viewmodel.Event
import com.khalifa.mapviewer.viewmodel.fragment.splash.SplashViewModel

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

    override fun handleEvent(event: Event) {}

    override fun handleError(error: Error) {}

    override fun registerLiveDataObservers() {}
}