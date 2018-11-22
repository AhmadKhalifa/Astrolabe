package com.khalifa.astrolabe.ui.widget.osmdroid

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class MapView : org.osmdroid.views.MapView {

    var contextMenu: View? = null

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context) : super(context)

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        contextMenu?.dispatchTouchEvent(event)
        return super.dispatchTouchEvent(event)
    }
}
