package com.khalifa.astrolabe.ui.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.FragmentManager
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.khalifa.astrolabe.R
import com.khalifa.astrolabe.util.FragmentNotAttachedException
import com.khalifa.astrolabe.ui.adapter.MapLayersAdapter
import com.khalifa.astrolabe.ui.adapter.WMSLayerAdapter
import com.khalifa.astrolabe.ui.base.BaseFullScreenDialogFragmentWithSharedViewModel
import com.khalifa.astrolabe.ui.widget.osmdroid.TilesOverlayWithOpacity
import com.khalifa.astrolabe.ui.widget.osmdroid.WMSOverlayWithOpacity
import com.khalifa.astrolabe.util.MapSourceUtil
import com.khalifa.astrolabe.viewmodel.Error
import com.khalifa.astrolabe.viewmodel.Event
import com.khalifa.astrolabe.viewmodel.fragment.implementation.LayerManagerViewModel
import com.khalifa.astrolabe.viewmodel.fragment.implementation.MapViewModel
import kotlinx.android.synthetic.main.content_layers_managers.*
import kotlinx.android.synthetic.main.fragment_layers_manager.view.*
import org.osmdroid.tileprovider.tilesource.ITileSource

/**
 * @author Ahmad Khalifa
 */

class LayersManagerFragment :
        BaseFullScreenDialogFragmentWithSharedViewModel<LayerManagerViewModel, MapViewModel>(),
        MapLayersAdapter.OnItemInteractionListener,
        WMSLayerAdapter.OnItemInteractionListener {

    companion object {
        private val TAG: String = LayersManagerFragment::class.java.simpleName

        private const val DIALOG_CLOSE_DELAY_MS = 1000L

        fun showFragment(fragmentManager: FragmentManager?,
                         onFragmentInteractionListener: OnFragmentInteractionListener) =
                fragmentManager?.let { manager ->
                    LayersManagerFragment().also {
                        it.fragmentInteractionListener = onFragmentInteractionListener
                        it.show(manager, TAG)
                    }
                }
    }

    interface OnFragmentInteractionListener {

        fun openLayerOpacityAdjustmentScreen(overlay: TilesOverlayWithOpacity)

        fun openMapLayersScreen()

        fun openWMSLayersScreen()
    }

    private var fragmentInteractionListener: OnFragmentInteractionListener? = null

    private val mapLayersAdapter = MapLayersAdapter(this)
    private val wmsLayersAdapter = WMSLayerAdapter(this)

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_layers_manager, container, false)
                    .also {
                        with(it.toolbar) {
                            setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
                            setNavigationOnClickListener { dismiss() }
                            setTitle(R.string.map_layers)
                        }
                    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(mapLayersRecyclerView) {
            itemAnimator = DefaultItemAnimator()
            layoutManager = LinearLayoutManager(context)
            isNestedScrollingEnabled = false
            adapter = mapLayersAdapter
        }
        with(wmsLayersRecyclerView) {
            itemAnimator = DefaultItemAnimator()
            layoutManager = LinearLayoutManager(context)
            isNestedScrollingEnabled = false
            adapter = wmsLayersAdapter
        }
        val openMapSourcesClickListener = View.OnClickListener { button ->
            when(button.id) {
                R.id.changeBaseMapButton, R.id.addMapLayerButton ->
                    fragmentInteractionListener?.openMapLayersScreen()
                R.id.addWMSLayerButton ->
                    fragmentInteractionListener?.openWMSLayersScreen()
            }
            Handler().postDelayed(::dismiss, DIALOG_CLOSE_DELAY_MS)
        }
        changeBaseMapButton.setOnClickListener(openMapSourcesClickListener)
        addMapLayerButton.setOnClickListener(openMapSourcesClickListener)
        addWMSLayerButton.setOnClickListener(openMapSourcesClickListener)
        setBaseMapLayout(sharedViewModel.baseMapSource.value)
        setMapLayers(sharedViewModel.mapLayers.value)
        setWMSLayers(sharedViewModel.mapWMSLayers.value)
    }

    override fun getViewModelInstance() = LayerManagerViewModel.getInstance(this)

    override fun onEvent(event: Event) {}

    override fun onError(error: Error) {}

    override fun registerLiveDataObservers() {
        sharedViewModel.baseMapSource.observe(this, Observer(::setBaseMapLayout))
        sharedViewModel.mapLayers.observe(this, Observer(::setMapLayers))
        sharedViewModel.mapWMSLayers.observe(this, Observer(::setWMSLayers))
    }

    override fun getSharedViewModelInstance() = activity?.run {
        MapViewModel.getInstance(this)
    } ?: throw FragmentNotAttachedException()

    override fun onRemoveMapLayerClicked(mapLayer: TilesOverlayWithOpacity) =
        sharedViewModel.removeMapLayer(mapLayer)

    override fun onShowOrHideLayerClicked(mapLayer: TilesOverlayWithOpacity, itemIndex: Int) {
        sharedViewModel.mapLayers.value?.get(itemIndex)?.reverseVisibility()
        mapLayersAdapter.notifyItemChanged(itemIndex)
    }

    override fun onAdjustOpacityClicked(mapLayer: TilesOverlayWithOpacity) {
        fragmentInteractionListener?.openLayerOpacityAdjustmentScreen(mapLayer)
        dismiss()
    }

    override fun onLayerOrderChanged(mapLayer: TilesOverlayWithOpacity,
                                     fromIndex: Int,
                                     toIndex: Int) {

    }

    override fun onRemoveMapLayerClicked(wmsLayer: WMSOverlayWithOpacity) =
            sharedViewModel.removeWMSLayer(wmsLayer)

    override fun onShowOrHideLayerClicked(wmsLayer: WMSOverlayWithOpacity, itemIndex: Int) {
        sharedViewModel.mapWMSLayers.value?.get(itemIndex)?.reverseVisibility()
        wmsLayersAdapter.notifyItemChanged(itemIndex)
    }

    override fun onAdjustOpacityClicked(wmsLayer: WMSOverlayWithOpacity) =
            onAdjustOpacityClicked(wmsLayer as TilesOverlayWithOpacity)

    override fun onLayerOrderChanged(wmsLayer: WMSOverlayWithOpacity,
                                     fromIndex: Int,
                                     toIndex: Int) {

    }

    private fun setBaseMapLayout(baseTileSource: ITileSource?) {
        baseTileSource?.run {
            thumbnailImageView.setImageResource(MapSourceUtil.getThumbnail(this))
            iconImageView.setImageResource(MapSourceUtil.getIcon(this))
            sourceNameTextView.text = MapSourceUtil.getName(this)
            sourceTypeTextView.text = MapSourceUtil.getType(this)
        }
    }

    private fun setMapLayers(mapLayers: ArrayList<TilesOverlayWithOpacity>?) {
        if (mapLayers != null) {
            if (mapLayers.isEmpty()) {
                mapLayersRecyclerView.visibility = View.GONE
                noMapLayersLayout.visibility = View.VISIBLE
            } else {
                mapLayersRecyclerView.visibility = View.VISIBLE
                noMapLayersLayout.visibility = View.GONE
            }
            mapLayersAdapter.mapLayers = mapLayers
        } else setMapLayers(ArrayList())
    }

    private fun setWMSLayers(wmsLayers: ArrayList<WMSOverlayWithOpacity>?) {
        if (wmsLayers != null) {
            if (wmsLayers.isEmpty()) {
                wmsLayersRecyclerView.visibility = View.GONE
                noWMSLayersLayout.visibility = View.VISIBLE
            } else {
                wmsLayersRecyclerView.visibility = View.VISIBLE
                noWMSLayersLayout.visibility = View.GONE
            }
            wmsLayersAdapter.wmsLayers = wmsLayers
        } else setWMSLayers(ArrayList())
    }
}