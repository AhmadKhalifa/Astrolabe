package com.khalifa.astrolabe.viewmodel.fragment.implementation

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.Color
import android.support.annotation.DrawableRes
import com.khalifa.astrolabe.AstrolabeApplication
import com.khalifa.astrolabe.R
import com.khalifa.astrolabe.data.model.tileSource.MapSourceFactory
import com.khalifa.astrolabe.ui.activity.MapActivity
import com.khalifa.astrolabe.ui.fragment.MapFragment
import com.khalifa.astrolabe.ui.widget.osmdroid.MapView
import com.khalifa.astrolabe.ui.widget.osmdroid.MiniMapOverlay
import com.khalifa.astrolabe.ui.widget.osmdroid.TilesOverlayWithOpacity
import com.khalifa.astrolabe.util.DimensionsUtil
import com.khalifa.astrolabe.viewmodel.BaseRxViewModel
import com.khalifa.astrolabe.viewmodel.activity.MapActivityViewModel
import com.khalifa.astrolabe.viewmodel.fragment.IMapViewModel
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.MapTileProviderBasic
import org.osmdroid.tileprovider.tilesource.ITileSource
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon
import org.osmdroid.views.overlay.ScaleBarOverlay
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

/**
 * @author Ahmad Khalifa
 */

class MapViewModel : BaseRxViewModel(), IMapViewModel {

    companion object {
        @JvmStatic
        fun getInstance(mapFragment: MapFragment): MapViewModel =
                ViewModelProviders
                        .of(mapFragment)
                        .get(MapViewModel::class.java)

        val DEFAULT_START_POSITION = GeoPoint(30.0592319, 31.2322223)
        const val DEFAULT_OPACITY = 50
        const val DEFAULT_ZOOM_LEVEL = 12.0
        const val TIME_TO_WAIT_IN_MS = 100
    }

    private var miniMapOverlay: MiniMapOverlay? = null
    private var compassOverlay: CompassOverlay? = null
    lateinit var mInfoWindow: MarkerInfoWindow
    var isInDrawingMode = false
    var isDrawingLine = false
    var polygonPointsCount = MutableLiveData<Int>().apply { value = 0 }
    private var polygonGeoPoints = ArrayList<GeoPoint>()
    private var drawingMarkers = ArrayList<Marker>()

    fun addMarker(mapView: MapView,
                  latitude: Double,
                  longitude: Double,
                  @DrawableRes drawable: Int,
                  title: String? = ""): Marker {
        val marker = Marker(mapView)
        marker.position = GeoPoint(latitude, longitude)
        marker.icon = AstrolabeApplication.getDrawable(drawable)
        marker.title = title
        marker.setAnchor(Marker.ANCHOR_CENTER, 1.0f)
        marker.setInfoWindow(mInfoWindow)
        mapView.overlays.add(marker)
        mapView.invalidate()
        return marker
    }

