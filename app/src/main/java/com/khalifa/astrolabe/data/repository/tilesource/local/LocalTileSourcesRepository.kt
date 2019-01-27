package com.khalifa.astrolabe.data.repository.tilesource.local

import android.arch.lifecycle.LiveData
import com.khalifa.astrolabe.data.storage.room.entity.TileSource
import com.khalifa.astrolabe.data.repository.BaseLocalRepository

/**
 * @author Ahmad Khalifa
 */

abstract class LocalTileSourcesRepository : BaseLocalRepository() {

    companion object {

        fun getDefault(): LocalTileSourcesRepository = RoomTileSourcesRepository()
    }

    abstract fun addTileSource(vararg tileSource: TileSource)

    abstract fun getTileSources(): LiveData<List<TileSource>>

    abstract fun deleteTileSource(tileSource: TileSource)
}