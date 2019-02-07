package com.khalifa.astrolabe.viewmodel.fragment.implementation

import android.arch.lifecycle.ViewModelProviders
import com.khalifa.astrolabe.ui.fragment.AllSourcesFragment
import com.khalifa.astrolabe.viewmodel.BaseRxViewModel
import com.khalifa.astrolabe.viewmodel.fragment.IAllSourcesViewModel

/**
 * @author Ahmad Khalifa
 */

class AllSourcesViewModel : BaseRxViewModel(), IAllSourcesViewModel {

    companion object {

        const val MAP_SOURCES_FRAGMENT_INDEX = 0
        const val WMS_SOURCES_FRAGMENT_INDEX = 1

        fun getInstance(allSourcesFragment: AllSourcesFragment) =
                ViewModelProviders.of(allSourcesFragment).get(AllSourcesViewModel::class.java)
    }

    var currentFragmentIndex = MAP_SOURCES_FRAGMENT_INDEX
}