package com.khalifa.astrolabe.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.khalifa.astrolabe.AstrolabeApplication
import com.khalifa.astrolabe.R
import com.khalifa.astrolabe.ui.widget.osmdroid.TilesOverlayWithOpacity
import com.khalifa.astrolabe.ui.widget.osmdroid.WMSOverlayWithOpacity
import com.khalifa.astrolabe.util.MapSourceUtil
import kotlinx.android.synthetic.main.list_item_map_layer.view.*

/**
 * @author Ahmad Khalifa
 */

class WMSLayerAdapter(private val itemInteractionListener: OnItemInteractionListener?) :
        RecyclerView.Adapter<WMSLayerAdapter.WMSLayersViewHolder>() {

    var wmsLayers: List<WMSOverlayWithOpacity>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = WMSLayersViewHolder(
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_map_layer, parent, false)
    )

    override fun getItemCount() = wmsLayers?.size ?: 0

    override fun onBindViewHolder(mapLayersViewHolder: WMSLayersViewHolder, position: Int) =
            mapLayersViewHolder.setContent(this)

    class WMSLayersViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun setContent(adapter: WMSLayerAdapter) = with(view) {
            val wmsLayer = adapter.wmsLayers?.get(adapterPosition)
            wmsLayer?.let { layer ->
                backgroundThumbnailLayout.setBackgroundResource(MapSourceUtil.getThumbnail(null))
                iconImageView.setImageResource(MapSourceUtil.getIcon(null))
                sourceNameTextView.text = layer.wmsLayer.name
                sourceTypeTextView.text = layer.wmsLayer.title
                showHideButton.setText(if (layer.isVisible) R.string.hide else R.string.show)
                opacityButton.isEnabled = layer.isVisible
                opacityButton.setBackgroundColor(AstrolabeApplication.getColor(
                        if (layer.isVisible) R.color.green
                        else R.color.grey
                ))
                adapter.itemInteractionListener ?.let { listener ->
                    showHideButton.setOnClickListener {
                        listener.onShowOrHideLayerClicked(layer, adapterPosition)
                    }
                    opacityButton.setOnClickListener {
                        listener.onAdjustOpacityClicked(layer)
                    }
                    removeButton.setOnClickListener {
                        listener.onRemoveMapLayerClicked(layer)
                    }
                }
                Unit
            }
            Unit
        }
    }

    interface OnItemInteractionListener  {

        fun onRemoveMapLayerClicked(wmsLayer: WMSOverlayWithOpacity)

        fun onShowOrHideLayerClicked(wmsLayer: WMSOverlayWithOpacity, itemIndex: Int)

        fun onAdjustOpacityClicked(wmsLayer: WMSOverlayWithOpacity)

        fun onLayerOrderChanged(wmsLayer: WMSOverlayWithOpacity, fromIndex: Int, toIndex: Int)
    }
}