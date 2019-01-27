package com.khalifa.astrolabe.data.repository.wmsservice.remote

import com.khalifa.astrolabe.data.storage.room.converter.InputStreamConverter
import com.khalifa.astrolabe.util.InvalidGetCapabilitiesUrl
import org.osmdroid.wms.WMSEndpoint
import org.osmdroid.wms.WMSParser
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

/**
 * @author Ahmad Khalifa
 */

class RetrofitWMSServicesRepository : RemoteWMSServicesRepository() {

    override fun getWMSService(capabilitiesUrl: String) = requireInternet {
        try {
            val connection = URL(capabilitiesUrl).openConnection() as HttpURLConnection?
            val inputStream = connection?.inputStream
            val inputStreamString = InputStreamConverter.toString(inputStream)
            val wmsEndpoint = WMSParser.parse(inputStream)
            inputStream?.close()
            connection?.disconnect()
            Pair(wmsEndpoint as WMSEndpoint, inputStreamString)
        } catch (exception: Exception) {
            throw InvalidGetCapabilitiesUrl()
        }
    }
}