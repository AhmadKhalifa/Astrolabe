package com.khalifa.mapViewer.viewmodel.activity

import android.arch.lifecycle.ViewModelProviders
import com.khalifa.mapViewer.ui.activity.MapActivity
import com.khalifa.mapViewer.viewmodel.BaseRxViewModel

/**
 * @author Ahmad Khalifa
 */

class MapActivityViewModel : BaseRxViewModel() {

    companion object {
        @JvmStatic
        fun getInstance(mapActivity: MapActivity): MapActivityViewModel =
                ViewModelProviders
                        .of(mapActivity)
                        .get(MapActivityViewModel::class.java)
    }
}