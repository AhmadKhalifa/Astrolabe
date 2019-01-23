package com.khalifa.astrolabe.ui.widget.osmdroid

import android.content.Context
import android.support.annotation.IntRange
import org.osmdroid.tileprovider.MapTileProviderBase
import org.osmdroid.wms.WMSLayer
import java.io.Serializable

class WMSOverlayWithOpacity(tileProvider: MapTileProviderBase?,
                            aContext: Context?,
                            val wmsLayer: WMSLayer,
                            @IntRange(from = 10, to = 100) transparencyPercentage: Int = 50) :
        TilesOverlayWithOpacity(tileProvider, aContext, transparencyPercentage),
        Serializable