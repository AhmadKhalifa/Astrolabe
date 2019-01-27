package com.khalifa.astrolabe.data.repository.wmsservice

import android.arch.lifecycle.MutableLiveData
import com.khalifa.astrolabe.data.repository.wmsservice.local.LocalWMSServicesRepository
import com.khalifa.astrolabe.data.repository.wmsservice.remote.RemoteWMSServicesRepository
import com.khalifa.astrolabe.data.storage.room.converter.InputStreamConverter
import com.khalifa.astrolabe.data.storage.room.entity.WMSService
import com.khalifa.astrolabe.util.InvalidGetCapabilitiesUrl
import org.osmdroid.wms.WMSEndpoint
import org.osmdroid.wms.WMSParser

/**
 * @author Ahmad Khalifa
 */

class WMSServicesRepository private constructor(
        localRepository: LocalWMSServicesRepository,
        remoteRepository: RemoteWMSServicesRepository) :
        BaseWMSServiceRepository(localRepository, remoteRepository) {

    companion object {

        private var sInstance: WMSServicesRepository? = null

        val instance: WMSServicesRepository
            get() {
                if (sInstance == null)
                    sInstance = WMSServicesRepository(
                            LocalWMSServicesRepository.getDefault(),
                            RemoteWMSServicesRepository.getDefault()
                    )
                if (sInstance == null)
                    throw IllegalStateException("Couldn't instantiate WMSServicesRepository")
                return sInstance as WMSServicesRepository
            }
    }

    override fun getAllWMSEndPoints(): MutableLiveData<ArrayList<WMSEndpoint>> {
        if (wmsEndpoints.value == null) {
            wmsEndpoints.postValue(
                    ArrayList<WMSEndpoint>().apply {
                        localRepository.getWMSServices()
                                .forEach { add(WMSParser.parse(it.inputStream)) }
                    }
            )
        }
        return wmsEndpoints
    }

    override fun addWMSService(capabilitiesUrl: String) {
        val (wmsEndpoint, inputStreamString) = remoteRepository.getWMSService(capabilitiesUrl)
        if (wmsEndpoint != null && inputStreamString != null) {
            localRepository.addWMSService(WMSService(
                    capabilitiesUrl,
                    InputStreamConverter.toInputStream(inputStreamString)
            ))
            wmsEndpoints.postValue(wmsEndpoints.value?.apply { add(wmsEndpoint) })
        } else throw InvalidGetCapabilitiesUrl()
    }

    override fun deleteWMSService(wmsService: WMSService) {
        localRepository.deleteWMSService(wmsService)
        val wmsEndpoint: WMSEndpoint? = wmsEndpoints.value?.let { endPoints ->
            endPoints.find { endPoint ->
                WMSParser.parse(wmsService.inputStream).name?.equals(endPoint.name) ?: false
            }
        }
        wmsEndpoint?.run {
            wmsEndpoints.postValue(wmsEndpoints.value?.apply { remove(this@run) })
        }
    }
}