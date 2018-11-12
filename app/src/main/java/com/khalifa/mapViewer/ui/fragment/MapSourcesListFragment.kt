package com.khalifa.mapViewer.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.khalifa.mapViewer.R
import com.khalifa.mapViewer.ui.base.BaseFullScreenDialogFragment
import com.khalifa.mapViewer.viewmodel.Error
import com.khalifa.mapViewer.viewmodel.Event
import com.khalifa.mapViewer.viewmodel.fragment.implementation.MapSourcesListViewModel

/**
 * @author Ahmad Khalifa
 */

class MapSourcesListFragment : BaseFullScreenDialogFragment<MapSourcesListViewModel>() {

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

    override fun getViewModelInstance() = MapSourcesListViewModel.getInstance(this)

    override fun onEvent(event: Event) {}

    override fun onError(error: Error) {}

    override fun registerLiveDataObservers() {}
}