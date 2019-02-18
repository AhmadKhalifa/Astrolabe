package com.khalifa.astrolabe.business

import android.graphics.Color
import android.support.annotation.DrawableRes
import com.khalifa.astrolabe.AstrolabeApplication
import com.khalifa.astrolabe.R
import com.khalifa.astrolabe.data.factory.MapSourceFactory
import com.khalifa.astrolabe.ui.widget.osmdroid.MapView
import com.khalifa.astrolabe.ui.widget.osmdroid.MiniMapOverlay
import com.khalifa.astrolabe.ui.widget.osmdroid.TilesOverlayWithOpacity
import com.khalifa.astrolabe.ui.widget.osmdroid.WMSOverlayWithOpacity
import com.khalifa.astrolabe.util.DimensionsUtil
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
import org.osmdroid.wms.WMSEndpoint
import org.osmdroid.wms.WMSLayer
import org.osmdroid.wms.WMSTileSource
import kotlin.properties.Delegates

class MapViewWrapper(private var mapView: MapView?) : MapEventsReceiver {

    companion object {

        val DEFAULT_MAP_SOURCE = MapSourceFactory.HereWeGo.DAY
        val DEFAULT_MINI_MAP_SOURCE = MapSourceFactory.Google.SATELLITE
        val DEFAULT_START_POSITION = GeoPoint(30.0592319, 31.2322223)
        const val DEFAULT_OPACITY = 50
        const val DEFAULT_ZOOM_LEVEL = 12.0
        const val TIME_TO_WAIT_IN_MS = 100
    }

    private val waitForMapTimeTask = object : Runnable {
        override fun run() {
            if (mapView?.latitudeSpanDouble == 0.0 || mapView?.longitudeSpanDouble == 360000000.0)
                mapView?.postDelayed(this, TIME_TO_WAIT_IN_MS.toLong())
        }
    }
    private var miniMapOverlay: MiniMapOverlay? = null
    private var compassOverlay: CompassOverlay? = null
    private var infoWindow: MarkerInfoWindow
    var isInDrawingMode = false
    var isDrawingLine = false
    private var polygonGeoPoints = ArrayList<GeoPoint>()
    private var drawingMarkers = ArrayList<Marker>()

    private var polygonPointsCount: Int by Delegates.observable(0) {
        _, _, newCount -> uiCallback?.onPolygonPointsCountChanged(newCount, isDrawingLine)
    }

    private var uiCallback: UICallback? = null

    interface UICallback {

        fun onPolygonPointsCountChanged(count: Int, isDrawingLine: Boolean)

        fun onMapClick(geoPoint: GeoPoint?)

        fun onMapLongClick(geoPoint: GeoPoint?): Boolean
    }

    init {
        Marker.ENABLE_TEXT_LABELS_WHEN_NO_IMAGE = true
        mapView?.run {
            setBuiltInZoomControls(true)
            setMultiTouchControls(true)
            postDelayed(waitForMapTimeTask, TIME_TO_WAIT_IN_MS.toLong())
        }
        infoWindow = MarkerInfoWindow(R.layout.bonuspack_bubble, mapView)
        addMapOverlays()
        showDefaultLocation()
    }

    constructor(mapView: MapView?, uiCallback: UICallback) : this(mapView) {
        this.uiCallback = uiCallback
    }

    private fun showDefaultLocation() {
        updateMapLocation(
                DEFAULT_START_POSITION.latitude,
                DEFAULT_START_POSITION.longitude,
                DEFAULT_ZOOM_LEVEL
        )
    }

    fun setBaseMap(tileSource: ITileSource) = mapView?.setTileSource(tileSource)

    fun addMarker(latitude: Double, longitude: Double,
                  @DrawableRes drawable: Int,
                  title: String? = "") = Marker(mapView).also { marker ->
        marker.position = GeoPoint(latitude, longitude)
        marker.icon = AstrolabeApplication.getDrawable(drawable)
        marker.title = title
        marker.setAnchor(Marker.ANCHOR_CENTER, 1.0f)
        marker.infoWindow = this@MapViewWrapper.infoWindow
        mapView?.overlays?.add(marker)
        mapView?.invalidate()
    }

