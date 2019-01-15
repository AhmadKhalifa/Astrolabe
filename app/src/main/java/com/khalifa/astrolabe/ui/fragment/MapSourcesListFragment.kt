package com.khalifa.astrolabe.ui.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.khalifa.astrolabe.R
import com.khalifa.astrolabe.data.model.tileSource.MapSourceFactory
import com.khalifa.astrolabe.ui.adapter.AllMapSourcesAdapter
import com.khalifa.astrolabe.ui.base.BaseFullScreenDialogFragment
import com.khalifa.astrolabe.viewmodel.Error
import com.khalifa.astrolabe.viewmodel.Event
import com.khalifa.astrolabe.viewmodel.fragment.implementation.MapSourcesListViewModel
import kotlinx.android.synthetic.main.content_dialog_full_screen.*
import kotlinx.android.synthetic.main.fragment_map_sources_list.view.*
import org.osmdroid.tileprovider.tilesource.ITileSource

/**
 * @author Ahmad Khalifa
 */

class MapSourcesListFragment :
        BaseFullScreenDialogFragment<MapSourcesListViewModel>(),
        AllMapSourcesAdapter.OnItemInteractionListener {

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
    private val mapSourceAdapter = AllMapSourcesAdapter(this)

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
            adapter = mapSourceAdapter
        }
        viewModel.loadMapSources()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setWindowAnimations(R.style.DialogAnimation)
        }
    }

    private fun updateMapSources(mapSources: ArrayList<MapSourceFactory.MapSource>) {
        mapSourceAdapter.mapSources = mapSources
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