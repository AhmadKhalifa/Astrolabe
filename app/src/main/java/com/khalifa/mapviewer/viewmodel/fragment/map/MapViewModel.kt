package com.khalifa.mapViewer.viewmodel.fragment.map

import android.arch.lifecycle.ViewModelProviders
import com.khalifa.mapViewer.ui.fragment.MapFragment
import com.khalifa.mapViewer.viewmodel.BaseRxViewModel

/**
 * @author Ahmad Khalifa
 */

class MapViewModel : BaseRxViewModel(), IMapViewModel {

    companion object {
        @JvmStatic
        fun getInstance(mapFragment: MapFragment): MapViewModel =
                ViewModelProviders
                        .of(mapFragment)
                        .get(MapViewModel::class.java)
    }
}