    private fun addMapOverlays() = mapView?.run {
        val context = AstrolabeApplication.instance

        // marker overlay
        val locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), this)
        locationOverlay.enableMyLocation()
        overlays.add(locationOverlay)

        // scale bar overlay
        val scaleBarOverlay = ScaleBarOverlay(this)
        scaleBarOverlay.setCentred(true)
        scaleBarOverlay.setScaleBarOffset(
                DimensionsUtil.getScreenWidthInPixels(context) / 2,
                10
        )
        overlays.add(scaleBarOverlay)

        // two-finger rotation gesture
        val rotationGestureOverlay = RotationGestureOverlay(this)
        rotationGestureOverlay.isEnabled = true
        setMultiTouchControls(true)
        overlays.add(rotationGestureOverlay)

        addCompass()
        addMiniMap()

        // click handler overlay
        overlays.add(MapEventsOverlay(this@MapViewWrapper))
    }

    private fun addMiniMap() = mapView?.run {
        miniMapOverlay = MiniMapOverlay(context, tileRequestCompleteHandler).also {
            it.width = DimensionsUtil.getScreenWidthInPixels(context) / 5
            it.height = DimensionsUtil.getScreenHeightInPixels(context) / 5
            it.setTileSource(DEFAULT_MINI_MAP_SOURCE)
            miniMapOverlay = it
        }
        overlays.add(miniMapOverlay)
    }

    private fun addCompass() = mapView?.run {
        compassOverlay = CompassOverlay(
                context,
                InternalCompassOrientationProvider(context),
                this
        ).also { it.enableCompass() }
        overlays.add(compassOverlay)
    }

    private fun validateMiniMap() {
        mapView?.overlays?.remove(miniMapOverlay)
        addMiniMap()
    }

    private fun validateCompass() {
        mapView?.overlays?.remove(compassOverlay)
        addCompass()
    }

    private fun validateOverlays() {
        validateMiniMap()
        validateCompass()
    }

    private fun addNumericMarker(latitude: Double, longitude: Double, number: Int): Marker {
        val marker = Marker(mapView)
        marker.textLabelBackgroundColor = AstrolabeApplication.getColor(R.color.colorPrimary)
        marker.textLabelForegroundColor = AstrolabeApplication.getColor(R.color.white)
        marker.title = " $number "
        marker.icon = null
        marker.position = GeoPoint(latitude, longitude)
        mapView?.overlays?.add(marker)
        mapView?.invalidate()
        return marker
    }

    private fun addNumericMarker(geoPoint: GeoPoint?) = geoPoint?.apply {
        polygonGeoPoints.add(this)
        polygonPointsCount++
        drawingMarkers.add(
                addNumericMarker(latitude, longitude, polygonPointsCount)
        )
        if (isDrawingLine && polygonPointsCount == 2)
            finishDrawing()
    }

    fun startDrawingMode(geoPoint: GeoPoint?,
                         isDrawingLine: Boolean) = geoPoint?.apply {
        isInDrawingMode = true
        this@MapViewWrapper.isDrawingLine = isDrawingLine
        addNumericMarker(geoPoint)
    }

    fun cancelDrawingMode() {
        resetNumericMarkers()
    }

    fun finishDrawing() {
        val polygon = Polygon()
        polygonGeoPoints.add(polygonGeoPoints[0])
        polygon.points = polygonGeoPoints
        mapView?.overlays?.add(polygon)
        resetNumericMarkers()
    }

    private fun resetNumericMarkers() {
        polygonPointsCount = 0
        drawingMarkers.forEach { marker -> mapView?.overlayManager?.remove(marker) }
        isInDrawingMode = false
        isDrawingLine = false
        drawingMarkers.clear()
        polygonGeoPoints.clear()
    }

    private fun handleSingleTap(geoPoint: GeoPoint?): Boolean {
        uiCallback?.onMapClick(geoPoint)
        if (isInDrawingMode) {
            geoPoint?.let(::addNumericMarker)
        } else infoWindow.close()
        return true
    }

    private fun updateMapLocation(latitude: Double,
                                  longitude: Double,
                                  zoomLevel: Double) {
        val mapController = mapView?.controller
        mapController?.setCenter(GeoPoint(latitude, longitude))
        mapController?.setZoom(zoomLevel)
    }

    fun addMapLayer(tileSource: ITileSource): TilesOverlayWithOpacity {
        val context = mapView?.context
        val tileProvider = MapTileProviderBasic(context)
        tileProvider.tileSource = tileSource
        val tilesOverlay = TilesOverlayWithOpacity(tileProvider, context, DEFAULT_OPACITY)
        tilesOverlay.loadingBackgroundColor = Color.TRANSPARENT
        tilesOverlay.loadingLineColor = Color.TRANSPARENT
        mapView?.overlays?.add(tilesOverlay)
        validateOverlays()
        return tilesOverlay
    }

    fun removeMapLayer(layer: TilesOverlayWithOpacity) =
            mapView?.overlays?.remove(layer)

    fun addWMSLayer(wmsEndPoint: WMSEndpoint, layerIndex: Int): WMSOverlayWithOpacity? {
        val tileSource = WMSTileSource.createFrom(wmsEndPoint, wmsEndPoint.layers[layerIndex])
        val layer = wmsEndPoint.layers?.get(layerIndex) ?: return null
        layer.bbox?.run { mapView?.zoomToBoundingBox(this, true) }
        val context = mapView?.context
        val tileProvider = MapTileProviderBasic(context)
        tileProvider.tileSource = tileSource
        val wmsOverlay = WMSOverlayWithOpacity(tileProvider, context, layer, DEFAULT_OPACITY)
        wmsOverlay.loadingBackgroundColor = Color.TRANSPARENT
        wmsOverlay.loadingLineColor = Color.TRANSPARENT
        mapView?.overlays?.add(wmsOverlay)
        validateOverlays()
        return wmsOverlay
    }

    fun removeWMSLayer(wmsLayer: WMSLayer): WMSOverlayWithOpacity {
        val wmsOverlay =  mapView?.overlays?.first { overlay ->
            overlay is WMSOverlayWithOpacity && overlay.wmsLayer.name == wmsLayer.name
        }
        mapView?.overlays?.remove(wmsOverlay)
        mapView?.invalidate()
        return wmsOverlay as WMSOverlayWithOpacity
    }

    fun removeWMSLayer(wmsLayer: WMSOverlayWithOpacity) =
            mapView?.overlays?.remove(wmsLayer)

    fun onCleared() {
        mapView = null
    }

    override fun longPressHelper(geoPoint: GeoPoint?) =
            uiCallback?.onMapLongClick(geoPoint!!) ?: false

    override fun singleTapConfirmedHelper(geoPoint: GeoPoint?) = handleSingleTap(geoPoint)
}
