package com.khalifa.mapviewer

import android.app.Application
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes

class MapApplication : Application() {

    companion object {

        lateinit var instace: MapApplication

        fun getString(@StringRes resId: Int) = instace.getString(resId)

        fun getDrawable(@DrawableRes resId: Int) = instace.getDrawable(resId)
    }

    override fun onCreate() {
        super.onCreate()
        instace = this
    }
}