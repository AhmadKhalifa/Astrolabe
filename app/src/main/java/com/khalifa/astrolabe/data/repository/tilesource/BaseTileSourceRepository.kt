package com.khalifa.astrolabe.data.repository.tilesource

import android.arch.lifecycle.LiveData
import com.khalifa.astrolabe.data.repository.Repository
import com.khalifa.astrolabe.data.repository.tilesource.local.LocalTileSourcesRepository
import com.khalifa.astrolabe.data.storage.room.entity.TileSource


/**
 * @author Ahmad Khalifa
 */

abstract class BaseTileSourceRepository protected constructor(
        protected val localRepository: LocalTileSourcesRepository
) : Repository {

    companion object {

        fun getDefault() =  TileSourcesRepository.instance
    }

    abstract fun getCustomTileSources(): LiveData<List<TileSource>>

    abstract fun addCustomTileSource(tileSource: TileSource)

    abstract fun deleteCustomTileSource(tileSource: TileSource)
}