package com.khalifa.astrolabe.viewmodel.fragment.implementation

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModelProviders
import com.khalifa.astrolabe.ui.fragment.WMSServicesListFragment
import com.khalifa.astrolabe.viewmodel.BaseRxViewModel
import com.khalifa.astrolabe.viewmodel.Error
import com.khalifa.astrolabe.viewmodel.fragment.IWMSServicesListViewModel
import org.osmdroid.wms.WMSEndpoint
import org.osmdroid.wms.WMSParser
import java.io.BufferedInputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * @author Ahmad Khalifa
 */

class WMSServicesListViewModel : BaseRxViewModel(), IWMSServicesListViewModel {

    companion object {

        fun getInstance(wmsServicesListFragment: WMSServicesListFragment) =
                ViewModelProviders.of(wmsServicesListFragment)
                        .get(WMSServicesListViewModel::class.java)
    }

    override val wmsEndpoints = MutableLiveData<ArrayList<WMSEndpoint>>()

    override fun loadWMSEndpoints() {
        val endPoints = ArrayList<WMSEndpoint>()
        val capabilitiesUrl = "http://ows.terrestris.de/osm/service?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetCapabilities"
        performAsync(
                action = {

                    fun toString(inputStream: InputStream?): String {
                        val bis = BufferedInputStream(inputStream)
                        val buf = ByteArrayOutputStream()
                        var result = bis.read()
                        while (result != -1) {
                            buf.write(result.toByte().toInt())
                            result = bis.read()
                        }
                        return buf.toString("UTF-8")
                    }

                    fun toInputStream(string: String?): InputStream =
                            ByteArrayInputStream(string?.toByteArray(Charsets.UTF_8))

                    val connection = URL(capabilitiesUrl).openConnection() as HttpURLConnection?
                    val inputStream = connection?.inputStream
                    val _inputStream = toInputStream(toString(connection?.inputStream))
                    val wmsEndpoint = WMSParser.parse(_inputStream)
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

    override fun deleteWMSService(wmsEndpoint: WMSEndpoint) {
        wmsEndpoints.value?.run {
            remove(wmsEndpoint)
            wmsEndpoints.value = this
        }
    }
}