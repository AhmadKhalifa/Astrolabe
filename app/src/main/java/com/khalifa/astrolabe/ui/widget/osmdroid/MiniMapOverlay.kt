package com.khalifa.astrolabe.ui.widget.osmdroid

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Handler
import android.view.MotionEvent
import com.khalifa.astrolabe.AstrolabeApplication
import com.khalifa.astrolabe.util.DimensionsUtil
import org.osmdroid.tileprovider.MapTileProviderBase
import org.osmdroid.tileprovider.MapTileProviderBasic
import org.osmdroid.tileprovider.tilesource.ITileSource
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.TilesOverlay
import org.osmdroid.views.util.constants.OverlayConstants

/**
 * @author Ahmad Khalifa
 */

class MiniMapOverlay constructor(
        pContext: Context?,
        pTileRequestCompleteHandler: Handler,
        pTileProvider: MapTileProviderBase = MapTileProviderBasic(pContext),
        pZoomDifference: Int = OverlayConstants.DEFAULT_ZOOMLEVEL_MINIMAP_DIFFERENCE) :
        TilesOverlay(pTileProvider, pContext) {

    var width = 100
    var height = 100
    private var padding = 10
    private var zoomDifference: Int = 0
    private val mPaint: Paint

    init {
        zoomDifference = pZoomDifference

        mTileProvider.setTileRequestCompleteHandler(pTileRequestCompleteHandler)

        // Don't draw loading lines in the minimap
        loadingLineColor = loadingBackgroundColor

        // Scale the default size
        val density = AstrolabeApplication.instance.resources.displayMetrics.density
        width *= density.toInt()
        height *= density.toInt()

        mPaint = Paint()
        mPaint.color = Color.GRAY
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = 2f
    }

    fun setTileSource(pTileSource: ITileSource) {
        mTileProvider.tileSource = pTileSource
    }

    override fun draw(c: Canvas, osmv: MapView, shadow: Boolean) {
        if (shadow) {
            return
        }

        if (!setViewPort(c, osmv)) {
            return
        }

        // Draw a solid background where the minimap will be drawn with a 2 pixel inset
        osmv.projection.save(c, false, true)

//        -1 * (DimensionsUtil.getScreenWidthInPixels(AstrolabeApplication.instance).toFloat() - c.width)
        c.translate((-1 * (DimensionsUtil.getScreenWidthInPixels(AstrolabeApplication.instance).toFloat() - width)) + 2 * padding, 0f)
        c.drawRect(
                (canvasRect.left - 2).toFloat(), (canvasRect.top - 2).toFloat(),
                (canvasRect.right + 2).toFloat(), (canvasRect.bottom + 2).toFloat(), mPaint)

        super.drawTiles(c, projection, projection.zoomLevel, mViewPort)
        osmv.projection.restore(c, true)
    }

    override fun onSingleTapUp(pEvent: MotionEvent?, pMapView: MapView?): Boolean {
        // Consume event so layers underneath don't receive
        return contains(pEvent)
    }

    override fun onDoubleTap(pEvent: MotionEvent?, pMapView: MapView?): Boolean {
        // Consume event so layers underneath don't receive
        return contains(pEvent)
    }

    override fun onLongPress(pEvent: MotionEvent?, pMapView: MapView?): Boolean {
        // Consume event so layers underneath don't receive
        return contains(pEvent)
    }

    override fun isOptionsMenuEnabled(): Boolean {
        // Don't provide menu items from TilesOverlay.
        return false
    }

    private operator fun contains(pEvent: MotionEvent?): Boolean {
        val canvasRect = canvasRect
        return canvasRect != null && canvasRect.contains(pEvent!!.x.toInt(), pEvent.y.toInt())
    }

    override fun setViewPort(pCanvas: Canvas?, pMapView: MapView): Boolean {
        val zoomLevel = pMapView.projection.zoomLevel - zoomDifference
        if (zoomLevel < mTileProvider.minimumZoomLevel) {
            return false
        }

        val left = pCanvas!!.width - padding - width
        val top = pCanvas.height - padding - height
        canvasRect = Rect(left, top, left + width, top + height)
        projection = pMapView.projection.getOffspring(zoomLevel, canvasRect)
        projection.getMercatorViewPort(mViewPort)
        return true
    }
}
