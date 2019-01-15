package com.khalifa.astrolabe.viewmodel.fragment.implementation

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModelProviders
import com.khalifa.astrolabe.data.model.tileSource.MapSourceFactory
import com.khalifa.astrolabe.ui.fragment.LayersManagerFragment
import com.khalifa.astrolabe.viewmodel.BaseRxViewModel
import com.khalifa.astrolabe.viewmodel.fragment.ILayersManagerViewModel

/**
 * @author Ahmad Khalifa
 */

class LayerManagerViewModel : BaseRxViewModel(), ILayersManagerViewModel {

    companion object {
        @JvmStatic
        fun getInstance(layersManagerFragment: LayersManagerFragment): LayerManagerViewModel =
                ViewModelProviders
                        .of(layersManagerFragment)
                        .get(LayerManagerViewModel::class.java)
    }

    val mapSources = MutableLiveData<ArrayList<MapSourceFactory.MapSource>>()

    fun loadMapSources() {
        mapSources.value = MapSourceFactory.onlineMapSources
    }
}