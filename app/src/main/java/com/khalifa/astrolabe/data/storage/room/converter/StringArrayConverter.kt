package com.khalifa.astrolabe.data.storage.room.converter

import android.arch.persistence.room.TypeConverter
import java.lang.StringBuilder

/**
 * @author Ahmad Khalifa
 */

class StringArrayConverter {
    companion object {

        @JvmStatic
        @TypeConverter
        fun toStringArray(string: String?): Array<String> =
                string?.split("|")?.toTypedArray() ?: arrayOf()

        @JvmStatic
        @TypeConverter
        fun toString(stringArray: Array<String>?): String = stringArray?.let { array ->
            StringBuilder().apply {
                for (i in 0 until array.size) {
                    append(array[i])
                    if (i == array.size - 1)
                        append("|")
                }
            }.toString()
        } ?: ""
    }
}