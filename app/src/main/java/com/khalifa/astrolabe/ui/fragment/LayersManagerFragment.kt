package com.khalifa.astrolabe.ui.fragment

import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.khalifa.astrolabe.R
import com.khalifa.astrolabe.ui.adapter.MapLayersAdapter
import com.khalifa.astrolabe.ui.base.BaseFullScreenDialogFragment
import com.khalifa.astrolabe.viewmodel.Error
import com.khalifa.astrolabe.viewmodel.Event
import com.khalifa.astrolabe.viewmodel.fragment.implementation.LayerManagerViewModel
import kotlinx.android.synthetic.main.content_dialog_full_screen.*
import kotlinx.android.synthetic.main.fragment_map_sources_list.view.*

/**
 * @author Ahmad Khalifa
 */

class LayersManagerFragment :
        BaseFullScreenDialogFragment<LayerManagerViewModel>(),
        MapLayersAdapter.OnItemInteractionListener {

    companion object {
        private val TAG: String = LayersManagerFragment::class.java.simpleName

        fun showFragment(fragmentManager: FragmentManager?,
                         onFragmentInteractionListener: OnFragmentInteractionListener) =
                fragmentManager?.let { manager ->
                    LayersManagerFragment().also {
                        it.fragmentInteractionListener = onFragmentInteractionListener
                    }.show(manager, TAG)
                }
    }

    interface OnFragmentInteractionListener {

        fun loadBaseMap()

        fun loadMapLayers()

        fun deleteLayer()

        fun showLayer()

        fun hideLayer()

        fun openMapSourcesScreen()
    }

    private var fragmentInteractionListener: OnFragmentInteractionListener? = null
    private val mapLayersAdapter = MapLayersAdapter(this)

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_layers_manager, container, false)
                    .also {
                        with(it.toolbar) {
                            setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
                            setNavigationOnClickListener { dismiss() }
                            setTitle(R.string.map_sources)
                        }
                    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(recyclerView) {
            itemAnimator = DefaultItemAnimator()
            layoutManager = LinearLayoutManager(context)
            isNestedScrollingEnabled = false
            adapter = mapLayersAdapter
        }
        fragmentInteractionListener?.loadBaseMap()
        fragmentInteractionListener?.loadMapLayers()
    }

    override fun getViewModelInstance() = LayerManagerViewModel.getInstance(this)

    override fun onEvent(event: Event) {}

    override fun onError(error: Error) {}

    override fun registerLiveDataObservers() {}
}