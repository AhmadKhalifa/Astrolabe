package com.khalifa.astrolabe.ui.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.khalifa.astrolabe.R
import com.khalifa.astrolabe.data.factory.MapSourceFactory
import com.khalifa.astrolabe.util.FragmentNotAttachedException
import com.khalifa.astrolabe.ui.adapter.AllMapSourcesAdapter
import com.khalifa.astrolabe.ui.base.BaseFragmentWithSharedViewModel
import com.khalifa.astrolabe.ui.widget.osmdroid.TilesOverlayWithOpacity
import com.khalifa.astrolabe.viewmodel.Error
import com.khalifa.astrolabe.viewmodel.Event
import com.khalifa.astrolabe.viewmodel.fragment.implementation.MapSourcesListViewModel
import com.khalifa.astrolabe.viewmodel.fragment.implementation.MapViewModel
import kotlinx.android.synthetic.main.fragment_recycler_view.*
import org.osmdroid.tileprovider.tilesource.ITileSource

/**
 * @author Ahmad Khalifa
 */

class MapSourcesListFragment :
        BaseFragmentWithSharedViewModel<MapSourcesListViewModel, MapViewModel>(),
        AllMapSourcesAdapter.OnItemInteractionListener {

    companion object {
        val TAG: String = MapSourcesListFragment::class.java.simpleName

        fun newInstance(fragmentInteractionListener: OnFragmentInteractionListener) =
                MapSourcesListFragment().also { fragment ->
                    fragment.fragmentInteractionListener = fragmentInteractionListener
                }
    }

    interface OnFragmentInteractionListener {

        fun onBaseMapSelected(tileSource: ITileSource)

        fun onMapOverlayAdded(mapOverlay: TilesOverlayWithOpacity)
    }

    private var fragmentInteractionListener: OnFragmentInteractionListener? = null
    private val mapSourceAdapter = AllMapSourcesAdapter(this)

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_recycler_view, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(recyclerView) {
            itemAnimator = DefaultItemAnimator()
            layoutManager = LinearLayoutManager(context)
            isNestedScrollingEnabled = false
            adapter = mapSourceAdapter
        }
        viewModel.loadMapSources()
        mapSourceAdapter.baseMapSource = sharedViewModel.baseMapSource.value
        mapSourceAdapter.mapLayers = sharedViewModel.mapLayers.value
    }

    private fun updateMapSources(mapSources: ArrayList<MapSourceFactory.MapSource>?) {
        if (mapSources?.isEmpty() != false) {
            noItemsLayout.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            noItemsLayout.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
        mapSourceAdapter.mapSources = mapSources
    }

    private fun onBaseMapSourceChanged(baseMapSource: ITileSource?) {
        mapSourceAdapter.baseMapSource = baseMapSource
    }

    private fun onMapLayersChanged(mapLayers: ArrayList<TilesOverlayWithOpacity>?) {
        mapSourceAdapter.mapLayers = mapLayers
    }

    override fun getViewModelInstance() = MapSourcesListViewModel.getInstance(this)

    override fun getSharedViewModelInstance() = activity?.run {
        MapViewModel.getInstance(this)
    } ?: throw FragmentNotAttachedException()

    override fun onEvent(event: Event) {}

    override fun onError(error: Error) {}

    override fun registerLiveDataObservers() {
        viewModel.mapSources.observe(this, Observer(::updateMapSources))
        sharedViewModel.baseMapSource.observe(this, Observer(::onBaseMapSourceChanged))
        sharedViewModel.mapLayers.observe(this, Observer(::onMapLayersChanged))
    }

    override fun onUseAsBaseMapClicked(tileSource: ITileSource) {
        sharedViewModel.setBaseMap(tileSource)
        fragmentInteractionListener?.onBaseMapSelected(tileSource)
    }

    override fun onAddAsMapLayerClicked(tileSource: ITileSource) {
        val mapOverlay = sharedViewModel.addMapLayer(tileSource)
        fragmentInteractionListener?.onMapOverlayAdded(mapOverlay)
    }
}