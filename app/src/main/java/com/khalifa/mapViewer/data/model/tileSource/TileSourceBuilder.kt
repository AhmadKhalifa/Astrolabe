package com.khalifa.mapViewer.data.model.tileSource

import org.osmdroid.tileprovider.tilesource.XYTileSource

class TileSourceBuilder(private val baseUrl: String) {

    private var name = "Mapnik"
    private var minZoomLevel = 0
    private var maxZoomLevel = 15
    private var tileSizePixels = 256
    private var tileExtension = ".png"

    fun name(name: String) = this.also { this.name = name }

    fun minZoomLevel(minZoomLevel: Int) = this.also { this.minZoomLevel = minZoomLevel }

    fun maxZoomLevel(maxZoomLevel: Int) = this.also { this.maxZoomLevel = maxZoomLevel }

    fun tileSizePixels(tileSizePixels: Int) = this.also { this.tileSizePixels = tileSizePixels }

    fun tileExtension(tileExtension: String) = this.also { this.tileExtension = tileExtension }

    fun build() = XYTileSource(
            name,
            minZoomLevel, maxZoomLevel,
            tileSizePixels,
            ".png",
            arrayOf(baseUrl)
    )
}