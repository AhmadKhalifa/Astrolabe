package com.khalifa.astrolabe.ui.widget.osmdroid

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.annotation.IntRange
import org.osmdroid.tileprovider.MapTileProviderBase
import org.osmdroid.views.overlay.TilesOverlay
import java.io.Serializable

/**
 * @author Ahmad Khalifa
 */

class TilesOverlayWithOpacity(tileProvider: MapTileProviderBase?,
                              aContext: Context?,
                              @IntRange(from = 10, to = 100) var transparencyPercentage: Int = 50) :
        TilesOverlay(tileProvider, aContext, true, true),
        Serializable {

    var isVisible = true
    private val mIntersectionRect: Rect = Rect()
    private var currentColorFilter: ColorFilter? = null

    override fun onTileReadyToDraw(c: Canvas?, currentMapTile: Drawable?, tileRect: Rect?) {
        if (c != null && currentMapTile != null && tileRect != null) {
            currentMapTile.colorFilter = currentColorFilter
            currentMapTile.setBounds(tileRect.left, tileRect.top, tileRect.right, tileRect.bottom)
            currentMapTile.alpha =
                    if (isVisible) (255f * (transparencyPercentage / 100f)).toInt()
                    else 0
            val canvasRect = canvasRect
            if (canvasRect == null) {
                currentMapTile.draw(c)
                return
            }
            // Save the current clipping bounds
            c.save()
            // Check to see if the drawing area intersects with the mini-map area
            if (mIntersectionRect.setIntersect(c.clipBounds, canvasRect)) {
                // If so, then clip that area
                c.clipRect(mIntersectionRect)

                // Draw the tile, which will be appropriately clipped
                currentMapTile.draw(c)
            }
            c.restore()
        }
    }

    fun tileProvider(): MapTileProviderBase = mTileProvider

    fun reverseVisibility() {
        isVisible = !isVisible
    }

    override fun setColorFilter(filter: ColorFilter?) {
        super.setColorFilter(filter)
        currentColorFilter = filter
    }
}