package com.khalifa.astrolabe.data.repository.wmsservice.local

import com.khalifa.astrolabe.data.storage.room.AstrolabeDatabase
import com.khalifa.astrolabe.data.storage.room.dao.WMSServicesDao
import com.khalifa.astrolabe.data.storage.room.entity.WMSService

/**
 * @author Ahmad Khalifa
 */

class RoomWMSServicesRepository : LocalWMSServicesRepository() {

    private var _wmsServicesDao: WMSServicesDao? = null

    private val wmsServicesDao: WMSServicesDao
        get() {
            _wmsServicesDao = _wmsServicesDao ?: AstrolabeDatabase.instance.wmsServicesDao()
            return if (_wmsServicesDao != null) _wmsServicesDao as WMSServicesDao
            else throw IllegalStateException(
                    "AstrolabeDatabase.instance.wmsServicesDao() " +
                            "implementation returns null!"
            )
        }

    override fun addWMSService(vararg wmsService: WMSService) =
            wmsServicesDao.addWMSService(*wmsService)

    override fun getWMSServices() =
            wmsServicesDao.getWMSServices()

    override fun getWMSServices(capabilitiesUrl: String) =
            wmsServicesDao.getWMSService(capabilitiesUrl)

    override fun deleteWMSService(wmsService: WMSService) =
            wmsServicesDao.deleteWMSService(wmsService)
}