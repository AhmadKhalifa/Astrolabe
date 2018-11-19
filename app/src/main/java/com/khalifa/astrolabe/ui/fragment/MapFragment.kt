package com.khalifa.astrolabe.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.khalifa.astrolabe.AstrolabeApplication
import com.khalifa.astrolabe.R
import com.khalifa.astrolabe.data.model.tileSource.MapSourceFactory
import com.khalifa.astrolabe.ui.base.BaseFragment
import com.khalifa.astrolabe.ui.widget.MiniMapOverlay
import com.khalifa.astrolabe.ui.widget.pinterest.CircleImageView
import com.khalifa.astrolabe.ui.widget.pinterest.PinterestView
import com.khalifa.astrolabe.util.DimensionsUtil
import com.khalifa.astrolabe.util.PermissionUtil
import com.khalifa.astrolabe.viewmodel.Error
import com.khalifa.astrolabe.viewmodel.Event
import com.khalifa.astrolabe.viewmodel.fragment.implementation.MapViewModel
import kotlinx.android.synthetic.main.fragment_map.*
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.leinardi.android.speeddial.SpeedDialView
import org.osmdroid.tileprovider.MapTileProviderBasic
import org.osmdroid.tileprovider.tilesource.ITileSource
import org.osmdroid.views.overlay.*
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

/**
 * @author Ahmad Khalifa
 */

