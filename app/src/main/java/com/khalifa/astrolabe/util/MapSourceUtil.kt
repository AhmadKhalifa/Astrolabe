package com.khalifa.astrolabe.util

import com.khalifa.astrolabe.R
import com.khalifa.astrolabe.data.model.tileSource.MapSourceFactory
import org.osmdroid.tileprovider.tilesource.ITileSource
import java.util.HashMap

class MapSourceUtil private constructor() {

    companion object {
        private val tileSourceImageMap = HashMap<String, Int>().apply {
            putAll(MapSourceFactory.OpenStreetMap.getSourcesIconMap())
            putAll(MapSourceFactory.Google.getSourcesIconMap())
            putAll(MapSourceFactory.Bing.getSourcesIconMap())
            putAll(MapSourceFactory.MapBox.getSourcesIconMap())
            putAll(MapSourceFactory.HereWeGo.getSourcesIconMap())
            putAll(MapSourceFactory.ThunderForest.getSourcesIconMap())
        }

        fun getIconImage(tileSource: ITileSource?) =
                tileSourceImageMap[tileSource?.toString()] ?: R.drawable.defaultmap

        fun getName(tileSource: ITileSource?) = tileSource?.let {
            val tileSourceStr = tileSource.toString()
            if (tileSourceStr.contains(" - ")) {
                tileSourceStr.substring(0, tileSourceStr.indexOf(" - "))
            } else tileSourceStr
        } ?: "Unknown"

        fun getType(tileSource: ITileSource?) = tileSource?.name() ?: "Unknown"
    }
}