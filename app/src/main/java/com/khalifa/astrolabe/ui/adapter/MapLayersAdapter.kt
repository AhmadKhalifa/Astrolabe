package com.khalifa.astrolabe.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.khalifa.astrolabe.AstrolabeApplication
import com.khalifa.astrolabe.R
import com.khalifa.astrolabe.ui.widget.osmdroid.TilesOverlayWithOpacity
import com.khalifa.astrolabe.util.MapSourceUtil
import kotlinx.android.synthetic.main.list_item_map_layer.view.*

/**
 * @author Ahmad Khalifa
 */

class MapLayersAdapter(private val itemInteractionListener: OnItemInteractionListener?) :
        RecyclerView.Adapter<MapLayersAdapter.MapLayersViewHolder>() {

    var mapLayers: List<TilesOverlayWithOpacity>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MapLayersViewHolder(
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_map_layer, parent, false)
    )

    override fun getItemCount() = mapLayers?.size ?: 0

    override fun onBindViewHolder(mapLayersViewHolder: MapLayersViewHolder, position: Int) =
            mapLayersViewHolder.setContent(this)

    class MapLayersViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun setContent(adapter: MapLayersAdapter) = with(view) {
            val mapLayer = adapter.mapLayers?.get(adapterPosition)
            mapLayer?.let { layer ->
                backgroundThumbnailLayout.setBackgroundResource(MapSourceUtil.getThumbnail(
                        layer.tileProvider().tileSource
                ))
                iconImageView.setImageResource(MapSourceUtil.getIcon(
                        layer.tileProvider().tileSource
                ))
                sourceNameTextView.text = MapSourceUtil.getName(
                        layer.tileProvider().tileSource
                )
                sourceTypeTextView.text = MapSourceUtil.getType(
                        layer.tileProvider().tileSource
                )
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

    interface OnItemInteractionListener {

        fun onRemoveMapLayerClicked(mapLayer: TilesOverlayWithOpacity)

        fun onShowOrHideLayerClicked(mapLayer: TilesOverlayWithOpacity, itemIndex: Int)

        fun onAdjustOpacityClicked(mapLayer: TilesOverlayWithOpacity)

        fun onLayerOrderChanged(mapLayer: TilesOverlayWithOpacity, fromIndex: Int, toIndex: Int)
    }
}