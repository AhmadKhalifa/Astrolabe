package com.khalifa.astrolabe.data.storage.room.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.InputStream

/**
 * @author Ahmad Khalifa
 */
@Entity
class WMSService(@PrimaryKey @ColumnInfo(name = "capabilities_url") val capabilitiesUrl: String,
                 @ColumnInfo(name = "input_stream") val inputStream: InputStream)
