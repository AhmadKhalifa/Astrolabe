package com.khalifa.astrolabe.ui.fragment

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.khalifa.astrolabe.R
import com.khalifa.astrolabe.ui.activity.MapActivity
import com.khalifa.astrolabe.ui.adapter.WMSServicesAdapter
import com.khalifa.astrolabe.ui.base.BaseFullScreenDialogFragment
import com.khalifa.astrolabe.viewmodel.Error
import com.khalifa.astrolabe.viewmodel.Event
import com.khalifa.astrolabe.viewmodel.activity.MapActivityViewModel
import com.khalifa.astrolabe.viewmodel.fragment.implementation.WMSServicesListViewModel
import kotlinx.android.synthetic.main.fragment_recycler_view.*
import org.osmdroid.wms.WMSEndpoint
import org.osmdroid.wms.WMSLayer
import java.lang.IllegalStateException

/**
 * @author Ahmad Khalifa
 */

class WMSServicesListFragment :
        BaseFullScreenDialogFragment<WMSServicesListViewModel>(),
        WMSServicesAdapter.OnItemInteractionListener {

    companion object {
        private val TAG: String = MapSourcesListFragment::class.java.simpleName

        fun newInstance() = WMSServicesListFragment()
    }

    interface OnFragmentInteractionListener {

        fun onDeleteWMSServiceClicked(wmsEndpoint: WMSEndpoint)

        fun onAddWMSLayerClicked(wmsLayer: WMSLayer)
    }

    private lateinit var activityViewModel: MapActivityViewModel
    private val mapSourceAdapter = WMSServicesAdapter(this)
    private var fragmentInteractionListener: OnFragmentInteractionListener? = null

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
        viewModel.loadWMSEndpoints()
        mapSourceAdapter.mapWMSLayers = activityViewModel.mapWMSLayers.value
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) fragmentInteractionListener = context
        else throw IllegalStateException(
                "$context must implement WMSServicesListFragment.OnFragmentInteractionListener"
        )
    }

    override fun onDetach() {
        fragmentInteractionListener = null
        super.onDetach()
    }

    private fun updateEndpoints(wmsEndpoints: ArrayList<WMSEndpoint>?) {
        mapSourceAdapter.wmsEndpoints = wmsEndpoints
    }

    private fun onLayersUpdated(wmsLayers: ArrayList<WMSLayer>?) {
        mapSourceAdapter.mapWMSLayers = wmsLayers
    }

    override fun getViewModelInstance() =
            WMSServicesListViewModel.getInstance(this)

    override fun onEvent(event: Event) {}

    override fun onError(error: Error) {}

    override fun registerLiveDataObservers() {
        viewModel.wmsEndpoints.observe(this, Observer(this::updateEndpoints))
        activityViewModel = MapActivityViewModel.getInstance(context as MapActivity)
        activityViewModel.mapWMSLayers.observe(this, Observer(this::onLayersUpdated))
    }

    override fun onDeleteWMSServiceClicked(wmsEndpoint: WMSEndpoint) =
            viewModel.deleteWMSService(wmsEndpoint)

    override fun onAddWMSLayerClicked(wmsLayer: WMSLayer) = activityViewModel.addWMSLayer(wmsLayer)
}