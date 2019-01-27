package com.khalifa.astrolabe.data.repository.wmsservice.local

import com.khalifa.astrolabe.data.repository.BaseLocalRepository
import com.khalifa.astrolabe.data.storage.room.entity.WMSService

/**
 * @author Ahmad Khalifa
 */

abstract class LocalWMSServicesRepository : BaseLocalRepository() {

    companion object {

        fun getDefault(): LocalWMSServicesRepository = RoomWMSServicesRepository()
    }

    abstract fun addWMSService(vararg wmsService: WMSService)

    abstract fun getWMSServices(): List<WMSService>

    abstract fun getWMSServices(capabilitiesUrl: String): WMSService

    abstract fun deleteWMSService(wmsService: WMSService)
}