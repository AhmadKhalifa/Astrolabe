package com.khalifa.astrolabe.data.storage.room.converter

import android.arch.persistence.room.TypeConverter
import java.io.BufferedInputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream

/**
 * @author Ahmad Khalifa
 */

class InputStreamConverter {
    companion object {

        @JvmStatic
        @TypeConverter
        fun toInputStream(string: String?): InputStream = ByteArrayInputStream(
                string?.toByteArray(Charsets.UTF_8) ?: "".toByteArray(Charsets.UTF_8)
        )

        @JvmStatic
        @TypeConverter
        fun toString(inputStream: InputStream?): String = inputStream?.let {

            val bis = BufferedInputStream(inputStream)
            bis.mark(1_000_000_000)
            val buf = ByteArrayOutputStream()
            var result = bis.read()
            while (result != -1) {
                buf.write(result.toByte().toInt())
                result = bis.read()
            }
            bis.reset()
            buf.toString("UTF-8")
        } ?: ""
    }
}