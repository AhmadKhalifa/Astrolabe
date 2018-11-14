package com.khalifa.mapViewer.viewmodel.fragment.implementation

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModelProviders
import com.khalifa.mapViewer.data.model.tileSource.OnlineTileSourceFactory
import com.khalifa.mapViewer.ui.fragment.MapSourcesListFragment
import com.khalifa.mapViewer.viewmodel.BaseRxViewModel
import com.khalifa.mapViewer.viewmodel.fragment.IMapSourcesListViewModel
import org.osmdroid.tileprovider.tilesource.ITileSource

/**
 * @author Ahmad Khalifa
 */

class MapSourcesListViewModel : BaseRxViewModel(), IMapSourcesListViewModel {

    companion object {
        @JvmStatic
        fun getInstance(mapSourcesListFragment: MapSourcesListFragment): MapSourcesListViewModel =
                ViewModelProviders
                        .of(mapSourcesListFragment)
                        .get(MapSourcesListViewModel::class.java)
    }

    val mapSources = MutableLiveData<List<ITileSource>>()

    fun loadMapSources() {
        mapSources.value = OnlineTileSourceFactory.onlineMapSources
    }
}