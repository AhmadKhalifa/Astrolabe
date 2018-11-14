package com.khalifa.mapViewer

import android.app.Application
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import com.khalifa.mapViewer.data.model.tileSource.OnlineTileSourceFactory

class MapApplication : Application() {

    companion object {

        lateinit var instace: MapApplication

        fun getString(@StringRes resId: Int): String = instace.getString(resId)

        fun getDrawable(@DrawableRes resId: Int) = ContextCompat.getDrawable(instace, resId)

        fun getColor(@ColorRes resId: Int) = ContextCompat.getColor(instace, resId)
    }

    override fun onCreate() {
        super.onCreate()
        instace = this
        OnlineTileSourceFactory.initialize(this)
    }
}