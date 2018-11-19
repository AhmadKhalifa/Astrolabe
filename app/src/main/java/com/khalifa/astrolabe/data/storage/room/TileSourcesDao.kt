package com.khalifa.astrolabe.data.storage.room

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.khalifa.astrolabe.data.model.tileSource.TileSource

/**
 * @author Ahmad Khalifa
 */

@Dao
interface TileSourcesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTileSources(vararg tileSource: TileSource)

    @Query("SELECT * FROM TileSource")
    fun getTileSources(): LiveData<List<TileSource>>

    @Delete
    fun deleteTileSource(tileSource: TileSource)
}