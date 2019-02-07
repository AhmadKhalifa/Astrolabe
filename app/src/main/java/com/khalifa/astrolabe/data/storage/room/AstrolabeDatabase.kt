package com.khalifa.astrolabe.data.storage.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.khalifa.astrolabe.AstrolabeApplication
import com.khalifa.astrolabe.data.storage.room.converter.StringArrayConverter
import com.khalifa.astrolabe.data.storage.room.converter.InputStreamConverter
import com.khalifa.astrolabe.data.storage.room.entity.TileSource
import com.khalifa.astrolabe.data.storage.room.dao.TileSourcesDao
import com.khalifa.astrolabe.data.storage.room.dao.WMSServicesDao
import com.khalifa.astrolabe.data.storage.room.entity.WMSService

/**
 * @author Ahmad Khalifa
 */

@Database(entities = [TileSource::class, WMSService::class], version = 1, exportSchema = false)
@TypeConverters(InputStreamConverter::class, StringArrayConverter::class)
abstract class AstrolabeDatabase : RoomDatabase() {
    companion object {

        private const val DATABASE_NAME = "Astrolabe-Room.db"

        private var sInstance: AstrolabeDatabase? = null

        val instance: AstrolabeDatabase
            get() {
                if (sInstance == null)
                    sInstance = Room.databaseBuilder(
                            AstrolabeApplication.instance,
                            AstrolabeDatabase::class.java,
                            DATABASE_NAME
                    ).build()
                if (sInstance == null)
                    throw IllegalStateException("Couldn't instantiate AstrolabeDatabase")
                return sInstance as AstrolabeDatabase
            }
    }

    abstract fun tileSourcesDao(): TileSourcesDao

    abstract fun wmsServicesDao(): WMSServicesDao
}