    fun addMapOverlays(context: Context?,
                       mapView: MapView,
                       eventsReceiver: MapEventsReceiver) = with(mapView.overlays) {
        // marker overlay
        val locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), mapView)
        locationOverlay.enableMyLocation()
        mapView.overlays.add(locationOverlay)

        // scale bar overlay
        val scaleBarOverlay = ScaleBarOverlay(mapView)
        scaleBarOverlay.setCentred(true)
        scaleBarOverlay.setScaleBarOffset(
                DimensionsUtil.getScreenWidthInPixels(context) / 2,
                10
        )
        add(scaleBarOverlay)

        // two-finger rotation gesture
        val rotationGestureOverlay = RotationGestureOverlay(mapView)
        rotationGestureOverlay.isEnabled = true
        mapView.setMultiTouchControls(true)
        add(rotationGestureOverlay)

        addCompass(mapView)
        addMiniMap(mapView)

        // click handler overlay
        add(MapEventsOverlay(eventsReceiver))
        Unit
    }

    private fun addMiniMap(mapView: MapView) {
        val context = AstrolabeApplication.instance
        miniMapOverlay = MiniMapOverlay(context, mapView.tileRequestCompleteHandler).also {
            it.width = DimensionsUtil.getScreenWidthInPixels(context) / 5
            it.height = DimensionsUtil.getScreenHeightInPixels(context) / 5
            it.setTileSource(MapSourceFactory.Google.SATELLITE)
        }
        mapView.overlays.add(miniMapOverlay)
    }

    private fun addCompass(mapView: MapView) {
        val context = AstrolabeApplication.instance
        compassOverlay = CompassOverlay(
                context,
                InternalCompassOrientationProvider(context),
                mapView
        ).also { it.enableCompass() }
        mapView.overlays.add(compassOverlay)
    }

    private fun validateMiniMap(mapView: MapView) {
        mapView.overlays.remove(miniMapOverlay)
        addMiniMap(mapView)
    }

    private fun validateCompass(mapView: MapView) {
        mapView.overlays.remove(compassOverlay)
        addCompass(mapView)
    }

    private fun validateOverlays(mapView: MapView) {
        validateMiniMap(mapView)
        validateCompass(mapView)
    }

    fun handleSingleTap(mapView: MapView, geoPoint: GeoPoint?): Boolean {
        if (isInDrawingMode) {
            geoPoint?.let { addNumericMarker(mapView, geoPoint) }
        } else mInfoWindow.close()
        return true
    }

    fun startDrawingMode(mapView: MapView,
                         geoPoint: GeoPoint?,
                         isDrawingLine: Boolean) = geoPoint?.apply {
        isInDrawingMode = true
        this@MapViewModel.isDrawingLine = isDrawingLine
        addNumericMarker(mapView, geoPoint)
    }

    fun addTileSourceLayer(context: Context?,
                           mapView: MapView,
                           tileSource: ITileSource): TilesOverlayWithOpacity {
        val tileProvider = MapTileProviderBasic(context)
        tileProvider.tileSource = tileSource
        val tilesOverlay = TilesOverlayWithOpacity(tileProvider, context, DEFAULT_OPACITY)
        tilesOverlay.loadingBackgroundColor = Color.TRANSPARENT
        tilesOverlay.loadingLineColor = Color.TRANSPARENT
        mapView.overlays.add(tilesOverlay)
        validateOverlays(mapView)
        val mapLayers = MapActivityViewModel.getInstance(context as MapActivity).mapLayers
        val layers = mapLayers.value
        layers?.add(tilesOverlay)
        mapLayers.value = layers
        return tilesOverlay
    }

    private fun addNumericMarker(mapView: MapView,
                                 latitude: Double,
                                 longitude: Double,
                                 number: Int): Marker {
        Marker.ENABLE_TEXT_LABELS_WHEN_NO_IMAGE = true
        val marker = Marker(mapView)
        marker.textLabelBackgroundColor = AstrolabeApplication.getColor(R.color.colorPrimary)
        marker.textLabelForegroundColor = AstrolabeApplication.getColor(R.color.white)
        marker.title = " $number "
        marker.icon = null
        marker.position = GeoPoint(latitude, longitude)
        mapView.overlays.add(marker)
        mapView.invalidate()
        return marker
    }

    fun updateMapLocation(mapView: MapView,
                          latitude: Double,
                          longitude: Double,
                          zoomLevel: Double) {
        val mapController = mapView.controller
        mapController.setCenter(GeoPoint(latitude, longitude))
        mapController.setZoom(zoomLevel)
    }

    fun deleteLayer(mapView: MapView, layer: TilesOverlayWithOpacity) =
            mapView.overlays.remove(layer)

    private fun addNumericMarker(mapView: MapView,
                                 geoPoint: GeoPoint?) = geoPoint?.apply {
        polygonGeoPoints.add(geoPoint)
        polygonPointsCount.value = polygonPointsCount.value!! + 1
        drawingMarkers.add(
                addNumericMarker(mapView, latitude, longitude, polygonPointsCount.value!!)
        )
        if (isDrawingLine && polygonPointsCount.value == 2)
            finishDrawing(mapView)
    }

    fun cancelDrawingMode(mapView: MapView) {
        resetNumericMarkers(mapView)
    }

    fun finishDrawing(mapView: MapView) {
        val polygon = Polygon()
        polygonGeoPoints.add(polygonGeoPoints[0])
        polygon.points = polygonGeoPoints
        mapView.overlays.add(polygon)
        resetNumericMarkers(mapView)
    }

    private fun resetNumericMarkers(mapView: MapView) {
        polygonPointsCount.value = 0
        drawingMarkers.forEach { marker -> mapView.overlayManager.remove(marker) }
        isInDrawingMode = false
        isDrawingLine = false
        drawingMarkers.clear()
        polygonGeoPoints.clear()
    }
}