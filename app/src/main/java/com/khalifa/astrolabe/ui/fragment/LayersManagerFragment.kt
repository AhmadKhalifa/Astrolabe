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
import com.khalifa.astrolabe.ui.base.BaseFullScreenDialogFragmentWithSharedViewModel
import com.khalifa.astrolabe.ui.widget.osmdroid.TilesOverlayWithOpacity
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
        MapLayersAdapter.OnItemInteractionListener {

    companion object {
        private val TAG: String = LayersManagerFragment::class.java.simpleName

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

        fun openAllSourcesScreen()
    }

    private var fragmentInteractionListener: OnFragmentInteractionListener? = null
    private val mapLayersAdapter = MapLayersAdapter(this)

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
        with(recyclerView) {
            itemAnimator = DefaultItemAnimator()
            layoutManager = LinearLayoutManager(context)
            isNestedScrollingEnabled = false
            adapter = mapLayersAdapter
        }
        val openMapSourcesClickListener = View.OnClickListener{
            fragmentInteractionListener?.openAllSourcesScreen()
            Handler().postDelayed(::dismiss, 1000)
        }
        changeBaseMapButton.setOnClickListener(openMapSourcesClickListener)
        addLayerButton.setOnClickListener(openMapSourcesClickListener)
        setBaseMapLayout(sharedViewModel.baseMapSource.value)
        setMapLayers(sharedViewModel.mapLayers.value)
    }

    override fun getViewModelInstance() = LayerManagerViewModel.getInstance(this)

    override fun onEvent(event: Event) {}

    override fun onError(error: Error) {}

    override fun registerLiveDataObservers() {
        sharedViewModel.baseMapSource.observe(this, Observer(this::setBaseMapLayout))
        sharedViewModel.mapLayers.observe(this, Observer(this::setMapLayers))
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
                recyclerView.visibility = View.GONE
                noLayersLayout.visibility = View.VISIBLE
            } else {
                recyclerView.visibility = View.VISIBLE
                noLayersLayout.visibility = View.GONE
            }
            mapLayersAdapter.mapLayers = mapLayers
        } else setMapLayers(ArrayList())
    }
}