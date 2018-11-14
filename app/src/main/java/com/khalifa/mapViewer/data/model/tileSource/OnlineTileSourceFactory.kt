package com.khalifa.mapViewer.data.model.tileSource

import android.app.Application
import com.khalifa.mapViewer.R
import org.osmdroid.tileprovider.tilesource.*
import org.osmdroid.tileprovider.tilesource.bing.BingMapTileSource
import org.osmdroid.tileprovider.util.ManifestUtil
import org.osmdroid.util.MapTileIndex.*

/**
 * @author Ahmad Khalifa
 */

object OnlineTileSourceFactory {

    val onlineMapSources = ArrayList<ITileSource>()

    private val tileSourceImageMap = HashMap<String, Int>()

    fun getIconImage(tileSource: ITileSource?) =
            tileSourceImageMap[tileSource?.name()] ?: R.drawable.defaultmap

    fun initialize(application: Application) {
        Google.HYBRID = Google.getTileSource("Hybrid", "m")
        Google.SATELLITE = Google.getTileSource("Sat", "s")
        Google.ROADS = Google.getTileSource("Roads", "y")

        ThunderForest.OPEN_CYCLE_MAP = ThunderForest.getTileSource(application, "cycle")
        ThunderForest.TRANSPORT = ThunderForest.getTileSource(application, "transport")
        ThunderForest.TRANSPORT_DARK = ThunderForest.getTileSource(application, "transport-dark")
        ThunderForest.LANDSCAPE = ThunderForest.getTileSource(application, "landscape")
        ThunderForest.OUTDOORS = ThunderForest.getTileSource(application, "outdoors")

        MAP_QUEST = MapQuestTileSource(application)

        HERE_WE_GO = HEREWeGoTileSource(application)

        MAP_BOX = with(MapBoxTileSource(application)) {
            retrieveAccessToken(application)
            retrieveMapBoxMapId(application)
            this
        }

        BING = with(BingMapTileSource(null)) {
            BingMapTileSource.retrieveBingKey(application)
            BingMapTileSource.setBingKey(ManifestUtil.retrieveKey(application, "BING_KEY"))
            style = BingMapTileSource.IMAGERYSET_AERIALWITHLABELS
            this
        }

        onlineMapSources.apply {
            add(Google.HYBRID)
            add(Google.SATELLITE)
            add(Google.ROADS)
            add(ThunderForest.OPEN_CYCLE_MAP)
            add(ThunderForest.TRANSPORT)
            add(ThunderForest.TRANSPORT_DARK)
            add(ThunderForest.LANDSCAPE)
            add(ThunderForest.OUTDOORS)
            add(MAP_QUEST)
            add(HERE_WE_GO)
            add(MAP_BOX)
            add(BING)
        }

        tileSourceImageMap.apply {
            put("Google-Hybrid", R.drawable.googlemaps)
            put("Google-Sat", R.drawable.googlemaps)
            put("Google-Roads", R.drawable.googlemaps)

            put("ThunderForest-cycle", R.drawable.thunderforest)
            put("ThunderForest-transport", R.drawable.thunderforest)
            put("ThunderForest-transport-dark", R.drawable.thunderforest)
            put("ThunderForest-landscape", R.drawable.thunderforest)
            put("ThunderForest-outdoors", R.drawable.thunderforest)

            put("MapQuest", R.drawable.mapquest)
            put("herewego", R.drawable.herewego)
            put("mapbox", R.drawable.mapbox)
            put("BingMaps", R.drawable.bing)
        }
    }

    object Google {

        lateinit var HYBRID: ITileSource

        lateinit var SATELLITE: ITileSource

        lateinit var ROADS: ITileSource

        fun getTileSource(mapType: String, lyrs: String) = object : XYTileSource(
                "Google-$mapType",
                0,
                19,
                256,
                ".png",
                arrayOf("http://mt0.google.com",
                        "http://mt1.google.com",
                        "http://mt2.google.com",
                        "http://mt3.google.com"
                )) {
            override fun getTileURLString(pMapTileIndex: Long): String {
                return "$baseUrl/vt/lyrs=$lyrs&x=${getX(pMapTileIndex)}" +
                        "&y=${getY(pMapTileIndex)}&z=${getZoom(pMapTileIndex)}"
            }
        }
    }

    object ThunderForest {

        lateinit var OPEN_CYCLE_MAP: ITileSource

        lateinit var TRANSPORT: ITileSource

        lateinit var TRANSPORT_DARK: ITileSource

        lateinit var LANDSCAPE: ITileSource

        lateinit var OUTDOORS: ITileSource

        fun getTileSource(application: Application, mapType: String) = object : XYTileSource(
                "ThunderForest-$mapType",
                0,
                18,
                256,
                ".png",
                arrayOf("https://tile.thunderforest.com"
                )) {
            override fun getTileURLString(pMapTileIndex: Long): String {
                return "$baseUrl/$mapType/${getZoom(pMapTileIndex)}/" +
                        "${getX(pMapTileIndex)}/${getY(pMapTileIndex)}.$mImageFilenameEnding?" +
                        "apikey=${ManifestUtil.retrieveKey(application, "BING_KEY")}"
            }
        }
    }

    lateinit var MAP_QUEST: ITileSource

    lateinit var HERE_WE_GO: ITileSource

    lateinit var MAP_BOX: ITileSource

    lateinit var BING: ITileSource
}

