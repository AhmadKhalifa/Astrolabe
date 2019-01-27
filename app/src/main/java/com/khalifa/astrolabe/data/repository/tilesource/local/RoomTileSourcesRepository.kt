package com.khalifa.astrolabe.data.repository.tilesource.local

import com.khalifa.astrolabe.data.storage.room.entity.TileSource
import com.khalifa.astrolabe.data.storage.room.dao.TileSourcesDao
import com.khalifa.astrolabe.data.storage.room.AstrolabeDatabase

/**
 * @author Ahmad Khalifa
 */

class RoomTileSourcesRepository : LocalTileSourcesRepository() {

    private var _tileSourcesDao: TileSourcesDao? = null

    private val tileSourcesDao: TileSourcesDao
        get() {
            _tileSourcesDao = _tileSourcesDao ?: AstrolabeDatabase.instance.tileSourcesDao()
            return if (_tileSourcesDao != null) _tileSourcesDao as TileSourcesDao
            else throw IllegalStateException(
                    "AstrolabeDatabase.instance.tileSourcesDao() " +
                            "implementation returns null!"
            )
        }

    override fun addTileSource(vararg tileSource: TileSource) =
            tileSourcesDao.addTileSources(*tileSource)

    override fun getTileSources() =
            tileSourcesDao.getTileSources()

    override fun deleteTileSource(tileSource: TileSource) =
            tileSourcesDao.deleteTileSource(tileSource)
}