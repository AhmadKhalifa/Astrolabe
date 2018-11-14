package com.khalifa.mapViewer.data.storage.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import com.khalifa.mapViewer.MapApplication
import com.khalifa.mapViewer.data.model.tileSource.TileSource

/**
 * @author Ahmad Khalifa
 */

@Database(entities = [TileSource::class], version = 1, exportSchema = false)
abstract class TileSourcesDatabase : RoomDatabase() {
    companion object {

        private const val DATABASE_NAME = "MoviesRoomDatabase.db"

        private var sInstance: TileSourcesDatabase? = null

        fun getInstance(): TileSourcesDatabase {
            if (sInstance == null)
                sInstance = Room.databaseBuilder(
                        MapApplication.instace,
                        TileSourcesDatabase::class.java,
                        DATABASE_NAME
                ).build()
            if (sInstance == null)
                throw IllegalStateException("Couldn't instantiate MoviesDatabase")
            return sInstance as TileSourcesDatabase
        }
    }

    abstract fun tileSourcesDao(): TileSourcesDao
}