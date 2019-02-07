package com.khalifa.astrolabe.viewmodel.fragment.implementation

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModelProviders
import com.khalifa.astrolabe.data.repository.wmsservice.BaseWMSServicesRepository
import com.khalifa.astrolabe.ui.fragment.NewWMSServiceDialogFragment
import com.khalifa.astrolabe.util.InvalidGetCapabilitiesUrl
import com.khalifa.astrolabe.util.NoInternetConnectionException
import com.khalifa.astrolabe.viewmodel.BaseRxViewModel
import com.khalifa.astrolabe.viewmodel.Error
import com.khalifa.astrolabe.viewmodel.fragment.INewWMSServiceViewModel

class NewWMSServiceViewModel : BaseRxViewModel(), INewWMSServiceViewModel {

    companion object {

        fun getInstance(newWMSServiceDialogFragment: NewWMSServiceDialogFragment) =
                ViewModelProviders.of(newWMSServiceDialogFragment)
                        .get(NewWMSServiceViewModel::class.java)
    }

    override val repository = BaseWMSServicesRepository.getDefault()

    override val isValidEndpoint = MutableLiveData<Boolean>()

    override fun checkEndpoint(getCapabilitiesUrl: String) = performAsync(
            action = {
                repository.addWMSService(getCapabilitiesUrl)
            },
            onSuccess = {
                isValidEndpoint.value = true
            },
            onFailure = { throwable ->
                when(throwable) {
                    is NoInternetConnectionException -> notify(Error.NO_INTERNET_CONNECTION)
                    is InvalidGetCapabilitiesUrl -> {
                        notify(Error.ERROR_LOADING_WMS_CAPABILITIES)
                        isValidEndpoint.value = false
                    }
                    else -> {
                        notify(Error.GENERAL_ERROR)
                        isValidEndpoint.value = false
                    }
                }
            }
    )
}