class MapFragment :
        BaseFragment<MapViewModel>(),
        LocationListener,
        MapEventsReceiver,
        SpeedDialView.OnActionSelectedListener,
        PinterestView.PinMenuClickListener,
        MapSourcesListFragment.OnFragmentInteractionListener {

    companion object {
        val TAG: String = MapFragment::class.java.simpleName

        private val DEFAULT_START_POSITION = GeoPoint(30.0592319, 31.3022223)
        private const val DEFAULT_ZOOM_LEVEL = 12.0
        private const val TIME_TO_WAIT_IN_MS = 100

        fun newInstance() = MapFragment()
    }

    interface OnFragmentInteractionListener

    private val waitForMapTimeTask = object : Runnable {
        override fun run() {
            if (mapView.latitudeSpanDouble == 0.0 || mapView.longitudeSpanDouble == 360000000.0)
                mapView.postDelayed(this, TIME_TO_WAIT_IN_MS.toLong())
        }
    }

    private lateinit var mInfoWindow: MarkerInfoWindow
    var isInDrawingMode = false
    private var isDrawingLine = false
    private var drawingSnackBar: Snackbar? = null
    private var polygonPointsCount = 0
    private var polygonGeoPoints = ArrayList<GeoPoint>()
    private var drawingMarkers = ArrayList<Marker>()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_map, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeMap()
        initializeFloatingActionMenu()
        initializePinterestView()
        floatingActionButton.setOnChangeListener(object : SpeedDialView.OnChangeListener {
            override fun onMainActionSelected(): Boolean {
                finishDrawing()
                return false
            }

            override fun onToggleChanged(isOpen: Boolean) {}
        })
    }

    private fun initializeFloatingActionMenu() = with(floatingActionMenu) {
        fun createActionItem(@IdRes id: Int,
                             @DrawableRes iconResId: Int,
                             @StringRes titleResId: Int) =
                SpeedDialActionItem.Builder(id, iconResId)
                        .setFabBackgroundColor(AstrolabeApplication.getColor(R.color.colorPrimary))
                        .setFabImageTintColor(AstrolabeApplication.getColor(R.color.white))
                        .setLabelColor(AstrolabeApplication.getColor(R.color.white))
                        .setLabelBackgroundColor(AstrolabeApplication.getColor(R.color.colorAccent))
                        .setLabel(AstrolabeApplication.getString(titleResId))
                        .setLabelClickable(true)
                        .create()
        addActionItem(createActionItem(
                R.id.action_show_my_location, R.drawable.ic_my_location_white_24dp, R.string.show_my_location
        ))
        addActionItem(createActionItem(
                R.id.action_select_map_source, R.drawable.ic_map_white_24dp, R.string.select_map_source
        ))
        setOnActionSelectedListener(this@MapFragment)
    }

    private fun initializePinterestView() {
        fun createActionItem(@IdRes id: Int,
                             @DrawableRes iconResId: Int,
                             @StringRes titleResId: Int) = with(CircleImageView(context)) {
            setId(id)
            borderWidth = 0
            scaleType = ImageView.ScaleType.CENTER_CROP
            fillColor = AstrolabeApplication.getColor(R.color.colorPrimary)
            tag = AstrolabeApplication.getString(titleResId)
            setImageResource(iconResId)
            setColorFilter(
                    AstrolabeApplication.getColor(R.color.white),
                    PorterDuff.Mode.SRC_IN
            )
            this
        }
        pinterestView.addMenuItem(
                createActionItem(
                        R.id.action_add_location,
                        R.drawable.ic_add_location_black_24dp,
                        R.string.marker
                ),
                createActionItem(R.id.action_add_line,
                        R.drawable.ic_line_black_24dp,
                        R.string.line
                ),
                createActionItem(
                        R.id.action_add_polygon,
                        R.drawable.ic_polygon_black_24dp,
                        R.string.polygon
                )
        )
        pinterestView.setPinClickListener(this@MapFragment)
        mapView.contextMenu = pinterestView
    }

    override fun onActionSelected(actionItem: SpeedDialActionItem?) = actionItem?.let {
        when (it.id) {
            R.id.action_show_my_location -> showCurrentLocation()
            R.id.action_select_map_source -> MapSourcesListFragment.showFragment(fragmentManager, this@MapFragment)
        }
        false
    } ?: true

    override fun onMenuItemClick(checkedView: View?, clickItemPos: Int) {
        checkedView?.let { view ->
            pinterestView.geoPoint?.let {geoPoint ->
                when(view.id) {
                    R.id.action_add_location ->
                        addMarker(
                                geoPoint.latitude,
                                geoPoint.longitude,
                                R.drawable.ic_place_black_24dp
                        )
                    R.id.action_add_line ->
                        startDrawingMode(geoPoint, true)
                    R.id.action_add_polygon ->
                        startDrawingMode(geoPoint, false)
                    else -> {}
                }
                Unit
            }
        }
    }

    override fun singleTapConfirmedHelper(geoPoint: GeoPoint?): Boolean {
        if (isInDrawingMode) {
            geoPoint?.let { addNumericMarker(it) }
        } else mInfoWindow.close()
        return true
    }

    override fun onTileSourceSelectedAsBaseMap(tileSource: ITileSource) =
            mapView.setTileSource(tileSource)

    override fun onTileSourceSelectedAsLayer(tileSource: ITileSource) =
            addTileSourceLayer(tileSource)

    private fun showCurrentLocation() {

    }

    private fun startDrawingMode(geoPoint: GeoPoint?, isDrawingLine: Boolean) = geoPoint?.apply {
        isInDrawingMode = true
        this@MapFragment.isDrawingLine = isDrawingLine
        drawingSnackBar = Snackbar.make(
                rootView,
                if (isDrawingLine) R.string.drawing_line else R.string.drawing_polygon,
                Snackbar.LENGTH_INDEFINITE
        )
        drawingSnackBar?.setAction(R.string.cancel) { cancelDrawingMode() }
        drawingSnackBar?.setActionTextColor(AstrolabeApplication.getColor(R.color.red))
        snackbar(drawingSnackBar)
        addNumericMarker(geoPoint)
        if (!isDrawingLine) {
            floatingActionButton.visibility = View.VISIBLE
        }
    }

    private fun addNumericMarker(geoPoint: GeoPoint?) = geoPoint?.apply {
        polygonGeoPoints.add(geoPoint)
        drawingMarkers.add(addNumericMarker(latitude, longitude, ++polygonPointsCount))
        if (isDrawingLine && polygonPointsCount == 2)
            finishDrawing()
    }

    fun cancelDrawingMode() {
        resetNumericMarkers()
    }

    private fun finishDrawing() {
        val polygon = Polygon()
        polygonGeoPoints.add(polygonGeoPoints[0])
        polygon.points = polygonGeoPoints
        mapView.overlays.add(polygon)
        resetNumericMarkers()
    }

    private fun resetNumericMarkers() {
        polygonPointsCount = 0
        drawingMarkers.forEach { marker -> mapView.overlayManager.remove(marker) }
        isInDrawingMode = false
        isDrawingLine = false
        drawingMarkers.clear()
        polygonGeoPoints.clear()
        drawingSnackBar?.dismiss()
        floatingActionButton.visibility = View.INVISIBLE
        mapView.invalidate()
    }

    override fun onAnchorViewClick() {

    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        stopUsingGps()
        super.onDestroy()
    }

    private fun initializeMap() {
        mapView.setTileSource(MapSourceFactory.DEFAULT_MAP_SOURCE)
        mapView.setBuiltInZoomControls(true)
        mapView.setMultiTouchControls(true)
        mapView.postDelayed(waitForMapTimeTask, TIME_TO_WAIT_IN_MS.toLong())
        mInfoWindow = MarkerInfoWindow(R.layout.bonuspack_bubble, mapView)
        addMapOverlays()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermissions()
        } else {
            requestLocationUpdates()
        }
        updateMapLocation(
                DEFAULT_START_POSITION.latitude,
                DEFAULT_START_POSITION.longitude,
                DEFAULT_ZOOM_LEVEL
        )
    }

    private fun addTileSourceLayer(tileSource: ITileSource) {
        val tileProvider = MapTileProviderBasic(context)
        tileProvider.tileSource = tileSource
        val tilesOverlay = TilesOverlay(tileProvider, context)
        tilesOverlay.loadingBackgroundColor = Color.TRANSPARENT
        tilesOverlay.loadingLineColor = Color.TRANSPARENT
        mapView.overlays.add(tilesOverlay)
    }

    private fun addMapOverlays() = with(mapView.overlays) {
        // marker overlay
        val locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), mapView)
        locationOverlay.enableMyLocation()
        mapView.overlays.add(locationOverlay)

        // compass overlay
        val compassOverlay =
                CompassOverlay(context, InternalCompassOrientationProvider(context), mapView)
        compassOverlay.enableCompass()
        add(compassOverlay)

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

        // Mini map
        val miniMapOverlay = MiniMapOverlay(context, mapView.tileRequestCompleteHandler)
        miniMapOverlay.width = DimensionsUtil.getScreenWidthInPixels(context) / 5
        miniMapOverlay.height = DimensionsUtil.getScreenHeightInPixels(context) / 5
        miniMapOverlay.setTileSource(MapSourceFactory.Google.SATELLITE)
        mapView.overlays.add(miniMapOverlay)

        // click handler overlay
        add(MapEventsOverlay(this@MapFragment))
        Unit
    }

    private fun checkLocationPermissions() {
        if (PermissionUtil.hasLocationPermissions()) {
            Log.d(TAG, "checkLocationPermissions: Permissions granted")
            requestLocationUpdates()
        } else {
            Log.d(TAG, "checkLocationPermissions: requesting permissions")
            PermissionUtil.requestLocationPermission(this)
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdates() {
        val locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0f,
                this
        )
    }

    @SuppressLint("MissingPermission")
    private fun stopUsingGps() {
        val locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.removeUpdates(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            PermissionUtil.LOCATION_PERMISSIONS_REQUEST_CODE -> {
                for (grantResult in grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        return
                    }
                }
                checkLocationPermissions()
            }
        }
    }

    override fun longPressHelper(geoPoint: GeoPoint?) = geoPoint?.let {
        pinterestView.geoPoint = geoPoint
        true
    } ?: false

    override fun getViewModelInstance() = MapViewModel.getInstance(this)

    override fun onEvent(event: Event) {}

    override fun onError(error: Error) {}

    override fun registerLiveDataObservers() {}

    private fun addMarker(latitude: Double,
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

    private fun addNumericMarker(latitude: Double,
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

    private fun updateMapLocation(latitude: Double, longitude: Double, zoomLevel: Double) {
        val mapController = mapView.controller
        mapController.setCenter(GeoPoint(latitude, longitude))
        mapController.setZoom(zoomLevel)
        stopUsingGps()
    }

    override fun onLocationChanged(location: Location) {
        Log.d(TAG, "onLocationChanged: " + location.toString())
//        updateMapLocation(location.latitude, location.longitude)
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        Log.d(TAG, "onStatusChanged")
    }

    override fun onProviderEnabled(provider: String) {
        Log.d(TAG, "onProviderEnabled")
    }

    override fun onProviderDisabled(provider: String) {
        Log.d(TAG, "onProviderDisabled")
    }
}