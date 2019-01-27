package com.khalifa.astrolabe.viewmodel.fragment

import android.arch.lifecycle.MutableLiveData
import com.khalifa.astrolabe.ui.widget.osmdroid.TilesOverlayWithOpacity
import com.khalifa.astrolabe.viewmodel.IViewModel

/**
 * @author Ahmad Khalifa
 */

interface ITransparencyControllerViewModel : IViewModel {

    var initialValue: Double

    var isFinalValue: Boolean

    var isWMS: Boolean

    val tileOverlay: MutableLiveData<TilesOverlayWithOpacity>
}