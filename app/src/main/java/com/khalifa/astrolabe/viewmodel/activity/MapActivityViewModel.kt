package com.khalifa.astrolabe.viewmodel.activity

import android.arch.lifecycle.ViewModelProviders
import com.khalifa.astrolabe.ui.activity.MapActivity
import com.khalifa.astrolabe.viewmodel.BaseRxViewModel
import com.khalifa.astrolabe.viewmodel.activity.implementation.IMapActivityViewModel

/**
 * @author Ahmad Khalifa
 */

class MapActivityViewModel : BaseRxViewModel(), IMapActivityViewModel {

    companion object {

        fun getInstance(mapActivity: MapActivity) =
                ViewModelProviders.of(mapActivity).get(MapActivityViewModel::class.java)
    }
}