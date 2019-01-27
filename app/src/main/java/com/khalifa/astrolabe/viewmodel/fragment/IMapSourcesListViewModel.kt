package com.khalifa.astrolabe.viewmodel.fragment

import android.arch.lifecycle.MutableLiveData
import com.khalifa.astrolabe.data.factory.MapSourceFactory
import com.khalifa.astrolabe.viewmodel.IViewModel

/**
 * @author Ahmad Khalifa
 */

interface IMapSourcesListViewModel : IViewModel {

    val mapSources: MutableLiveData<ArrayList<MapSourceFactory.MapSource>>

    fun loadMapSources()
}