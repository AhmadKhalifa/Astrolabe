package com.khalifa.astrolabe.ui.adapter

import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.khalifa.astrolabe.AstrolabeApplication
import com.khalifa.astrolabe.R
import kotlinx.android.synthetic.main.list_item_wms_layer.view.*
import kotlinx.android.synthetic.main.list_item_wms_layers_group.view.*
import org.osmdroid.wms.WMSEndpoint
import org.osmdroid.wms.WMSLayer
import java.util.ArrayList

/**
 * @author Ahmad Khalifa
 */

class WMSServicesAdapter(private val itemInteractionListener: OnItemInteractionListener?) :
        RecyclerView.Adapter<WMSServicesAdapter.WMSServicesViewHolder>() {

    var mapWMSLayers: ArrayList<WMSLayer>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var wmsEndpoints: ArrayList<WMSEndpoint>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = WMSServicesViewHolder(
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_wms_layers_group, parent, false)
    )

    override fun getItemCount() = wmsEndpoints?.size ?: 0

    override fun onBindViewHolder(wmsServicesViewHolder: WMSServicesViewHolder, position: Int) =
            wmsServicesViewHolder.setContent(this)

    class WMSServicesViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun setContent(adapter: WMSServicesAdapter) = with(view) {
            val wmsEndpoint = adapter.wmsEndpoints?.get(adapterPosition)
            wmsEndpoint?.let { endpoint ->
                serviceTitleTextView.text = endpoint.title
                serviceAbstractTextView.text = endpoint.description
                deleteButton.setOnClickListener {
                    adapter.itemInteractionListener?.onDeleteWMSServiceClicked(endpoint)
                }
                with(layersRecyclerView) {
                    itemAnimator = DefaultItemAnimator()
                    layoutManager = LinearLayoutManager(context)
                    isNestedScrollingEnabled = false
                    this.adapter = WMSLayersAdapter(
                            adapter.itemInteractionListener,
                            adapter.mapWMSLayers
                    ).also { adapter ->
                        adapter.wmsLayers = endpoint.layers
                    }
                }
            }
            Unit
        }
    }

    interface OnItemInteractionListener {

        fun onDeleteWMSServiceClicked(wmsEndpoint: WMSEndpoint)

        fun onAddWMSLayerClicked(wmsLayer: WMSLayer)
    }

    private class WMSLayersAdapter(
            private val itemInteractionListener: WMSServicesAdapter.OnItemInteractionListener?,
            private var mapWMSLayers: ArrayList<WMSLayer>?
    ) : RecyclerView.Adapter<WMSLayersAdapter.WMSLayersViewHolder>() {

        var wmsLayers: List<WMSLayer>? = null
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
                WMSLayersViewHolder(LayoutInflater
                        .from(parent.context)
                        .inflate(R.layout.list_item_wms_layer, parent, false)
                )

        override fun getItemCount() = wmsLayers?.size ?: 0

        override fun onBindViewHolder(mapSourceViewHolder: WMSLayersViewHolder,
                                      position: Int) = mapSourceViewHolder.setContent(this)

        class WMSLayersViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

            private fun isLayerAlreadyUsed(adapter: WMSLayersAdapter, wmsLayer: WMSLayer) =
                    adapter.mapWMSLayers?.let { layers ->
                        for (layer in layers)
                            if (layer == wmsLayer) return true
                        false
                    } ?: false

            fun setContent(adapter: WMSLayersAdapter) = with(view) {
                val wmsLayer = adapter.wmsLayers?.get(adapterPosition)
                wmsLayer?.let { layer ->
                    layerNameTextView.text = layer.name
                    layerTitleTextView.text = layer.title
                    val isLayerAlreadyUsed = isLayerAlreadyUsed(adapter, layer)
                    addButton.isEnabled = !isLayerAlreadyUsed
                    addButton.setBackgroundColor(AstrolabeApplication.getColor(
                            if (!isLayerAlreadyUsed) R.color.green
                            else R.color.grey
                    ))
                    addButton.setOnClickListener {
                        adapter.itemInteractionListener?.onAddWMSLayerClicked(layer)
                    }
                }
                Unit
            }
        }
    }
}