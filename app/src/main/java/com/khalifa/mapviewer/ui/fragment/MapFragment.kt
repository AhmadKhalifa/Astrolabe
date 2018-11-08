package com.khalifa.mapViewer.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.annotation.IdRes
import android.support.annotation.StringRes

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.khalifa.mapViewer.MapApplication
import com.khalifa.mapViewer.R
import com.khalifa.mapViewer.data.model.tileSource.MapBaseUrl
import com.khalifa.mapViewer.data.model.tileSource.TileSourceBuilder
import com.khalifa.mapViewer.ui.base.BaseFragment
import com.khalifa.mapViewer.util.DimensionsUtil
import com.khalifa.mapViewer.util.PermissionUtil
import com.khalifa.mapViewer.viewmodel.Error
import com.khalifa.mapViewer.viewmodel.Event
import com.khalifa.mapViewer.viewmodel.fragment.map.MapViewModel
import kotlinx.android.synthetic.main.fragment_map.*
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.views.overlay.*
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow
import org.osmdroid.views.overlay.Marker
import android.support.v4.content.res.ResourcesCompat
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.leinardi.android.speeddial.SpeedDialView

/**
 * @author Ahmad Khalifa
 */

class MapFragment :
        BaseFragment<MapViewModel>(),
        LocationListener,
        MapEventsReceiver,
        SpeedDialView.OnActionSelectedListener {

    companion object {
        val TAG: String = MapFragment::class.java.simpleName

        private const val DEFAULT_ZOOM_LEVEL = 14.0
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

    private lateinit var mLocationMarker: Marker

    private lateinit var mInfoWindow: MarkerInfoWindow

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_map, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeMap()
        initializeFloatingActionMenu()
    }

    private fun initializeFloatingActionMenu() = with(floatingActionMenu) {
        fun createActionItem(@IdRes id: Int,
                             @DrawableRes iconResId: Int,
                             @StringRes titleResId: Int) =
                SpeedDialActionItem.Builder(id, iconResId)
                        .setFabBackgroundColor(MapApplication.getColor(R.color.colorPrimary))
                        .setFabImageTintColor(MapApplication.getColor(R.color.white))
                        .setLabelColor(MapApplication.getColor(R.color.white))
                        .setLabelBackgroundColor(MapApplication.getColor(R.color.colorAccent))
                        .setLabel(MapApplication.getString(titleResId))
                        .setLabelClickable(true)
                        .create()
        addActionItem(createActionItem(
                R.id.action1, R.drawable.ic_edit_black_24dp, R.string.action1_title
        ))
        addActionItem(createActionItem(
                R.id.action2, R.drawable.ic_edit_black_24dp, R.string.action2_title
        ))
        addActionItem(createActionItem(
                R.id.action3, R.drawable.ic_edit_black_24dp, R.string.action3_title
        ))
        setOnActionSelectedListener(this@MapFragment)
    }

    override fun onActionSelected(actionItem: SpeedDialActionItem?) = actionItem?.let {
        when (it.id) {
            R.id.action0 -> {
                snackbar("Action 0")
            }
            R.id.action1 -> {
                snackbar("Action 1")
            }
            R.id.action2 -> {
                snackbar("Action 2")
            }
            R.id.action3 -> {
                snackbar("Action 3")
            }
        }
        false
    } ?: true

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
        mapView.setTileSource(TileSourceBuilder(MapBaseUrl.OPEN_STREET_MAP).build())
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
    }

    private fun addMapOverlays() = with(mapView.overlays) {
        // marker overlay
        mLocationMarker = Marker(mapView)
        mLocationMarker.icon = MapApplication.getDrawable(R.drawable.ic_my_location)
        add(mLocationMarker)

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

    override fun singleTapConfirmedHelper(geoPoint: GeoPoint?) = geoPoint?.let {
        mInfoWindow.close()
        true
    } ?: false

    override fun longPressHelper(geoPoint: GeoPoint?) = geoPoint?.let {
        addMarker(it.latitude, it.longitude, R.drawable.ic_my_location, "marker")
        true
    } ?: false

    override fun getViewModelInstance() = MapViewModel.getInstance(this)

    override fun onEvent(event: Event) {}

    override fun onError(error: Error) {}

    override fun registerLiveDataObservers() {}

    private fun addMarker(latitude: Double,
                          longitude: Double,
                          @DrawableRes drawable: Int,
                          title: String? = "") {
        val marker = Marker(mapView)
        marker.position = GeoPoint(latitude, longitude)
        marker.icon = MapApplication.getDrawable(drawable)
        marker.title = title
        marker.setAnchor(Marker.ANCHOR_CENTER, 1.0f)
        marker.setInfoWindow(mInfoWindow)
        mapView.overlays.add(marker)
        mapView.invalidate()
    }

    private fun updateMarker(locationPoint: GeoPoint) {
        mapView.overlayManager.remove(mLocationMarker)
        mLocationMarker.position = locationPoint
        mapView.overlays.add(mLocationMarker)
    }

    private fun updateMapLocation(latitude: Double, longitude: Double) {
        updateMarker(GeoPoint(latitude, longitude))
        val mapController = mapView.controller
        mapController.setCenter(GeoPoint(latitude, longitude))
        mapController.setZoom(DEFAULT_ZOOM_LEVEL)
        stopUsingGps()
    }

    override fun onLocationChanged(location: Location) {
        Log.d(TAG, "onLocationChanged: " + location.toString())
        updateMapLocation(location.latitude, location.longitude)
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