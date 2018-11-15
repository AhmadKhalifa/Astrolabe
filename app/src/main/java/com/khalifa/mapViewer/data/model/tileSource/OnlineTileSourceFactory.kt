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

    lateinit var DEFAULT_MAP_SOURCE: ITileSource

    val onlineMapSources = ArrayList<ITileSource>()

    private val tileSourceImageMap = HashMap<String, Int>()

    fun getIconImage(tileSource: ITileSource?) =
            tileSourceImageMap[tileSource?.name()] ?: R.drawable.defaultmap

    fun initialize(application: Application) {
        OpenStreetMap.MAPNIK = OpenStreetMap.getTileSource()
        DEFAULT_MAP_SOURCE = OpenStreetMap.MAPNIK

        Google.HYBRID = Google.getTileSource("Hybrid", "m")
        Google.SATELLITE = Google.getTileSource("Sat", "s")
        Google.ROADS = Google.getTileSource("Roads", "y")

        Bing.HYBRID = Bing.getTileSource(application, BingMapTileSource.IMAGERYSET_AERIALWITHLABELS)
        Bing.ROAD = Bing.getTileSource(application, BingMapTileSource.IMAGERYSET_ROAD)
        Bing.AERIAL = Bing.getTileSource(application, BingMapTileSource.IMAGERYSET_AERIAL)

        MapBox.STREETS = MapBox.getTileSource(application, "mapbox.streets")
        MapBox.LIGHT = MapBox.getTileSource(application, "mapbox.light")
        MapBox.DARK = MapBox.getTileSource(application, "mapbox.dark")
        MapBox.SATELLITE = MapBox.getTileSource(application, "mapbox.satellite")
        MapBox.SATELLITE_STREETS = MapBox.getTileSource(application, "mapbox.streets-satellite")
        MapBox.OUTDOORS = MapBox.getTileSource(application, "mapbox.outdoors")
        MapBox.PENCIL = MapBox.getTileSource(application, "mapbox.pencil")

        ThunderForest.OPEN_CYCLE_MAP = ThunderForest.getTileSource(application, "cycle")
        ThunderForest.TRANSPORT = ThunderForest.getTileSource(application, "transport")
        ThunderForest.TRANSPORT_DARK = ThunderForest.getTileSource(application, "transport-dark")
        ThunderForest.LANDSCAPE = ThunderForest.getTileSource(application, "landscape")
        ThunderForest.OUTDOORS = ThunderForest.getTileSource(application, "outdoors")

        HereWeGo.DAY = HereWeGo.getTileSource(application, "normal.day")
        HereWeGo.NIGHT = HereWeGo.getTileSource(application, "normal.night")
        HereWeGo.TRAFFIC_DAY = HereWeGo.getTileSource(application, "normal.traffic.day")
        HereWeGo.TRAFFIC_NIGHT = HereWeGo.getTileSource(application, "normal.traffic.night")
        HereWeGo.SATELLITE = HereWeGo.getTileSource(application, "satellite.day")
        HereWeGo.HYBRID = HereWeGo.getTileSource(application, "hybrid.day")

        onlineMapSources.apply {
            add(OpenStreetMap.MAPNIK)

            add(Google.HYBRID)
            add(Google.SATELLITE)
            add(Google.ROADS)

            add(Bing.HYBRID)
            add(Bing.ROAD)
            add(Bing.AERIAL)

            add(MapBox.STREETS)
            add(MapBox.LIGHT)
            add(MapBox.DARK)
            add(MapBox.SATELLITE)
            add(MapBox.SATELLITE_STREETS)
            add(MapBox.OUTDOORS)
            add(MapBox.PENCIL)

            add(HereWeGo.DAY)
            add(HereWeGo.NIGHT)
            add(HereWeGo.TRAFFIC_DAY)
            add(HereWeGo.TRAFFIC_NIGHT)
            add(HereWeGo.SATELLITE)
            add(HereWeGo.HYBRID)

            add(ThunderForest.OPEN_CYCLE_MAP)
            add(ThunderForest.TRANSPORT)
            add(ThunderForest.TRANSPORT_DARK)
            add(ThunderForest.LANDSCAPE)
            add(ThunderForest.OUTDOORS)
        }

        tileSourceImageMap.apply {
            put(OpenStreetMap.MAPNIK.toString(), R.drawable.osm)

            put(Google.HYBRID.toString(), R.drawable.googlemaps)
            put(Google.SATELLITE.toString(), R.drawable.googlemaps)
            put(Google.ROADS.toString(), R.drawable.googlemaps)

            put(Bing.HYBRID.toString(), R.drawable.bing)
            put(Bing.ROAD.toString(), R.drawable.bing)
            put(Bing.AERIAL.toString(), R.drawable.bing)

            put(MapBox.STREETS.toString(), R.drawable.mapbox)
            put(MapBox.LIGHT.toString(), R.drawable.mapbox)
            put(MapBox.DARK.toString(), R.drawable.mapbox)
            put(MapBox.SATELLITE.toString(), R.drawable.mapbox)
            put(MapBox.SATELLITE_STREETS.toString(), R.drawable.mapbox)
            put(MapBox.OUTDOORS.toString(), R.drawable.mapbox)
            put(MapBox.PENCIL.toString(), R.drawable.mapbox)

            put(HereWeGo.DAY.toString(), R.drawable.herewego)
            put(HereWeGo.NIGHT.toString(), R.drawable.herewego)
            put(HereWeGo.TRAFFIC_DAY.toString(), R.drawable.herewego)
            put(HereWeGo.TRAFFIC_NIGHT.toString(), R.drawable.herewego)
            put(HereWeGo.SATELLITE.toString(), R.drawable.herewego)
            put(HereWeGo.HYBRID.toString(), R.drawable.herewego)

            put(ThunderForest.OPEN_CYCLE_MAP.toString(), R.drawable.thunderforest)
            put(ThunderForest.TRANSPORT.toString(), R.drawable.thunderforest)
            put(ThunderForest.TRANSPORT_DARK.toString(), R.drawable.thunderforest)
            put(ThunderForest.LANDSCAPE.toString(), R.drawable.thunderforest)
            put(ThunderForest.OUTDOORS.toString(), R.drawable.thunderforest)
        }
    }

    object OpenStreetMap {

        lateinit var MAPNIK: ITileSource

        fun getTileSource() = XYTileSource(
                "Open Street Map",
                0,
                19,
                256,
                ".png",
                arrayOf("https://a.tile.openstreetmap.org/",
                        "https://b.tile.openstreetmap.org/",
                        "https://c.tile.openstreetmap.org/"),
                "© OpenStreetMap contributors"
        )
    }

    object Google {

        lateinit var HYBRID: ITileSource

        lateinit var SATELLITE: ITileSource

        lateinit var ROADS: ITileSource

        fun getTileSource(mapType: String, lyrs: String) = object : XYTileSource(
                "Google - $mapType",
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

    object Bing {

        lateinit var HYBRID: ITileSource

        lateinit var ROAD: ITileSource

        lateinit var AERIAL: ITileSource

        fun getTileSource(application: Application, style: String) =
                with(BingMapTileSource(null)) {
                    BingMapTileSource.retrieveBingKey(application)
                    BingMapTileSource.setBingKey(
                            ManifestUtil.retrieveKey(application, "BING_KEY")
                    )
                    this.style = style
                    this
                }

    }

    object MapBox {

        lateinit var STREETS: ITileSource

        lateinit var LIGHT: ITileSource

        lateinit var DARK: ITileSource

        lateinit var SATELLITE: ITileSource

        lateinit var SATELLITE_STREETS: ITileSource

        lateinit var OUTDOORS: ITileSource

        lateinit var PENCIL: ITileSource

        fun getTileSource(application: Application, mapType: String) = object : XYTileSource(
                "MapBox - $mapType",
                0,
                20,
                256,
                "png",
                arrayOf("http://api.tiles.mapbox.com/v4/")
        ) {
            override fun getTileURLString(pMapTileIndex: Long): String {
                return "$baseUrl/$mapType/" +
                        "${getZoom(pMapTileIndex)}/${getX(pMapTileIndex)}/${getY(pMapTileIndex)}" +
                        ".$mImageFilenameEnding?access_token=" +
                        ManifestUtil.retrieveKey(application, "MAPBOX_ACCESS_TOKEN")
            }
        }
    }

    object HereWeGo {

        lateinit var DAY: ITileSource

        lateinit var NIGHT: ITileSource

        lateinit var TRAFFIC_DAY: ITileSource

        lateinit var TRAFFIC_NIGHT: ITileSource

        lateinit var SATELLITE: ITileSource

        lateinit var HYBRID: ITileSource

        /**
        <scheme id="normal.day"/>
        <scheme id="normal.night"/>
        <scheme id="normal.traffic.day"/>
        <scheme id="normal.traffic.night"/>
        <scheme id="satellite.day"/>
        <scheme id="hybrid.day"/>
         */

        fun getTileSource(application: Application, mapType: String) = object : XYTileSource(
                "MapBox - $mapType",
                0,
                18,
                256,
                "png",
                arrayOf("https://1.base.maps.api.here.com/maptile/2.1/maptile/newest",
                        "https://2.base.maps.api.here.com/maptile/2.1/maptile/newest",
                        "https://3.base.maps.api.here.com/maptile/2.1/maptile/newest",
                        "https://4.base.maps.api.here.com/maptile/2.1/maptile/newest")
        ) {
            override fun getTileURLString(pMapTileIndex: Long): String {
                return "$baseUrl/$mapType/" +
                        "${getZoom(pMapTileIndex)}/${getX(pMapTileIndex)}/${getY(pMapTileIndex)}" +
                        "/$mImageFilenameEnding?" +
                        "app_id=${ManifestUtil.retrieveKey(application, "HEREWEGO_APPID")}" +
                        "&app_code=${ManifestUtil.retrieveKey(application, "HEREWEGO_APPCODE")}"
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
                "ThunderForest - $mapType",
                0,
                18,
                256,
                "png",
                arrayOf("https://tile.thunderforest.com"
                )) {
            override fun getTileURLString(pMapTileIndex: Long): String {
                return "$baseUrl/$mapType/${getZoom(pMapTileIndex)}/" +
                        "${getX(pMapTileIndex)}/${getY(pMapTileIndex)}.$mImageFilenameEnding?" +
                        "apikey=${ManifestUtil.retrieveKey(application, "THUNDER_FOREST_KEY")}"
            }
        }
    }
}

