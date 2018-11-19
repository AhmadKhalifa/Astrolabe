package com.khalifa.astrolabe.viewmodel.activity

import android.arch.lifecycle.ViewModelProviders
import com.khalifa.astrolabe.ui.activity.MapActivity
import com.khalifa.astrolabe.viewmodel.BaseRxViewModel

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