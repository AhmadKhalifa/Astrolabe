package com.khalifa.astrolabe.data.repository.wmsservice.remote

import com.khalifa.astrolabe.AstrolabeApplication
import com.khalifa.astrolabe.data.repository.BaseRemoteRepository
import com.khalifa.astrolabe.util.InternetConnectionChecker
import org.osmdroid.wms.WMSEndpoint

/**
 * @author Ahmad Khalifa
 */

abstract class RemoteWMSServicesRepository : BaseRemoteRepository(
        InternetConnectionChecker(AstrolabeApplication.instance),
        null
) {

    companion object {

        fun getDefault(): RemoteWMSServicesRepository = RetrofitWMSServicesRepository()
    }

    abstract fun getWMSService(capabilitiesUrl: String): Pair<WMSEndpoint?, String?>
}