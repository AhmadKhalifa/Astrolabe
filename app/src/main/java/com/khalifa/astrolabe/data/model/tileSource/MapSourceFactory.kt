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

    val DEFAULT_MAP_SOURCE = OpenStreetMap.MAPNIK

    val onlineMapSources = ArrayList<ITileSource>().apply {
        addAll(OpenStreetMap.getAllSources())
        addAll(Google.getAllSources())
        addAll(Bing.getAllSources())
        addAll(MapBox.getAllSources())
        addAll(HereWeGo.getAllSources())
        addAll(ThunderForest.getAllSources())
    }

    private val tileSourceImageMap = HashMap<String, Int>().apply {
        putAll(OpenStreetMap.getSourcesIconMap())
        putAll(Google.getSourcesIconMap())
        putAll(Bing.getSourcesIconMap())
        putAll(MapBox.getSourcesIconMap())
        putAll(HereWeGo.getSourcesIconMap())
        putAll(ThunderForest.getSourcesIconMap())
    }

    fun getIconImage(tileSource: ITileSource?) =
            tileSourceImageMap[tileSource?.toString()] ?: R.drawable.defaultmap

    interface MapSource {

        fun getTileSource(vararg arg: String): ITileSource

        fun getAllSources(): List<ITileSource>

        fun getSourcesIconMap(): HashMap<String, Int>
    }

    object OpenStreetMap : MapSource {

        val MAPNIK: ITileSource = getTileSource()

        override fun getTileSource(vararg arg: String) = object: XYTileSource(
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

        override fun getAllSources() = listOf(MAPNIK)

        override fun getSourcesIconMap() = hashMapOf(MAPNIK.toString() to R.drawable.osm)
    }

    object Google : MapSource {

        private val HYBRID = getTileSource("Hybrid", "m")

        val SATELLITE = getTileSource("Sat", "s")

        private val ROADS = getTileSource("Roads", "y")

        override fun getTileSource(vararg arg: String) = object : XYTileSource(
                "Google - ${arg[0]}",
                0,
                19,
                256,
                ".png",
                arrayOf("http://mt0.google.com",
                        "http://mt1.google.com",
                        "http://mt2.google.com",
                        "http://mt3.google.com"
                )) {

            override fun name() = when(arg[1]) {
                "m" -> getString(R.string.google_hybrid)
                "s" -> getString(R.string.google_satellite)
                "y" -> getString(R.string.google_roads)
                else -> ""
            }

            override fun getTileURLString(pMapTileIndex: Long): String {
                return "$baseUrl/vt/lyrs=${arg[1]}&x=${getX(pMapTileIndex)}" +
                        "&y=${getY(pMapTileIndex)}&z=${getZoom(pMapTileIndex)}"
            }

            override fun toString(): String {
                return "Google - ${name()}"
            }
        }

        override fun getAllSources() = listOf(HYBRID, SATELLITE, ROADS)

        override fun getSourcesIconMap() = hashMapOf(
                HYBRID.toString() to R.drawable.googlemaps,
                SATELLITE.toString() to R.drawable.googlemaps,
                ROADS.toString() to R.drawable.googlemaps
        )
    }

    object Bing : MapSource {

        private val HYBRID = getTileSource(BingMapTileSource.IMAGERYSET_AERIALWITHLABELS)

        private val ROAD = getTileSource( BingMapTileSource.IMAGERYSET_ROAD)

        private val AERIAL = getTileSource(BingMapTileSource.IMAGERYSET_AERIAL)

        override fun getTileSource(vararg arg: String) =
                with(object: BingMapTileSource(null){
                    override fun name() = when(arg[0]) {
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
                    this.style = arg[0]
                    this
                }

        override fun getAllSources() = listOf(HYBRID, ROAD, AERIAL)

        override fun getSourcesIconMap() = hashMapOf(
                HYBRID.toString() to R.drawable.bing,
                ROAD.toString() to R.drawable.bing,
                AERIAL.toString() to R.drawable.bing
        )
    }

    object MapBox : MapSource {

        private val STREETS = getTileSource("mapbox.streets")

        private val LIGHT = getTileSource("mapbox.light")

        private val DARK = getTileSource("mapbox.dark")

        private val SATELLITE = getTileSource("mapbox.satellite")

        private val SATELLITE_STREETS = getTileSource("mapbox.streets-satellite")

        private val OUTDOORS = getTileSource("mapbox.outdoors")

        private val PENCIL = getTileSource("mapbox.pencil")

        override fun getTileSource(vararg arg: String) = object : XYTileSource(
                "MapBox - ${arg[0]}",
                0,
                20,
                256,
                "png",
                arrayOf("http://api.tiles.mapbox.com/v4")
        ) {

            override fun name() = when(arg[0]) {
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
                return "$baseUrl/${arg[0]}/" +
                        "${getZoom(pMapTileIndex)}/${getX(pMapTileIndex)}/${getY(pMapTileIndex)}" +
                        ".$mImageFilenameEnding?access_token=${Keys.MapBox.ACCESS_TOKEN}"
            }
        }

        override fun getAllSources() =
                listOf(STREETS, LIGHT, DARK, SATELLITE, SATELLITE_STREETS, OUTDOORS, PENCIL)

        override fun getSourcesIconMap() = hashMapOf(
                STREETS.toString() to R.drawable.mapbox,
                LIGHT.toString() to R.drawable.mapbox,
                DARK.toString() to R.drawable.mapbox,
                SATELLITE.toString() to R.drawable.mapbox,
                SATELLITE_STREETS.toString() to R.drawable.mapbox,
                OUTDOORS.toString() to R.drawable.mapbox,
                PENCIL.toString() to R.drawable.mapbox
        )
    }

    object HereWeGo : MapSource {

        private val DAY = getTileSource("normal.day")

        private val NIGHT = getTileSource("normal.night")

        private val TRAFFIC_DAY = getTileSource("normal.traffic.day")

        private val TRAFFIC_NIGHT = getTileSource("normal.traffic.night")

        private val SATELLITE = getTileSource("satellite.day")

        private val HYBRID = getTileSource("hybrid.day")

        override fun getTileSource(vararg arg: String) = object : XYTileSource(
                "MapBox - ${arg[0]}",
                0,
                18,
                256,
                "png",
                arrayOf("https://1.base.maps.api.here.com/maptile/2.1/maptile/newest",
                        "https://2.base.maps.api.here.com/maptile/2.1/maptile/newest",
                        "https://3.base.maps.api.here.com/maptile/2.1/maptile/newest",
                        "https://4.base.maps.api.here.com/maptile/2.1/maptile/newest")
        ) {

            override fun name() = when(arg[0]) {
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
                return "$baseUrl/${arg[0]}/" +
                        "${getZoom(pMapTileIndex)}/${getX(pMapTileIndex)}/${getY(pMapTileIndex)}" +
                        "/$tileSizePixels/$mImageFilenameEnding?" +
                        "app_id=${Keys.HereWeGo.APP_ID}&app_code=${Keys.HereWeGo.APP_CODE}"
            }
        }

        override fun getAllSources() =
                listOf(DAY, NIGHT, TRAFFIC_DAY, TRAFFIC_NIGHT, SATELLITE, HYBRID)

        override fun getSourcesIconMap() = hashMapOf(
                DAY.toString() to R.drawable.herewego,
                NIGHT.toString() to R.drawable.herewego,
                TRAFFIC_DAY.toString() to R.drawable.herewego,
                TRAFFIC_NIGHT.toString() to R.drawable.herewego,
                SATELLITE.toString() to R.drawable.herewego,
                HYBRID.toString() to R.drawable.herewego
        )
    }

    object ThunderForest : MapSource {

        private val OPEN_CYCLE_MAP = getTileSource("cycle")

        private val TRANSPORT = getTileSource("transport")

        private val TRANSPORT_DARK = getTileSource("transport-dark")

        private val LANDSCAPE = getTileSource("landscape")

        private val OUTDOORS = getTileSource("outdoors")

        override fun getTileSource(vararg arg: String) = object : XYTileSource(
                "ThunderForest - ${arg[0]}",
                0,
                18,
                256,
                "png",
                arrayOf("https://tile.thunderforest.com")
        ) {

            override fun name() = when(arg[0]) {
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
                return "$baseUrl/${arg[0]}/${getZoom(pMapTileIndex)}/" +
                        "${getX(pMapTileIndex)}/${getY(pMapTileIndex)}.$mImageFilenameEnding?" +
                        "apikey=${Keys.ThunderForest.API_KEY}"
            }
        }

        override fun getAllSources() =
                listOf(OPEN_CYCLE_MAP, TRANSPORT, TRANSPORT_DARK, LANDSCAPE, OUTDOORS)

        override fun getSourcesIconMap() = hashMapOf(
                OPEN_CYCLE_MAP.toString() to R.drawable.thunderforest,
                TRANSPORT.toString() to R.drawable.thunderforest,
                TRANSPORT_DARK.toString() to R.drawable.thunderforest,
                LANDSCAPE.toString() to R.drawable.thunderforest,
                OUTDOORS.toString() to R.drawable.thunderforest
        )
    }
}

