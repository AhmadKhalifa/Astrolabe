package com.khalifa.astrolabe.viewmodel.fragment.implementation

import android.arch.lifecycle.ViewModelProviders
import com.khalifa.astrolabe.ui.fragment.LayersManagerFragment
import com.khalifa.astrolabe.viewmodel.BaseRxViewModel
import com.khalifa.astrolabe.viewmodel.fragment.ILayersManagerViewModel

/**
 * @author Ahmad Khalifa
 */

class LayerManagerViewModel : BaseRxViewModel(), ILayersManagerViewModel {

    companion object {

        fun getInstance(layersManagerFragment: LayersManagerFragment) =
                ViewModelProviders.of(layersManagerFragment).get(LayerManagerViewModel::class.java)
    }
}