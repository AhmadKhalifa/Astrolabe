package com.khalifa.astrolabe.ui.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.khalifa.astrolabe.AstrolabeApplication
import com.khalifa.astrolabe.R
import com.khalifa.astrolabe.ui.activity.MapActivity
import com.khalifa.astrolabe.ui.base.BaseDialogFragmentWithSharedViewModel
import com.khalifa.astrolabe.util.toast
import com.khalifa.astrolabe.viewmodel.Error
import com.khalifa.astrolabe.viewmodel.Event
import com.khalifa.astrolabe.viewmodel.fragment.implementation.MapViewModel
import com.khalifa.astrolabe.viewmodel.fragment.implementation.NewWMSServiceViewModel
import kotlinx.android.synthetic.main.fragment_dialog_new_wms_endpoint.*

/**
 * @author Ahmad Khalifa
 */

class NewWMSServiceDialogFragment :
        BaseDialogFragmentWithSharedViewModel<NewWMSServiceViewModel, MapViewModel>() {

    companion object {
        val TAG: String = NewWMSServiceDialogFragment::class.java.simpleName

        fun showFragment(fragmentManager: FragmentManager?) =
                fragmentManager?.let { manager ->
                    NewWMSServiceDialogFragment().also { fragment ->
                        fragment.show(manager, TAG)
                    }
                }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_dialog_new_wms_endpoint, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addButton.setOnClickListener {
            addButton.isLoading = true
            viewModel.checkEndpoint(endpointEditText?.text.toString())
        }
        closeButton.setOnClickListener { dismiss() }
    }

    override fun getViewModelInstance() =
            NewWMSServiceViewModel.getInstance(this)

    override fun getSharedViewModelInstance() =
            MapViewModel.getInstance(context as MapActivity)

    override fun onEvent(event: Event) {}

    override fun onError(error: Error) {
        toast(error.stringResId)
        addButton.isLoading = false
    }

    override fun registerLiveDataObservers() {
        viewModel.isValidEndpoint.observe(this, Observer(::onEndpointValidationDone))
    }

    private fun onEndpointValidationDone(isValidEndpoint: Boolean?) {
        addButton.isLoading = false
        if (isValidEndpoint != null && isValidEndpoint) {
            toast(R.string.wms_service_saved_successfully)
            dismiss()
        } else {
            endpointEditText.error =
                    AstrolabeApplication.getString(R.string.invalid_getcapabilities_endpoint)
        }
    }
}