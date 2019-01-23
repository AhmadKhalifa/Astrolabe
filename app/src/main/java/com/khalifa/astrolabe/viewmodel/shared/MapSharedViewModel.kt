package com.khalifa.astrolabe.viewmodel.shared

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.khalifa.astrolabe.business.MapViewWrapper
import com.khalifa.astrolabe.ui.fragment.MapFragment
import com.khalifa.astrolabe.ui.widget.osmdroid.MapView
import com.khalifa.astrolabe.ui.widget.osmdroid.TilesOverlayWithOpacity
import com.khalifa.astrolabe.ui.widget.osmdroid.WMSOverlayWithOpacity
import com.khalifa.astrolabe.viewmodel.BaseSharedViewModel
import com.khalifa.astrolabe.viewmodel.Error
import org.osmdroid.tileprovider.tilesource.ITileSource
import org.osmdroid.wms.WMSEndpoint
import org.osmdroid.wms.WMSLayer
import org.osmdroid.wms.WMSParser
import org.osmdroid.wms.WMSTileSource
import java.net.HttpURLConnection
import java.net.URL

/**
 * @author Ahmad Khalifa
 */

class MapSharedViewModel : BaseSharedViewModel() {

    companion object {

        fun getInstance(fragmentActivity: FragmentActivity, mapViewWrapper: MapViewWrapper) =
                ViewModelProviders.of(fragmentActivity).get(MapSharedViewModel::class.java).also {
                    viewModel -> viewModel.mapViewWrapper = mapViewWrapper
                }

        fun getInstance(fragmentActivity: FragmentActivity) =
                ViewModelProviders.of(fragmentActivity).get(MapSharedViewModel::class.java)
    }

    lateinit var mapViewWrapper: MapViewWrapper

}