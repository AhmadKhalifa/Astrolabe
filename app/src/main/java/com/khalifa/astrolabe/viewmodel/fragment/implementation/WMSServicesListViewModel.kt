package com.khalifa.astrolabe.viewmodel.fragment.implementation

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModelProviders
import com.khalifa.astrolabe.ui.fragment.WMSServicesListFragment
import com.khalifa.astrolabe.viewmodel.BaseRxViewModel
import com.khalifa.astrolabe.viewmodel.Error
import com.khalifa.astrolabe.viewmodel.fragment.IWMSServicesListViewModel
import org.osmdroid.wms.WMSEndpoint
import org.osmdroid.wms.WMSParser
import org.osmdroid.wms.WMSTileSource
import java.net.HttpURLConnection
import java.net.URL

/**
 * @author Ahmad Khalifa
 */

class WMSServicesListViewModel : BaseRxViewModel(), IWMSServicesListViewModel {

    companion object {
        @JvmStatic
        fun getInstance(wmsServicesListFragment: WMSServicesListFragment): WMSServicesListViewModel =
                ViewModelProviders
                        .of(wmsServicesListFragment)
                        .get(WMSServicesListViewModel::class.java)
    }

    val wmsEndpoints = MutableLiveData<ArrayList<WMSEndpoint>>()

    fun loadWMSEndpoints() {
        val endPoints = ArrayList<WMSEndpoint>()
        val capabilitiesUrl = "http://ows.terrestris.de/osm/service?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetCapabilities"
        performAsync(
                action = {
                    val connection = URL(capabilitiesUrl).openConnection() as HttpURLConnection?
                    val inputStream = connection?.inputStream
                    val wmsEndpoint = WMSParser.parse(inputStream)
                    inputStream?.close()
                    connection?.disconnect()
                    wmsEndpoint as WMSEndpoint
                },
                onSuccess = { wmsEndPoint ->
                    if (wmsEndPoint == null) return@performAsync
                    endPoints.add(wmsEndPoint)
                    wmsEndpoints.value = endPoints
                },
                onFailure = { notify(Error.ERROR_LOADING_WMS_CAPABILITIES) }
        )
    }

    fun deleteWMSService(wmsEndpoint: WMSEndpoint) {
        wmsEndpoints.value?.run {
            remove(wmsEndpoint)
            wmsEndpoints.value = this
        }
    }
}