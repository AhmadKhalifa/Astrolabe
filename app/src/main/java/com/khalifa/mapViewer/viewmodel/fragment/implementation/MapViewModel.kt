package com.khalifa.mapViewer.viewmodel.fragment.implementation

import android.arch.lifecycle.ViewModelProviders
import com.khalifa.mapViewer.ui.fragment.MapFragment
import com.khalifa.mapViewer.viewmodel.BaseRxViewModel
import com.khalifa.mapViewer.viewmodel.fragment.IMapViewModel

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