package com.khalifa.astrolabe.data.repository.wmsservice.remote

import com.khalifa.astrolabe.data.storage.room.converter.InputStreamConverter
import com.khalifa.astrolabe.util.InvalidGetCapabilitiesUrl
import org.osmdroid.wms.WMSEndpoint
import org.osmdroid.wms.WMSParser
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
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

            val byteArrayInputStream = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var len = inputStream?.read(buffer)
            while (len ?: -1 > -1) {
                byteArrayInputStream.write(buffer, 0, len?: 0)
                len = inputStream?.read(buffer)
            }
            byteArrayInputStream.flush()
            val inputStreamClone1 = ByteArrayInputStream(byteArrayInputStream.toByteArray())
            val inputStreamClone2 = ByteArrayInputStream(byteArrayInputStream.toByteArray())

            val inputStreamString = InputStreamConverter.toString(inputStreamClone1)
            val wmsEndpoint = WMSParser.parse(inputStreamClone2)
            inputStream?.close()
            connection?.disconnect()
            Pair(wmsEndpoint as WMSEndpoint, inputStreamString)
        } catch (exception: Exception) {
            throw InvalidGetCapabilitiesUrl()
        }
    }
}