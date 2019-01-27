package com.khalifa.astrolabe.data.storage.room.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.khalifa.astrolabe.data.storage.room.entity.WMSService

/**
 * @author Ahmad Khalifa
 */

@Dao
interface WMSServicesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addWMSService(vararg wmsService: WMSService)

    @Query("SELECT * FROM WMSService WHERE :capabilitiesUrl = capabilities_url")
    fun getWMSService(capabilitiesUrl: String): WMSService

    @Query("SELECT * FROM WMSService")
    fun getWMSServices(): List<WMSService>

    @Delete
    fun deleteWMSService(wmsService: WMSService)
}