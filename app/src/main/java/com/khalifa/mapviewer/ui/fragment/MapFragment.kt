package com.khalifa.mapviewer.ui.fragment

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
import com.khalifa.mapviewer.R
import com.khalifa.mapviewer.ui.base.BaseFragment
import com.khalifa.mapviewer.util.PermissionUtil
import com.khalifa.mapviewer.viewmodel.Error
import com.khalifa.mapviewer.viewmodel.Event
import com.khalifa.mapviewer.viewmodel.fragment.map.MapViewModel

/**
 * @author Ahmad Khalifa
 */

class MapFragment :
        BaseFragment<MapViewModel>(),
        LocationListener {

    companion object {
        val TAG: String = MapFragment::class.java.simpleName

        fun newInstance() = MapFragment()
    }

    interface OnFragmentInteractionListener

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_map, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        initializeMap()
    }

    private fun initializeMap() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermissions()
        } else {
            requestLocationUpdates()
        }
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

    override fun handleEvent(event: Event) {

    }

    override fun handleError(error: Error) {

    }

    override fun registerLiveDataObservers() {

    }

    private fun updateMapLocation(latitude: Double, longitude: Double) {
//        val locationPoint = GeoPoint(latitude, longitude)
//        val marker = Marker(mMapView)
//        marker.setIcon(getDrawable(R.drawable.ic_my_location))
//        marker.setPosition(locationPoint)
//        mMapView.getOverlays().add(marker)
//        val mapController = mMapView.getController()
//        mapController.setCenter(GeoPoint(latitude, longitude))
//        mapController.setZoom(DEFAULT_ZOOM_LEVEL)
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