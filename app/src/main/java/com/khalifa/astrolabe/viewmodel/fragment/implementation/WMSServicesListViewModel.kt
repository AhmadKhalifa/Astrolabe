package com.khalifa.astrolabe.viewmodel.fragment.implementation

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModelProviders
import com.khalifa.astrolabe.ui.fragment.WMSServicesListFragment
import com.khalifa.astrolabe.viewmodel.BaseRxViewModel
import com.khalifa.astrolabe.viewmodel.fragment.IWMSServicesListViewModel
import org.osmdroid.wms.WMSEndpoint

/**
 * @author Ahmad Khalifa
 */

class WMSServicesListViewModel : BaseRxViewModel(), IWMSServicesListViewModel {

    companion object {
        @JvmStatic
        fun getInstance(wmsServicesListFragment: WMSServicesListFragment): WMSServicesListViewModel =
                ViewModelProviders
                        .of(wmsServicesListFragment)
                        .get(WMSServicesListViewModel::class.java)
    }

    val wmsEndpoints = MutableLiveData<ArrayList<WMSEndpoint>>()

    fun loadWMSEndpoints() {
        wmsEndpoints.value = ArrayList()
    }

    fun deleteWMSService(wmsEndpoint: WMSEndpoint) {
        wmsEndpoints.value?.run {
            remove(wmsEndpoint)
            wmsEndpoints.value = this
        }
    }
}