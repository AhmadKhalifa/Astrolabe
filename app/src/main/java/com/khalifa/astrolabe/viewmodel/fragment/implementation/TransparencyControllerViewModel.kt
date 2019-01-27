package com.khalifa.astrolabe.viewmodel.fragment.implementation

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModelProviders
import com.khalifa.astrolabe.ui.fragment.TransparencyControllerFragment
import com.khalifa.astrolabe.ui.widget.osmdroid.TilesOverlayWithOpacity
import com.khalifa.astrolabe.viewmodel.BaseRxViewModel
import com.khalifa.astrolabe.viewmodel.fragment.ITransparencyControllerViewModel

/**
 * @author Ahmad Khalifa
 */

class TransparencyControllerViewModel : BaseRxViewModel(), ITransparencyControllerViewModel {

    companion object {

        fun getInstance(fragment: TransparencyControllerFragment) =
                ViewModelProviders.of(fragment).get(TransparencyControllerViewModel::class.java)
    }

    override var initialValue: Double = 50.0

    override var isFinalValue = false

    override var isWMS = false

    override val tileOverlay = MutableLiveData<TilesOverlayWithOpacity>()
}