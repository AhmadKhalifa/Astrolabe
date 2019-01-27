package com.khalifa.astrolabe.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
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
import com.khalifa.astrolabe.business.MapViewWrapper
import com.khalifa.astrolabe.util.FragmentNotAttachedException
import com.khalifa.astrolabe.ui.base.BaseFragment
import com.khalifa.astrolabe.ui.widget.osmdroid.TilesOverlayWithOpacity
import com.khalifa.astrolabe.ui.widget.osmdroid.WMSOverlayWithOpacity
import com.khalifa.astrolabe.ui.widget.pinterest.CircleImageView
import com.khalifa.astrolabe.ui.widget.pinterest.PinterestView
import com.khalifa.astrolabe.util.PermissionUtil
import com.khalifa.astrolabe.viewmodel.Error
import com.khalifa.astrolabe.viewmodel.Event
import com.khalifa.astrolabe.viewmodel.fragment.implementation.MapViewModel
import kotlinx.android.synthetic.main.fragment_map.*
import org.osmdroid.util.GeoPoint
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.leinardi.android.speeddial.SpeedDialView

/**
 * @author Ahmad Khalifa
 */

class MapFragment :
        BaseFragment<MapViewModel>(),
        LocationListener,
        SpeedDialView.OnActionSelectedListener,
        AllSourcesFragment.OnFragmentInteractionListener,
        PinterestView.PinMenuClickListener,
        LayersManagerFragment.OnFragmentInteractionListener {

    companion object {
        val TAG: String = MapFragment::class.java.simpleName

        fun newInstance() = MapFragment()
    }

    private var drawingSnackBar: Snackbar? = null

    interface OnFragmentInteractionListener

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_map, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermissions()
        } else {
            requestLocationUpdates()
        }
        viewModel.setBaseMap(MapViewWrapper.DEFAULT_MAP_SOURCE)
        stopUsingGps()
        initializeFloatingActionMenu()
        initializePinterestView()
        floatingActionButton.setOnChangeListener(object : SpeedDialView.OnChangeListener {
            override fun onMainActionSelected(): Boolean {
                viewModel.finishDrawing()
                return false
            }

            override fun onToggleChanged(isOpen: Boolean) {}
        })
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
                R.id.action_select_map_source, R.drawable.ic_map_white_24dp, R.string.all_maps
        ))
        addActionItem(createActionItem(
                R.id.action_layers_manager, R.drawable.ic_layers_white_24dp, R.string.layers_manager
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
            R.id.action_select_map_source -> openAllSourcesFragment()
            R.id.action_layers_manager -> showLayersManagerFragment()
        }
        false
    } ?: true

    override fun onMenuItemClick(checkedView: View?, clickItemPos: Int) {
        checkedView?.let { view ->
            pinterestView.geoPoint?.let { geoPoint ->
                when (view.id) {
                    R.id.action_add_location ->
                        viewModel.addMarker(geoPoint, R.drawable.ic_place_black_24dp)
                    R.id.action_add_line ->
                        startDrawingMode(geoPoint, true)
                    R.id.action_add_polygon ->
                        startDrawingMode(geoPoint, false)
                    else -> {
                    }
                }
                Unit
            }
        }
    }

    private fun startDrawingMode(geoPoint: GeoPoint?, isDrawingLine: Boolean) {
        viewModel.startDrawingMode(geoPoint, isDrawingLine)
    }

    override fun onMapOverlayAdded(mapOverlay: TilesOverlayWithOpacity) {
        showOpacityControllerFragment(mapOverlay)
    }

    override fun onWMSOverlayAdded(wmsOverlay: WMSOverlayWithOpacity) {
        showOpacityControllerFragment(wmsOverlay)
    }

    private fun showOpacityControllerFragment(layer: TilesOverlayWithOpacity) =
            showOpacityControllerFragment(layer, false)

    private fun showOpacityControllerFragment(layer: WMSOverlayWithOpacity) =
            showOpacityControllerFragment(layer, true)

    private fun showOpacityControllerFragment(layer: TilesOverlayWithOpacity, isWMS: Boolean) =
            TransparencyControllerFragment.showFragment(
                    fragmentManager,
                    layer,
                    isWMS,
                    object : TransparencyControllerFragment.OnFragmentInteractionListener {
                        override fun onTransparencyChanged(transparencyPercentage: Int) {
                            layer.transparencyPercentage = transparencyPercentage
                        }
                    }
            )

    private fun openAllSourcesFragment() =
            AllSourcesFragment.showFragment(fragmentManager, this)

    private fun showLayersManagerFragment() =
            LayersManagerFragment.showFragment(fragmentManager, this@MapFragment)

    private fun showCurrentLocation() {

    }

    fun isInDrawingMode() = viewModel.isInDrawingMode()

    fun cancelDrawingMode() {
        viewModel.cancelDrawingMode()
        onDrawingFinished()
    }

    private fun onDrawingFinished() {
    }

    override fun onAnchorViewClick() {}

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

    override fun getViewModelInstance() = activity?.run {
        MapViewModel.getInstance(
            this,
            MapViewWrapper(mapView, object : MapViewWrapper.UICallback {

                override fun onPolygonPointsCountChanged(count: Int, isDrawingLine: Boolean) {
                    if (count == 2 && isDrawingLine) viewModel.finishDrawing()
                }

                override fun onMapClick(geoPoint: GeoPoint?) {

                }

                override fun onMapLongClick(geoPoint: GeoPoint?) = geoPoint?.let {
                    pinterestView.geoPoint = geoPoint
                    true
                } ?: false
            })
        )
    } ?: throw FragmentNotAttachedException()

    override fun onEvent(event: Event) = when (event) {
        Event.STARTED_DRAWING -> {
            drawingSnackBar = Snackbar.make(
                    rootView,
                    if (viewModel.isDrawingLine()) R.string.drawing_line
                    else R.string.drawing_polygon,
                    Snackbar.LENGTH_INDEFINITE
            )
            drawingSnackBar?.setAction(R.string.cancel) { cancelDrawingMode() }
            drawingSnackBar?.setActionTextColor(AstrolabeApplication.getColor(R.color.red))
            snackbar(drawingSnackBar)
            floatingActionButton.visibility =
                    if (viewModel.isDrawingLine()) View.VISIBLE
                    else View.INVISIBLE
        }
        Event.FINISHED_DRAWING -> {
            drawingSnackBar?.dismiss()
            floatingActionButton.visibility = View.INVISIBLE
            mapView.invalidate()
        }
        else -> {
        }
    }

    override fun onError(error: Error) {}

    override fun registerLiveDataObservers() {}

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

    override fun openLayerOpacityAdjustmentScreen(overlay: TilesOverlayWithOpacity) {
        showOpacityControllerFragment(overlay)
    }

    override fun openAllSourcesScreen() {
        openAllSourcesFragment()
    }
}