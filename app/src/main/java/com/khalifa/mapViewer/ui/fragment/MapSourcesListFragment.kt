package com.khalifa.mapViewer.ui.fragment

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.khalifa.mapViewer.R
import com.khalifa.mapViewer.ui.base.BaseFullScreenDialogFragment
import com.khalifa.mapViewer.viewmodel.Error
import com.khalifa.mapViewer.viewmodel.Event
import com.khalifa.mapViewer.viewmodel.fragment.implementation.MapSourcesListViewModel
import kotlinx.android.synthetic.main.fragment_map_sources_list.*

/**
 * @author Ahmad Khalifa
 */

class MapSourcesListFragment : BaseFullScreenDialogFragment<MapSourcesListViewModel>() {

    companion object {
        val TAG: String = MapSourcesListFragment::class.java.simpleName

        fun showDialog(fragmentManager: FragmentManager?) = fragmentManager?.let { manager ->
            MapSourcesListFragment().show(manager, TAG)
        }
    }

    interface OnFragmentInteractionListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_map_sources_list, container, false)
                    .also {
                        with(toolbar) {
                            setNavigationIcon(R.drawable.ic_close_white_24dp)
                            setNavigationOnClickListener { dismiss() }
                            setTitle(R.string.map_sources)
                        }
                    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }

    }

    override fun getViewModelInstance() = MapSourcesListViewModel.getInstance(this)

    override fun onEvent(event: Event) {}

    override fun onError(error: Error) {}

    override fun registerLiveDataObservers() {}
}