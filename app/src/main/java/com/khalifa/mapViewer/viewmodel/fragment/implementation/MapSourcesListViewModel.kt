package com.khalifa.mapViewer.viewmodel.fragment.implementation

import android.arch.lifecycle.ViewModelProviders
import com.khalifa.mapViewer.ui.fragment.MapSourcesListFragment
import com.khalifa.mapViewer.viewmodel.BaseRxViewModel
import com.khalifa.mapViewer.viewmodel.fragment.IMapSourcesListViewModel

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
}