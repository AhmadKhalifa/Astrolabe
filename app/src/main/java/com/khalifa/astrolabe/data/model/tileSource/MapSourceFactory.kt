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

        val name: String

        val icon: Int

        fun getMapSource(vararg arg: String): ITileSource

        fun getAllSources(): List<ITileSource>

        fun getSourcesIconMap(): HashMap<String, Int>
    }

    object OpenStreetMap : MapSource {

        override val name: String
            get() = "Open Street Map"

        override val icon: Int
            get() = R.drawable.osm

        val MAPNIK: ITileSource = getMapSource()

        override fun getMapSource(vararg arg: String) = object: XYTileSource(
                name,
                0,
                19,
                256,
                ".png",
                arrayOf("https://a.tile.openstreetmap.org/",
                        "https://b.tile.openstreetmap.org/",
                        "https://c.tile.openstreetmap.org/"),
                "© OpenStreetMap contributors"
        ) {
            override fun name() = name

            override fun toString() = name()
        }

        override fun getAllSources() = listOf(MAPNIK)

        override fun getSourcesIconMap() = hashMapOf(MAPNIK.toString() to icon)
    }

    object Google : MapSource {

        override val name: String
            get() = "Google"

        override val icon: Int
            get() = R.drawable.googlemaps

        private const val MAP_KEY_HYBRID = "Hybrid"
        private const val MAP_KEY_SATELLITE = "Sat"
        private const val MAP_KEY_ROADS = "Roads"

        private const val TYPE_HYBRID = "m"
        private const val TYPE_SATELLITE = "s"
        private const val TYPE_ROADS = "y"

        private val HYBRID = getMapSource(MAP_KEY_HYBRID, TYPE_HYBRID)

        val SATELLITE = getMapSource(MAP_KEY_SATELLITE, TYPE_SATELLITE)

        private val ROADS = getMapSource(MAP_KEY_ROADS, TYPE_ROADS)

        override fun getMapSource(vararg arg: String) = object : XYTileSource(
                "$name - ${arg[0]}",
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
                TYPE_HYBRID -> getString(R.string.google_hybrid)
                TYPE_SATELLITE -> getString(R.string.google_satellite)
                TYPE_ROADS -> getString(R.string.google_roads)
                else -> ""
            }

            override fun getTileURLString(pMapTileIndex: Long): String {
                return "$baseUrl/vt/lyrs=${arg[1]}&x=${getX(pMapTileIndex)}" +
                        "&y=${getY(pMapTileIndex)}&z=${getZoom(pMapTileIndex)}"
            }

            override fun toString(): String {
                return "$name - ${name()}"
            }
        }

        override fun getAllSources() = listOf(HYBRID, SATELLITE, ROADS)

        override fun getSourcesIconMap() = hashMapOf(
                HYBRID.toString() to icon,
                SATELLITE.toString() to icon,
                ROADS.toString() to icon
        )
    }

    object Bing : MapSource {

        override val name: String
            get() = "Bing"

        override val icon: Int
            get() = R.drawable.bing

        private const val MAP_KEY_AERIAL_WITH_LABELS = BingMapTileSource.IMAGERYSET_AERIALWITHLABELS
        private const val MAP_KEY_ROAD = BingMapTileSource.IMAGERYSET_ROAD
        private const val MAP_KEY_AERIAL = BingMapTileSource.IMAGERYSET_AERIAL

        private val HYBRID = getMapSource(MAP_KEY_AERIAL_WITH_LABELS)

        private val ROAD = getMapSource(MAP_KEY_ROAD)

        private val AERIAL = getMapSource(MAP_KEY_AERIAL)

        override fun getMapSource(vararg arg: String) =
                with(object: BingMapTileSource(null){
                    override fun name() = when(arg[0]) {
                        MAP_KEY_AERIAL ->
                            getString(R.string.bing_aerial)
                        MAP_KEY_AERIAL_WITH_LABELS ->
                            getString(R.string.bing_hybrid)
                        MAP_KEY_ROAD ->
                            getString(R.string.bing_road)
                        else -> ""
                    }

                    override fun toString(): String {
                        return "$name - ${name()}"
                    }
                }) {
                    BingMapTileSource.setBingKey(Keys.Bing.API_KEY)
                    this.style = arg[0]
                    this
                }

        override fun getAllSources() = listOf(HYBRID, ROAD, AERIAL)

        override fun getSourcesIconMap() = hashMapOf(
                HYBRID.toString() to icon,
                ROAD.toString() to icon,
                AERIAL.toString() to icon
        )
    }

    object MapBox : MapSource {

        override val name: String
            get() = "MapBox"

        override val icon: Int
            get() = R.drawable.mapbox

        private const val MAP_KEY_STREETS = "mapbox.streets"
        private const val MAP_KEY_LIGHT = "mapbox.light"
        private const val MAP_KEY_DARK = "mapbox.dark"
        private const val MAP_KEY_SATELLITE = "mapbox.satellite"
        private const val MAP_KEY_SATELLITE_STREETS = "mapbox.streets-satellite"
        private const val MAP_KEY_OUTDOORS = "mapbox.outdoors"
        private const val MAP_KEY_PENCIL = "mapbox.pencil"

        private val STREETS = getMapSource(MAP_KEY_STREETS)

        private val LIGHT = getMapSource(MAP_KEY_LIGHT)

        private val DARK = getMapSource(MAP_KEY_DARK)

        private val SATELLITE = getMapSource(MAP_KEY_SATELLITE)

        private val SATELLITE_STREETS = getMapSource(MAP_KEY_SATELLITE_STREETS)

        private val OUTDOORS = getMapSource(MAP_KEY_OUTDOORS)

        private val PENCIL = getMapSource(MAP_KEY_PENCIL)

        override fun getMapSource(vararg arg: String) = object : XYTileSource(
                "$name - ${arg[0]}",
                0,
                20,
                256,
                "png",
                arrayOf("http://api.tiles.mapbox.com/v4")
        ) {

            override fun name() = when(arg[0]) {
                MAP_KEY_STREETS -> getString(R.string.mapbox_streets)
                MAP_KEY_LIGHT -> getString(R.string.mapbox_light)
                MAP_KEY_DARK -> getString(R.string.mapbox_dark)
                MAP_KEY_SATELLITE -> getString(R.string.mapbox_satellite)
                MAP_KEY_SATELLITE_STREETS -> getString(R.string.mapbox_satellite_streets)
                MAP_KEY_OUTDOORS -> getString(R.string.mapbox_outdoors)
                MAP_KEY_PENCIL -> getString(R.string.mapbox_pencil)
                else -> ""
            }

            override fun toString(): String {
                return "$name - ${name()}"
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
                STREETS.toString() to icon,
                LIGHT.toString() to icon,
                DARK.toString() to icon,
                SATELLITE.toString() to icon,
                SATELLITE_STREETS.toString() to icon,
                OUTDOORS.toString() to icon,
                PENCIL.toString() to icon
        )
    }

    object HereWeGo : MapSource {

        override val name: String
            get() = "HereWeGo"

        override val icon: Int
            get() = R.drawable.herewego

        private const val MAP_KEY_DAY = "normal.day"
        private const val MAP_KEY_NIGHT = "normal.night"
        private const val MAP_KEY_TRAFFIC_DAY = "normal.traffic.day"
        private const val MAP_KEY_TRAFFIC_NIGHT = "normal.traffic.night"
        private const val MAP_KEY_SATELLITE = "satellite.day"
        private const val MAP_KEY_HYBRID = "hybrid.day"

        private val DAY = getMapSource(MAP_KEY_DAY)

        private val NIGHT = getMapSource(MAP_KEY_NIGHT)

        private val TRAFFIC_DAY = getMapSource(MAP_KEY_TRAFFIC_DAY)

        private val TRAFFIC_NIGHT = getMapSource(MAP_KEY_TRAFFIC_NIGHT)

        private val SATELLITE = getMapSource(MAP_KEY_SATELLITE)

        private val HYBRID = getMapSource(MAP_KEY_HYBRID)

        override fun getMapSource(vararg arg: String) = object : XYTileSource(
                "$name - ${arg[0]}",
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
                MAP_KEY_DAY -> getString(R.string.here_we_go_day)
                MAP_KEY_NIGHT -> getString(R.string.here_we_go_night)
                MAP_KEY_TRAFFIC_DAY -> getString(R.string.here_we_go_traffic_day)
                MAP_KEY_TRAFFIC_NIGHT -> getString(R.string.here_we_go_traffic_night)
                MAP_KEY_SATELLITE -> getString(R.string.here_we_go_satellite)
                MAP_KEY_HYBRID -> getString(R.string.here_we_go_hybrid)
                else -> ""
            }

            override fun toString(): String {
                return "$name - ${name()}"
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
                DAY.toString() to icon,
                NIGHT.toString() to icon,
                TRAFFIC_DAY.toString() to icon,
                TRAFFIC_NIGHT.toString() to icon,
                SATELLITE.toString() to icon,
                HYBRID.toString() to icon
        )
    }

    object ThunderForest : MapSource {

        override val name: String
            get() = "ThunderForest"

        override val icon: Int
            get() = R.drawable.thunderforest

        private const val MAP_KEY_OPEN_CYCLE_MAP = "cycle"
        private const val MAP_KEY_TRANSPORT = "transport"
        private const val MAP_KEY_TRANSPORT_DARK = "transport-dark"
        private const val MAP_KEY_LANDSCAPE = "landscape"
        private const val MAP_KEY_OUTDOORS = "outdoors"

        private val OPEN_CYCLE_MAP = getMapSource(MAP_KEY_OPEN_CYCLE_MAP)

        private val TRANSPORT = getMapSource(MAP_KEY_TRANSPORT)

        private val TRANSPORT_DARK = getMapSource(MAP_KEY_TRANSPORT_DARK)

        private val LANDSCAPE = getMapSource(MAP_KEY_LANDSCAPE)

        private val OUTDOORS = getMapSource(MAP_KEY_OUTDOORS)

        override fun getMapSource(vararg arg: String) = object : XYTileSource(
                "$name - ${arg[0]}",
                0,
                18,
                256,
                "png",
                arrayOf("https://tile.thunderforest.com")
        ) {

            override fun name() = when(arg[0]) {
                MAP_KEY_OPEN_CYCLE_MAP -> getString(R.string.thunderforest_open_cycle_map)
                MAP_KEY_TRANSPORT -> getString(R.string.thunderforest_transport)
                MAP_KEY_TRANSPORT_DARK -> getString(R.string.thunderforest_transport_dark)
                MAP_KEY_LANDSCAPE -> getString(R.string.thunderforest_lanscape)
                MAP_KEY_OUTDOORS -> getString(R.string.thunderforest_outdoors)
                else -> ""
            }

            override fun toString(): String {
                return "$name - ${name()}"
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
                OPEN_CYCLE_MAP.toString() to icon,
                TRANSPORT.toString() to icon,
                TRANSPORT_DARK.toString() to icon,
                LANDSCAPE.toString() to icon,
                OUTDOORS.toString() to icon
        )
    }
}

