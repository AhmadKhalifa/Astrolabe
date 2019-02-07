package com.khalifa.astrolabe.viewmodel.fragment

import android.arch.lifecycle.MutableLiveData
import com.khalifa.astrolabe.data.repository.wmsservice.BaseWMSServicesRepository
import com.khalifa.astrolabe.viewmodel.IViewModel
import org.osmdroid.wms.WMSEndpoint

/**
 * @author Ahmad Khalifa
 */

interface INewWMSServiceViewModel : IViewModel {

    val repository: BaseWMSServicesRepository

    val isValidEndpoint: MutableLiveData<Boolean>

    fun checkEndpoint(getCapabilitiesUrl: String)
}