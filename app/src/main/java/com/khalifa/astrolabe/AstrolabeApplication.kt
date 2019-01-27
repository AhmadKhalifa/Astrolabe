package com.khalifa.astrolabe

import android.app.Application
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat

class AstrolabeApplication : Application() {

    companion object {

        lateinit var instance: AstrolabeApplication

        fun getString(@StringRes resId: Int): String = instance.getString(resId)

        fun getDrawable(@DrawableRes resId: Int) = ContextCompat.getDrawable(instance, resId)

        fun getColor(@ColorRes resId: Int) = ContextCompat.getColor(instance, resId)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}