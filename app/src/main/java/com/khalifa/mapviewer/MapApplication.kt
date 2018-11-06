package com.khalifa.mapViewer

import android.app.Application
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat

class MapApplication : Application() {

    companion object {

        lateinit var instace: MapApplication

        fun getString(@StringRes resId: Int) = instace.getString(resId)

        fun getDrawable(@DrawableRes resId: Int) = ContextCompat.getDrawable(instace, resId)
    }

    override fun onCreate() {
        super.onCreate()
        instace = this
    }
}