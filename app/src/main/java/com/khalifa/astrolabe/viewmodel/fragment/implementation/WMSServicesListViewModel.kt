package com.khalifa.astrolabe.viewmodel.fragment.implementation

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModelProviders
import com.khalifa.astrolabe.data.repository.wmsservice.BaseWMSServicesRepository
import com.khalifa.astrolabe.data.storage.room.entity.WMSService
import com.khalifa.astrolabe.ui.fragment.WMSServicesListFragment
import com.khalifa.astrolabe.viewmodel.BaseSharedViewModel
import com.khalifa.astrolabe.viewmodel.Error
import com.khalifa.astrolabe.viewmodel.Event
import com.khalifa.astrolabe.viewmodel.fragment.IWMSServicesListViewModel
import org.osmdroid.wms.WMSEndpoint
import org.osmdroid.wms.WMSParser
import java.lang.IllegalStateException

/**
 * @author Ahmad Khalifa
 */

class WMSServicesListViewModel : BaseSharedViewModel(), IWMSServicesListViewModel {

    companion object {

        fun getInstance(wmsServicesListFragment: WMSServicesListFragment) =
                ViewModelProviders.of(wmsServicesListFragment)
                        .get(WMSServicesListViewModel::class.java)
    }

    override val repository = BaseWMSServicesRepository.getDefault()

    override val wmsServices: LiveData<List<WMSService>> = repository.getAllWMSEndServices()

    override val wmsEndpoints = Transformations.map(wmsServices, this::extractWMSEndpoint)!!

    private fun extractWMSEndpoint(wmsServices: List<WMSService>?) = wmsServices?.run {
        ArrayList<WMSEndpoint>().apply {
            this@run.forEach { add(WMSParser.parse(it.inputStream)) }
        }
    } ?: ArrayList()

    override fun deleteWMSService(position: Int) = performAsync(
            action = {
                wmsServices.value?.run {
                    if (position in 0 until size) repository.deleteWMSService(get(position))
                } ?: throw IllegalStateException("Couldn't get repository WMS services")
            },
            onSuccess = {
                notify(Event.DELETED_SUCCESSFULLY)
            },
            onFailure = {
                notify(Error.GENERAL_ERROR)
            }
    )
}