package com.khalifa.astrolabe.util

import android.graphics.Color

/**
 * @author Ahmad Khalifa
 */

class ColorUtil private constructor() {
    companion object {

        fun isColorDark(color: Int): Boolean {
            val darkness = 1 - (0.299 * Color.red(color)
                    + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
            return darkness >= 0.5
        }
    }
}