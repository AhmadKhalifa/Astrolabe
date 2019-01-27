package com.khalifa.astrolabe.viewmodel.factory

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.khalifa.astrolabe.business.MapViewWrapper
import com.khalifa.astrolabe.viewmodel.fragment.implementation.MapViewModel

class MapViewModelFactory(private val mapViewWrapper: MapViewWrapper) :
        ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return MapViewModel(mapViewWrapper) as T
    }
}