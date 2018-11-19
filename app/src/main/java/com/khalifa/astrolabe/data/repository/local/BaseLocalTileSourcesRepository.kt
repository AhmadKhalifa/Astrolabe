package com.khalifa.astrolabe.data.repository.local

import android.arch.lifecycle.LiveData
import com.khalifa.astrolabe.data.model.tileSource.TileSource
import com.khalifa.astrolabe.data.repository.BaseLocalRepository

abstract class BaseLocalTileSourcesRepository : BaseLocalRepository() {

    abstract fun addTileSource(vararg tileSource: TileSource)

    abstract fun getTileSources(): LiveData<List<TileSource>>

    abstract fun deleteTileSource(tileSource: TileSource)

}