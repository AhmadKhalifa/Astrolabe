package com.khalifa.mapViewer.data.repository.local

import android.arch.lifecycle.LiveData
import com.khalifa.mapViewer.data.model.tileSource.TileSource
import com.khalifa.mapViewer.data.repository.BaseLocalRepository

abstract class BaseLocalTileSourcesRepository : BaseLocalRepository() {

    abstract fun addTileSource(vararg tileSource: TileSource)

    abstract fun getTileSources(): LiveData<List<TileSource>>

    abstract fun deleteTileSource(tileSource: TileSource)

}