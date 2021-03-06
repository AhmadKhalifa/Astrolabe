package com.khalifa.astrolabe.ui.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.khalifa.astrolabe.R
import com.khalifa.astrolabe.util.FragmentNotAttachedException
import com.khalifa.astrolabe.ui.adapter.WMSServicesAdapter
import com.khalifa.astrolabe.ui.base.BaseFragmentWithSharedViewModel
import com.khalifa.astrolabe.ui.widget.osmdroid.WMSOverlayWithOpacity
import com.khalifa.astrolabe.util.toast
import com.khalifa.astrolabe.viewmodel.Error
import com.khalifa.astrolabe.viewmodel.Event
import com.khalifa.astrolabe.viewmodel.fragment.implementation.MapViewModel
import com.khalifa.astrolabe.viewmodel.fragment.implementation.WMSServicesListViewModel
import kotlinx.android.synthetic.main.fragment_recycler_view.*
import org.osmdroid.wms.WMSEndpoint
import org.osmdroid.wms.WMSLayer

/**
 * @author Ahmad Khalifa
 */

class WMSServicesListFragment :
        BaseFragmentWithSharedViewModel<WMSServicesListViewModel, MapViewModel>(),
        WMSServicesAdapter.OnItemInteractionListener {

    companion object {
        val TAG: String = MapSourcesListFragment::class.java.simpleName

        fun newInstance(fragmentInteractionListener: OnFragmentInteractionListener) =
                WMSServicesListFragment().also { fragment ->
                    fragment.fragmentInteractionListener = fragmentInteractionListener
                }
    }

    interface OnFragmentInteractionListener {

        fun onWMSOverlayAdded(wmsOverlay: WMSOverlayWithOpacity)
    }

    private var fragmentInteractionListener: OnFragmentInteractionListener? = null
    private val mapSourceAdapter = WMSServicesAdapter(this)

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_recycler_view, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(recyclerView) {
            itemAnimator = DefaultItemAnimator()
            layoutManager = LinearLayoutManager(context)
            isNestedScrollingEnabled = false
            adapter = mapSourceAdapter
        }
        mapSourceAdapter.wmsEndpoints = viewModel.wmsEndpoints.value
        mapSourceAdapter.mapWMSLayers = sharedViewModel.mapWMSLayers.value
    }

    private fun updateEndpoints(wmsEndpoints: ArrayList<WMSEndpoint>?) {
        if (wmsEndpoints?.isEmpty() != false) {
            noItemsTextView.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            noItemsTextView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
        mapSourceAdapter.wmsEndpoints = wmsEndpoints
    }

    private fun onLayersUpdated(wmsLayers: ArrayList<WMSOverlayWithOpacity>?) {
        mapSourceAdapter.mapWMSLayers = wmsLayers
    }

    override fun getViewModelInstance() =
            WMSServicesListViewModel.getInstance(this)

    override fun getSharedViewModelInstance() = activity?.run {
        MapViewModel.getInstance(this)
    } ?: throw FragmentNotAttachedException()

    override fun onEvent(event: Event) = toast(event.messageRes)

    override fun onError(error: Error) = toast(error.messageRes)

    override fun registerLiveDataObservers() {
        viewModel.wmsEndpoints.observe(this, Observer(::updateEndpoints))
        sharedViewModel.mapWMSLayers.observe(this, Observer(::onLayersUpdated))
    }

    override fun onDeleteWMSServiceClicked(position: Int) =
            viewModel.deleteWMSService(position)

    override fun onAddWMSLayerClicked(wmsEndpoint: WMSEndpoint, layerIndex: Int) {
        val wmsOverlay = sharedViewModel.addWMSLayer(wmsEndpoint, layerIndex)
        wmsOverlay?.run { fragmentInteractionListener?.onWMSOverlayAdded(this) }
    }

    override fun onRemoveWMSLayerClicked(wmsLayer: WMSLayer) {
        sharedViewModel.removeWMSLayer(wmsLayer)
    }
}