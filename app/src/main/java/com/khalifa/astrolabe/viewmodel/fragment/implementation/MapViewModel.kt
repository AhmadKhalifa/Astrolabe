package com.khalifa.astrolabe.viewmodel.fragment.implementation

import android.arch.lifecycle.ViewModelProviders
import com.khalifa.astrolabe.ui.fragment.MapFragment
import com.khalifa.astrolabe.viewmodel.BaseRxViewModel
import com.khalifa.astrolabe.viewmodel.fragment.IMapViewModel

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