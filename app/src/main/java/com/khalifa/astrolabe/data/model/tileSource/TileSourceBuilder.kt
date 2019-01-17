package com.khalifa.astrolabe.data.model.tileSource

import org.osmdroid.tileprovider.tilesource.XYTileSource

/**
 * @author Ahmad Khalifa
 */

class TileSourceBuilder(private val baseUrls: Array<String>) {

    private var name = ""
    private var minZoomLevel = 0
    private var maxZoomLevel = 18
    private var tileSizePixels = 256
    private var tileExtension = ".png"

    constructor(baseUrl: String) : this(arrayOf(baseUrl))

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
            baseUrls
    )
}