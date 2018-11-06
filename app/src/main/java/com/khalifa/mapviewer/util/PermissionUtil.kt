package com.khalifa.mapViewer.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import com.khalifa.mapViewer.MapApplication

/**
 * @author Ahmad Khalifa
 */

class PermissionUtil private constructor() {
    companion object {

        const val LOCATION_PERMISSIONS_REQUEST_CODE = 1234

        private val LOCATION_PERMISSIONS = arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        )

        fun hasPermissions(permissions: Array<String>): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                for (permission in permissions) {
                    if (ActivityCompat.checkSelfPermission(MapApplication.instace, permission)
                            != PackageManager.PERMISSION_GRANTED) {
                        return false
                    }
                }
            }
            return true
        }

        private fun requestPermissions(fragment: Fragment,
                                       permissions: Array<String>,
                                       requestCode: Int) =
                fragment.requestPermissions(permissions, requestCode)

        private fun requestPermissions(activity: Activity,
                                       permissions: Array<String>,
                                       requestCode: Int) =
                ActivityCompat.requestPermissions(activity, permissions, requestCode)

        fun hasLocationPermissions() = hasPermissions(LOCATION_PERMISSIONS)

        fun requestLocationPermission(activity: Activity) = requestPermissions(
                activity,
                LOCATION_PERMISSIONS,
                LOCATION_PERMISSIONS_REQUEST_CODE
        )

        fun requestLocationPermission(fragment: Fragment)  = requestPermissions(
                fragment,
                LOCATION_PERMISSIONS,
                LOCATION_PERMISSIONS_REQUEST_CODE
        )
    }
}