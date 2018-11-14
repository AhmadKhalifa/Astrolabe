package com.khalifa.mapViewer.ui.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.khalifa.mapViewer.R
import com.khalifa.mapViewer.ui.adapter.MapSourceAdapter
import com.khalifa.mapViewer.ui.base.BaseFullScreenDialogFragment
import com.khalifa.mapViewer.viewmodel.Error
import com.khalifa.mapViewer.viewmodel.Event
import com.khalifa.mapViewer.viewmodel.fragment.implementation.MapSourcesListViewModel
import kotlinx.android.synthetic.main.content_dialog_full_screen.*
import kotlinx.android.synthetic.main.fragment_map_sources_list.*
import org.osmdroid.tileprovider.tilesource.ITileSource

/**
 * @author Ahmad Khalifa
 */

class MapSourcesListFragment :
        BaseFullScreenDialogFragment<MapSourcesListViewModel>(),
        MapSourceAdapter.OnItemInteractionListener {

    companion object {
        private val TAG: String = MapSourcesListFragment::class.java.simpleName

        fun showFragment(fragmentManager: FragmentManager?,
                         onFragmentInteractionListener: OnFragmentInteractionListener) =
                fragmentManager?.let { manager ->
                    MapSourcesListFragment().also {
                        it.fragmentInteractionListener = onFragmentInteractionListener
                    }.show(manager, TAG)
                }
    }

    interface OnFragmentInteractionListener {

        fun onTileSourceSelectedAsBaseMap(tileSource: ITileSource)

        fun onTileSourceSelectedAsLayer(tileSource: ITileSource)
    }

    private var fragmentInteractionListener: OnFragmentInteractionListener? = null
    private val mapSourceAdapter = MapSourceAdapter(this)

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
//                        with(toolbar) {
//                            setNavigationIcon(R.drawable.ic_close_white_24dp)
//                            setNavigationOnClickListener { dismiss() }
//                            setTitle(R.string.map_sources)
//                        }
                    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(recyclerView) {
            itemAnimator = DefaultItemAnimator()
            layoutManager = LinearLayoutManager(context)
            adapter = mapSourceAdapter
        }
        viewModel.loadMapSources()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }

    private fun updateMapSources(mapSources: List<ITileSource>) {
        mapSourceAdapter.tileSources = mapSources
    }

    override fun getViewModelInstance() = MapSourcesListViewModel.getInstance(this)

    override fun onEvent(event: Event) {}

    override fun onError(error: Error) {}

    override fun registerLiveDataObservers() {
        viewModel.mapSources.observe(this, Observer { mapSources ->
            mapSources?.let { updateMapSources(it) }
        })
    }

    override fun onTileSourceSelectedAsBaseMap(tileSource: ITileSource) {
        dismiss()
        fragmentInteractionListener?.onTileSourceSelectedAsBaseMap(tileSource)
    }

    override fun onTileSourceSelectedAsLayer(tileSource: ITileSource) {
        dismiss()
        fragmentInteractionListener?.onTileSourceSelectedAsLayer(tileSource)
    }
}