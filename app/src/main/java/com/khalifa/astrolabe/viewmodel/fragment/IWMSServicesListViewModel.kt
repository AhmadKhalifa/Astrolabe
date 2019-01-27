package com.khalifa.astrolabe.viewmodel.fragment

import android.arch.lifecycle.MutableLiveData
import com.khalifa.astrolabe.viewmodel.IViewModel
import org.osmdroid.wms.WMSEndpoint

interface IWMSServicesListViewModel : IViewModel {

    val wmsEndpoints: MutableLiveData<ArrayList<WMSEndpoint>>

    fun loadWMSEndpoints()

    fun deleteWMSService(wmsEndpoint: WMSEndpoint)
}