package com.khalifa.astrolabe.data.repository.tilesource

import com.khalifa.astrolabe.data.repository.tilesource.local.LocalTileSourcesRepository
import com.khalifa.astrolabe.data.storage.room.entity.TileSource


/**
 * @author Ahmad Khalifa
 */

class TileSourcesRepository private constructor(localRepository: LocalTileSourcesRepository) :
        BaseTileSourceRepository(localRepository) {

    companion object {

        private var sInstance: TileSourcesRepository? = null

        val instance: TileSourcesRepository
            get() {
                if (sInstance == null)
                    sInstance = TileSourcesRepository(LocalTileSourcesRepository.getDefault())
                if (sInstance == null)
                    throw IllegalStateException("Couldn't instantiate TileSourcesRepository")
                return sInstance as TileSourcesRepository
            }
    }

    override fun getCustomTileSources() = localRepository.getTileSources()

    override fun addCustomTileSource(tileSource: TileSource) =
            localRepository.addTileSource(tileSource)

    override fun deleteCustomTileSource(tileSource: TileSource) =
            localRepository.deleteTileSource(tileSource)
}