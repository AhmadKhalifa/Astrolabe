package com.khalifa.mapViewer.ui.widget.mapview

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

import com.khalifa.mapViewer.ui.widget.pinterest.PinterestView

import org.osmdroid.tileprovider.MapTileProviderBase

class MapView : org.osmdroid.views.MapView {

    var contextMenu: View? = null

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context) : super(context)

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        contextMenu?.dispatchTouchEvent(event)
        return super.dispatchTouchEvent(event)
    }
}
