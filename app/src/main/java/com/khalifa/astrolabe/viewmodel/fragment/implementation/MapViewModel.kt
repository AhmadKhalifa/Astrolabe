package com.khalifa.astrolabe.viewmodel.fragment.implementation

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModelProviders
import android.support.annotation.DrawableRes
import android.support.v4.app.FragmentActivity
import com.khalifa.astrolabe.business.MapViewWrapper
import com.khalifa.astrolabe.ui.widget.osmdroid.TilesOverlayWithOpacity
import com.khalifa.astrolabe.ui.widget.osmdroid.WMSOverlayWithOpacity
import com.khalifa.astrolabe.viewmodel.BaseSharedViewModel
import com.khalifa.astrolabe.viewmodel.Event
import com.khalifa.astrolabe.viewmodel.factory.MapViewModelFactory
import com.khalifa.astrolabe.viewmodel.fragment.IMapViewModel
import org.osmdroid.tileprovider.tilesource.ITileSource
import org.osmdroid.util.GeoPoint
import org.osmdroid.wms.WMSEndpoint
import org.osmdroid.wms.WMSLayer

/**
 * @author Ahmad Khalifa
 */

class MapViewModel(override val mapViewWrapper: MapViewWrapper) :
        BaseSharedViewModel(),
        IMapViewModel {

    companion object {
        fun getInstance(fragmentActivity: FragmentActivity, mapViewWrapper: MapViewWrapper) =
                ViewModelProviders
                        .of(fragmentActivity, MapViewModelFactory(mapViewWrapper))
                        .get(MapViewModel::class.java)

        fun getInstance(fragmentActivity: FragmentActivity) =
                ViewModelProviders.of(fragmentActivity)
                        .get(MapViewModel::class.java)
    }

    override var baseMapSource = MutableLiveData<ITileSource>()

    override var mapLayers = MutableLiveData<ArrayList<TilesOverlayWithOpacity>>()
            .apply { value = ArrayList() }

    override var mapWMSLayers = MutableLiveData<ArrayList<WMSOverlayWithOpacity>>()
            .apply { value = ArrayList() }

    override fun addMarker(geoPoint: GeoPoint, @DrawableRes drawable: Int) =
            mapViewWrapper.addMarker(geoPoint.latitude, geoPoint.longitude, drawable, "")

    override fun isInDrawingMode() = mapViewWrapper.isInDrawingMode

    override fun isDrawingLine() = mapViewWrapper.isDrawingLine

    override fun startDrawingMode(geoPoint: GeoPoint?, drawingLine: Boolean) {
        mapViewWrapper.startDrawingMode(geoPoint, drawingLine)
        notify(Event.STARTED_DRAWING)
    }

    override fun cancelDrawingMode() {
        mapViewWrapper.cancelDrawingMode()
        notify(Event.FINISHED_DRAWING)
    }

    override fun finishDrawing() {
        mapViewWrapper.finishDrawing()
        notify(Event.FINISHED_DRAWING)
    }

    override fun setBaseMap(tileSource: ITileSource) {
        mapViewWrapper.setBaseMap(tileSource)
        baseMapSource.value = tileSource
    }

    override fun addMapLayer(tileSource: ITileSource): TilesOverlayWithOpacity {
        val overlay = mapViewWrapper.addMapLayer(tileSource)
        mapLayers.apply { value = value?.apply { add(overlay) } }
        return overlay
    }

    override fun removeMapLayer(tileOverlay: TilesOverlayWithOpacity) {
        mapViewWrapper.removeMapLayer(tileOverlay)
        mapLayers.apply { value = value?.apply { remove(tileOverlay) } }
    }

    override fun addWMSLayer(wmsEndPoint: WMSEndpoint, layerIndex: Int): WMSOverlayWithOpacity? {
        val wmsOverlay = mapViewWrapper.addWMSLayer(wmsEndPoint, layerIndex)
        wmsOverlay?.let { overlay ->
            mapWMSLayers.apply { value = value?.apply { add(overlay) } }
        }
        return wmsOverlay
    }

    override fun removeWMSLayer(wmsLayer: WMSLayer) {
        val wmsOverlay = mapViewWrapper.removeWMSLayer(wmsLayer)
        mapWMSLayers.apply { value = value?.apply { remove(wmsOverlay) } }
    }

    override fun removeWMSLayer(wmsOverlay: WMSOverlayWithOpacity) {
        mapViewWrapper.removeWMSLayer(wmsOverlay)
        mapWMSLayers.apply { value = value?.apply { remove(wmsOverlay) } }
    }

    override fun onCleared() {
        mapViewWrapper.onCleared()
        super.onCleared()
    }
}