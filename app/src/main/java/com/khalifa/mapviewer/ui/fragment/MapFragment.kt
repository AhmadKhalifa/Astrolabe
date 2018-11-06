package com.khalifa.mapViewer.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle

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
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.ScaleBarOverlay
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider
import org.osmdroid.views.overlay.compass.CompassOverlay

/**
 * @author Ahmad Khalifa
 */

class MapFragment :
        BaseFragment<MapViewModel>(),
        LocationListener {

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

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_map, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeMap()
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
        mapView.setTileSource(TileSourceBuilder(MapBaseUrl.OPEN_STREET_MAP).build())
        mapView.setBuiltInZoomControls(true)
        mapView.setMultiTouchControls(true)
        mapView.postDelayed(waitForMapTimeTask, TIME_TO_WAIT_IN_MS.toLong())
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
        mapView.overlays.add(mLocationMarker)

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

    override fun getViewModelInstance() = MapViewModel.getInstance(this)

    override fun handleEvent(event: Event) {}

    override fun handleError(error: Error) {}

    override fun registerLiveDataObservers() {}

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