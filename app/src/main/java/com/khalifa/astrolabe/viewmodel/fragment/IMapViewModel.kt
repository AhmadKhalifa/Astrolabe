package com.khalifa.astrolabe.viewmodel.fragment

import android.arch.lifecycle.MutableLiveData
import android.support.annotation.DrawableRes
import com.khalifa.astrolabe.business.MapViewWrapper
import com.khalifa.astrolabe.ui.widget.osmdroid.TilesOverlayWithOpacity
import com.khalifa.astrolabe.ui.widget.osmdroid.WMSOverlayWithOpacity
import com.khalifa.astrolabe.viewmodel.IViewModel
import org.osmdroid.tileprovider.tilesource.ITileSource
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import org.osmdroid.wms.WMSEndpoint
import org.osmdroid.wms.WMSLayer

/**
 * @author Ahmad Khalifa
 */

interface IMapViewModel : IViewModel {

    val mapViewWrapper: MapViewWrapper
    val baseMapSource: MutableLiveData<ITileSource>
    val mapLayers: MutableLiveData<ArrayList<TilesOverlayWithOpacity>>
    val mapWMSLayers: MutableLiveData<ArrayList<WMSOverlayWithOpacity>>

    fun addMarker(geoPoint: GeoPoint, @DrawableRes drawable: Int): Marker

    fun isInDrawingMode(): Boolean

    fun isDrawingLine(): Boolean

    fun startDrawingMode(geoPoint: GeoPoint?, drawingLine: Boolean)

    fun cancelDrawingMode()

    fun finishDrawing()

    fun setBaseMap(tileSource: ITileSource)

    fun addMapLayer(tileSource: ITileSource): TilesOverlayWithOpacity

    fun removeMapLayer(tileOverlay: TilesOverlayWithOpacity)

    fun addWMSLayer(wmsEndPoint: WMSEndpoint, layerIndex: Int): WMSOverlayWithOpacity?

    fun removeWMSLayer(wmsLayer: WMSLayer)

    fun removeWMSLayer(wmsOverlay: WMSOverlayWithOpacity)
}