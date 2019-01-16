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
        @JvmStatic
        fun getInstance(fragment: TransparencyControllerFragment): TransparencyControllerViewModel =
                ViewModelProviders
                        .of(fragment)
                        .get(TransparencyControllerViewModel::class.java)
    }

    var initialValue = 50f

    var isFinalValue = false

    val tileOverlay = MutableLiveData<TilesOverlayWithOpacity>()
}