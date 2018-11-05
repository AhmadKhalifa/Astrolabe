package com.khalifa.mapviewer.viewmodel.activity

import android.arch.lifecycle.ViewModelProviders
import com.khalifa.mapviewer.ui.activity.MapActivity
import com.khalifa.mapviewer.viewmodel.BaseRxViewModel

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