package com.khalifa.astrolabe.data.model.tileSource

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import org.osmdroid.tileprovider.tilesource.XYTileSource

/**
 * @author Ahmad Khalifa
 */

@Entity
data class TileSource(var name: String = "",
                      var minZoomLevel: Int = 0, var maxZoomLevel: Int = 18,
                      var tileSizePixels: Int = 256, var tileExtension: String = ".png",
                      var baseUrls: Array<String>? = null, var tileUrlString: String? = null) {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TileSource

        if (id != other.id) return false
        if (name != other.name) return false
        if (minZoomLevel != other.minZoomLevel) return false
        if (maxZoomLevel != other.maxZoomLevel) return false
        if (tileSizePixels != other.tileSizePixels) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + minZoomLevel
        result = 31 * result + maxZoomLevel
        result = 31 * result + tileSizePixels
        result = 31 * result + tileExtension.hashCode()
        result = 31 * result + (baseUrls?.contentHashCode() ?: 0)
        return result
    }

    fun build() =
            XYTileSource(name, minZoomLevel, maxZoomLevel, tileSizePixels, tileExtension, baseUrls)
}