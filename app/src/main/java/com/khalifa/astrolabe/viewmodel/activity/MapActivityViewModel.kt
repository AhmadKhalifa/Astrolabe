package com.khalifa.astrolabe.viewmodel.activity

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModelProviders
import com.khalifa.astrolabe.ui.activity.MapActivity
import com.khalifa.astrolabe.ui.widget.osmdroid.TilesOverlayWithOpacity
import com.khalifa.astrolabe.viewmodel.BaseRxViewModel
import org.osmdroid.tileprovider.tilesource.ITileSource

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

    var baseMapSource = MutableLiveData<ITileSource>()

    var mapLayers = MutableLiveData<ArrayList<TilesOverlayWithOpacity>>().apply {
        value = ArrayList()
    }
}