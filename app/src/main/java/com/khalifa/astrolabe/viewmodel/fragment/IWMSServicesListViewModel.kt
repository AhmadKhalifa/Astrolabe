package com.khalifa.astrolabe.viewmodel.fragment

import android.arch.lifecycle.MutableLiveData
import org.osmdroid.wms.WMSEndpoint

interface IWMSServicesListViewModel {

    val wmsEndpoints: MutableLiveData<ArrayList<WMSEndpoint>>
}