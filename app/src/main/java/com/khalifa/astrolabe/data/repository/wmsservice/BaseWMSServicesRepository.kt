package com.khalifa.astrolabe.data.repository.wmsservice

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.khalifa.astrolabe.data.repository.Repository
import com.khalifa.astrolabe.data.repository.wmsservice.local.LocalWMSServicesRepository
import com.khalifa.astrolabe.data.repository.wmsservice.remote.RemoteWMSServicesRepository
import com.khalifa.astrolabe.data.storage.room.entity.WMSService
import org.osmdroid.wms.WMSEndpoint

/**
 * @author Ahmad Khalifa
 */

abstract class BaseWMSServicesRepository protected constructor(
        protected val localRepository: LocalWMSServicesRepository,
        protected val remoteRepository: RemoteWMSServicesRepository
) : Repository {

    companion object {

        fun getDefault() =  WMSServicesRepository.instance
    }

    protected val wmsEndpoints = MutableLiveData<ArrayList<WMSEndpoint>>().apply { value = null }

    abstract fun getAllWMSEndServices(): LiveData<List<WMSService>>

    abstract fun addWMSService(capabilitiesUrl: String)

    abstract fun deleteWMSService(wmsService: WMSService)
}