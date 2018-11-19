package com.khalifa.astrolabe.data.model.tileSource

import com.khalifa.astrolabe.AstrolabeApplication.Companion.getString
import com.khalifa.astrolabe.R
import com.khalifa.astrolabe.util.Keys
import org.osmdroid.tileprovider.tilesource.*
import org.osmdroid.tileprovider.tilesource.bing.BingMapTileSource
import org.osmdroid.util.MapTileIndex.*
import java.util.*

/**
 * @author Ahmad Khalifa
 */

object MapSourceFactory {

    lateinit var DEFAULT_MAP_SOURCE: ITileSource

    val onlineMapSources = ArrayList<ITileSource>()

    private val tileSourceImageMap = HashMap<String, Int>()

    fun getIconImage(tileSource: ITileSource?) =
            tileSourceImageMap[tileSource?.toString()] ?: R.drawable.defaultmap

    fun initialize() {
        OpenStreetMap.MAPNIK = OpenStreetMap.getTileSource()
        DEFAULT_MAP_SOURCE = OpenStreetMap.MAPNIK

        Google.HYBRID = Google.getTileSource("Hybrid", "m")
        Google.SATELLITE = Google.getTileSource("Sat", "s")
        Google.ROADS = Google.getTileSource("Roads", "y")

        Bing.HYBRID = Bing.getTileSource(BingMapTileSource.IMAGERYSET_AERIALWITHLABELS)
        Bing.ROAD = Bing.getTileSource( BingMapTileSource.IMAGERYSET_ROAD)
        Bing.AERIAL = Bing.getTileSource(BingMapTileSource.IMAGERYSET_AERIAL)

        MapBox.STREETS = MapBox.getTileSource("mapbox.streets")
        MapBox.LIGHT = MapBox.getTileSource("mapbox.light")
        MapBox.DARK = MapBox.getTileSource("mapbox.dark")
        MapBox.SATELLITE = MapBox.getTileSource("mapbox.satellite")
        MapBox.SATELLITE_STREETS = MapBox.getTileSource("mapbox.streets-satellite")
        MapBox.OUTDOORS = MapBox.getTileSource("mapbox.outdoors")
        MapBox.PENCIL = MapBox.getTileSource("mapbox.pencil")

        ThunderForest.OPEN_CYCLE_MAP = ThunderForest.getTileSource("cycle")
        ThunderForest.TRANSPORT = ThunderForest.getTileSource("transport")
        ThunderForest.TRANSPORT_DARK = ThunderForest.getTileSource("transport-dark")
        ThunderForest.LANDSCAPE = ThunderForest.getTileSource("landscape")
        ThunderForest.OUTDOORS = ThunderForest.getTileSource("outdoors")

        HereWeGo.DAY = HereWeGo.getTileSource("normal.day")
        HereWeGo.NIGHT = HereWeGo.getTileSource("normal.night")
        HereWeGo.TRAFFIC_DAY = HereWeGo.getTileSource("normal.traffic.day")
        HereWeGo.TRAFFIC_NIGHT = HereWeGo.getTileSource("normal.traffic.night")
        HereWeGo.SATELLITE = HereWeGo.getTileSource("satellite.day")
        HereWeGo.HYBRID = HereWeGo.getTileSource("hybrid.day")

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

        fun getTileSource() = object: XYTileSource(
                "Open Street Map",
                0,
                19,
                256,
                ".png",
                arrayOf("https://a.tile.openstreetmap.org/",
                        "https://b.tile.openstreetmap.org/",
                        "https://c.tile.openstreetmap.org/"),
                "© OpenStreetMap contributors"
        ) {
            override fun name() = "Open Street Map"

            override fun toString() = name()
        }
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

            override fun name() = when(lyrs) {
                "m" -> getString(R.string.google_hybrid)
                "s" -> getString(R.string.google_satellite)
                "y" -> getString(R.string.google_roads)
                else -> ""
            }
            
            override fun getTileURLString(pMapTileIndex: Long): String {
                return "$baseUrl/vt/lyrs=$lyrs&x=${getX(pMapTileIndex)}" +
                        "&y=${getY(pMapTileIndex)}&z=${getZoom(pMapTileIndex)}"
            }

            override fun toString(): String {
                return "Google - ${name()}"
            }
        }
    }

    object Bing {

        lateinit var HYBRID: ITileSource

        lateinit var ROAD: ITileSource

        lateinit var AERIAL: ITileSource

        fun getTileSource(style: String) =
                with(object: BingMapTileSource(null){
                    override fun name() = when(style) {
                        BingMapTileSource.IMAGERYSET_AERIAL ->
                            getString(R.string.bing_aerial)
                        BingMapTileSource.IMAGERYSET_AERIALWITHLABELS ->
                            getString(R.string.bing_hybrid)
                        BingMapTileSource.IMAGERYSET_ROAD ->
                            getString(R.string.bing_road)
                        else -> ""
                    }

                    override fun toString(): String {
                        return "Bing - ${name()}"
                    }
                }) {
                    BingMapTileSource.setBingKey(Keys.Bing.API_KEY)
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

        fun getTileSource(mapType: String) = object : XYTileSource(
                "MapBox - $mapType",
                0,
                20,
                256,
                "png",
                arrayOf("http://api.tiles.mapbox.com/v4")
        ) {

            override fun name() = when(mapType) {
                "mapbox.streets" -> getString(R.string.mapbox_streets)
                "mapbox.light" -> getString(R.string.mapbox_light)
                "mapbox.dark" -> getString(R.string.mapbox_dark)
                "mapbox.satellite" -> getString(R.string.mapbox_satellite)
                "mapbox.streets-satellite" -> getString(R.string.mapbox_satellite_streets)
                "mapbox.outdoors" -> getString(R.string.mapbox_outdoors)
                "mapbox.pencil" -> getString(R.string.mapbox_pencil)
                else -> ""
            }

            override fun toString(): String {
                return "MapBox - ${name()}"
            }
            
            override fun getTileURLString(pMapTileIndex: Long): String {
                return "$baseUrl/$mapType/" +
                        "${getZoom(pMapTileIndex)}/${getX(pMapTileIndex)}/${getY(pMapTileIndex)}" +
                        ".$mImageFilenameEnding?access_token=${Keys.MapBox.ACCESS_TOKEN}"
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

        fun getTileSource(mapType: String) = object : XYTileSource(
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
            
            override fun name() = when(mapType) {
                "normal.day" -> getString(R.string.here_we_go_day)
                "normal.night" -> getString(R.string.here_we_go_night)
                "normal.traffic.day" -> getString(R.string.here_we_go_traffic_day)
                "normal.traffic.night" -> getString(R.string.here_we_go_traffic_night)
                "satellite.day" -> getString(R.string.here_we_go_satellite)
                "hybrid.day" -> getString(R.string.here_we_go_hybrid)
                else -> ""
            }

            override fun toString(): String {
                return "HereWeGo - ${name()}"
            }
            
            override fun getTileURLString(pMapTileIndex: Long): String {
                return "$baseUrl/$mapType/" +
                        "${getZoom(pMapTileIndex)}/${getX(pMapTileIndex)}/${getY(pMapTileIndex)}" +
                        "/$tileSizePixels/$mImageFilenameEnding?" +
                        "app_id=${Keys.HereWeGo.APP_ID}&app_code=${Keys.HereWeGo.APP_CODE}"
            }
        }
    }

    object ThunderForest {

        lateinit var OPEN_CYCLE_MAP: ITileSource

        lateinit var TRANSPORT: ITileSource

        lateinit var TRANSPORT_DARK: ITileSource

        lateinit var LANDSCAPE: ITileSource

        lateinit var OUTDOORS: ITileSource

        fun getTileSource(mapType: String) = object : XYTileSource(
                "ThunderForest - $mapType",
                0,
                18,
                256,
                "png",
                arrayOf("https://tile.thunderforest.com"
                )) {

            override fun name() = when(mapType) {
                "cycle" -> getString(R.string.thunderforest_open_cycle_map)
                "transport" -> getString(R.string.thunderforest_transport)
                "transport-dark" -> getString(R.string.thunderforest_transport_dark)
                "landscape" -> getString(R.string.thunderforest_lanscape)
                "outdoors" -> getString(R.string.thunderforest_outdoors)
                else -> ""
            }

            override fun toString(): String {
                return "ThunderForest - ${name()}"
            }
            
            override fun getTileURLString(pMapTileIndex: Long): String {
                return "$baseUrl/$mapType/${getZoom(pMapTileIndex)}/" +
                        "${getX(pMapTileIndex)}/${getY(pMapTileIndex)}.$mImageFilenameEnding?" +
                        "apikey=${Keys.ThunderForest.API_KEY}"
            }
        }
    }
}

