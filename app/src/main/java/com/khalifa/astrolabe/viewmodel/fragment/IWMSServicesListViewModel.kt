package com.khalifa.astrolabe.viewmodel.fragment

import android.arch.lifecycle.LiveData
import com.khalifa.astrolabe.data.repository.wmsservice.BaseWMSServicesRepository
import com.khalifa.astrolabe.data.storage.room.entity.WMSService
import com.khalifa.astrolabe.viewmodel.ISharedViewModel
import org.osmdroid.wms.WMSEndpoint

interface IWMSServicesListViewModel : ISharedViewModel {

    val repository: BaseWMSServicesRepository

    val wmsServices: LiveData<List<WMSService>>

    val wmsEndpoints: LiveData<ArrayList<WMSEndpoint>>

    fun deleteWMSService(position: Int)
}